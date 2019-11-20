package com.ehmaugbogo.marketlister.views.listItems;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.ListItemViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.utils.Common;
import com.ehmaugbogo.marketlister.views.BaseActivity;
import com.ehmaugbogo.marketlister.views.listItems.adapter.ListerItemAdapter;
import com.ehmaugbogo.marketlister.views.listItems.dialog.AddItemDialog;
import com.ehmaugbogo.marketlister.views.listItems.dialog.BottomSheetFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListerItemActivity extends BaseActivity implements View.OnClickListener {
    public static final String HOLDER_KEY="com.ehmaugbogo.marketlister_HOLDER_KEY";

    private ListerItemAdapter adapter;
    private FloatingActionButton fab;
    private FloatingActionButton fabCal;

    private RecyclerView recyclerview;
    private ListItemViewModel viewModel;

    private ListItemHolder currentHolder;
    private ListItem storedItem;
    private List<ListItem> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if(getIntent()!=null){
            currentHolder = getIntent().getParcelableExtra(HOLDER_KEY);
            Common.setCurrentHolder(currentHolder);
        }

        initReference();
    }

    public void initReference(){
        fab = (FloatingActionButton)findViewById(R.id.list_activity_add_fab);
        fabCal = (FloatingActionButton)findViewById(R.id.list_activity_calculate_fab);

        recyclerview = (RecyclerView)findViewById(R.id.list_activity_recyclerView);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new ListerItemAdapter(this);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerview.getContext(),linearLayoutManager.getOrientation());
        recyclerview.addItemDecoration(dividerItemDecoration);
        recyclerview.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ListItemViewModel.class);
        //viewModelListeners();

        fab.setOnClickListener(this);
        fabCal.setOnClickListener(this);

        hiddenListeners();
        //alertBuilder=new AlertDialog.Builder(this);
    }

    public void hiddenListeners(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){
            public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder viewHolder1){
                return false;
            }
            public void onSwiped(RecyclerView.ViewHolder viewHolder,int position){
                storeBeforeDelete((ListItem)viewHolder.itemView.getTag());

                Snackbar.make(findViewById(R.id.list_activity_rootLayout),"Item Deleted",Snackbar.LENGTH_LONG)
                        .setAction("Undo",new View.OnClickListener(){
                            public void onClick(View view){
                                restoreDelete();
                            }
                        }).show();
            }
        }).attachToRecyclerView(recyclerview);

        NestedScrollView nsv =(NestedScrollView) findViewById(R.id.list_activity_nestedScrollView);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
    }

    private void storeBeforeDelete(ListItem item) {
        storedItem = item;
        viewModel.deleteListItem(storedItem);

    }

    private void restoreDelete() {
        if(storedItem!=null){
            viewModel.insertListItem(storedItem);
        } storedItem=null;
    }

    private void viewModelListeners() {
        viewModel.getItemsWithHolderId(currentHolder.getId()).observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> items) {
                if(items.size()>0){
                    fabCal.show();
                } else {
                    fabCal.hide();
                }
                adapter.setItemList(items);
                allItems=items;
            }
        });
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.list_activity_add_fab:
                openAddDialog();
                break;
            case R.id.list_activity_calculate_fab:
                showTotal();
                break;
        }
    }

    private void openAddDialog() {
        AddItemDialog itemDialog=AddItemDialog.newInstance(currentHolder.getId());
        itemDialog.show(getSupportFragmentManager(),"itemDialog");
    }

    public void showTotal(){
        String itemCount=String.valueOf(adapter.getItemCount());

        showToast("SizeOfItem: "+itemCount);
        BottomSheetFragment bottomSheetFragment=BottomSheetFragment.newInstance(getTotal(),itemCount,true);
        bottomSheetFragment.setCancelable(false);
        bottomSheetFragment.show(getSupportFragmentManager(),"bottomSheet_TAG");
    }

    public int getTotal(){
        int sum=0;

        if(allItems!=null){
            for(ListItem item:allItems){
                int itemAmount=item.getAmount();
                int itemQuantity=item.getQuantity();
                sum+=(itemAmount*itemQuantity);
            }
        }

        return sum;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModelListeners();
    }
}
