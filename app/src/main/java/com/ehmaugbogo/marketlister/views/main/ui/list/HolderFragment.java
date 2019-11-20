package com.ehmaugbogo.marketlister.views.main.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.ListItemViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.views.BaseFragment;
import com.ehmaugbogo.marketlister.views.main.ui.list.Adapters.HolderAdapter;
import com.ehmaugbogo.marketlister.views.main.ui.list.Dialog.AddListDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HolderFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "HolderFragment";
    private FloatingActionButton fab;
    private ListItemViewModel viewModel;
    private RecyclerView recyclerview;
    private HolderAdapter adapter;
    private ProgressBar progressBar;

    private List<ListItemHolder> allHolders;
    private ListItemHolder storedHolder;
    private List<ListItem> storedHolderItems;
    private boolean deleteHolderAndItsContent;
    private boolean called;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_holder,container,false);

        initReference(view);
        //viewModelListeners();

        return view;
    }

    public void initReference(View view){
        NestedScrollView nsv =view.findViewById(R.id.list_fragment_nestedScrollView);
        progressBar = view.findViewById(R.id.list_fragment_progressbar);
        fab = view.findViewById(R.id.list_fragment_fab);
        fab.setOnClickListener(this);

        viewModel = ViewModelProviders.of(getActivity()).get(ListItemViewModel.class);

        recyclerview = view.findViewById(R.id.list_fragment_recyclerView);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new HolderAdapter(getActivity(), viewModel);
        recyclerview.setAdapter(adapter);

        FrameLayout frameLayout=view.findViewById(R.id.list_fragment_FrameLayout);
        LinearLayout searchFoundLinearLayout=view.findViewById(R.id.list_fragment_search_result_LinearLayout);
        TextView emptySearchTextView=view.findViewById(R.id.list_fragment_emptySearchTextView);

        //alertBuilder=new AlertDialog.Builder(getActivity());

        nestedScrollViewListener(nsv);
    }

    private void nestedScrollViewListener(NestedScrollView nsv) {
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


    private void viewModelListeners() {
        viewModel.getAdapterPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });

        viewModel.getAllHolders().observe(getViewLifecycleOwner(), new Observer<List<ListItemHolder>>() {
            @Override
            public void onChanged(List<ListItemHolder> holders) {
                adapter.setHolderList(holders);
                allHolders = holders;
            }
        });
    }

    @Override
    public void onClick(View view) {
        showAddDataDialog();
    }

    private void showAddDataDialog() {
        AddListDialogFragment dialogFragment=new AddListDialogFragment();
        dialogFragment.show(getActivity().getSupportFragmentManager(),"Dialog");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        ListItemHolder holder=allHolders.get(item.getGroupId());
        switch(item.getItemId()){
            case 111: //Mark //Holder
                //markHolder(holder);
                return true;

            case 112: //Edit //Holder
                AddListDialogFragment addListFragment= AddListDialogFragment.newInstance(holder);
                addListFragment.show(getActivity().getSupportFragmentManager(),"Tag");
                return true;

            case 113: //Delete
                deleteHolderAndItsContent = true;
                called=true;
                storeBeforeDelete(holder);
                return true;


            case 114: //Delete Inner Lists // Of Holder
                deleteHolderAndItsContent = false;
                called=true;
                storeAndDeleteHolderLists(holder);
                return true;

            case 115: //Share List
                //share(holder);
                return true;

            case 116: //Add Reminder
                showSnackbar("Feature available on upgrade");
                return true;

            case 117: //View List Photos
                showSnackbar("Feature available on upgrade");
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }


    //******************* Delete & Restore Single Holder and Lists *******************//

    private void storeBeforeDelete(ListItemHolder holder) {
        storedHolder = holder;
        viewModel.deleteHolder(holder);
        storeAndDeleteHolderLists(holder);
    }

    private void storeAndDeleteHolderLists(ListItemHolder holder){
        viewModel.getItemsWithHolderId(holder.getId()).observe(getViewLifecycleOwner(), new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> listItems) {
                if(called){
                    String msg="Item deleted"; called=false;
                    if(!deleteHolderAndItsContent){
                        if(listItems.size()==0){
                            showSnackbar("Inner list already empty");
                            return;
                        }
                        msg="Inner items deleted ("+listItems.size()+")";
                    }

                    storedHolderItems = listItems;
                    viewModel.deleteAllListItem(holder.getId());

                    Snackbar snackbar=Snackbar.make(getActivity().findViewById(R.id.list_fragment_RelativeLayout),msg,Snackbar.LENGTH_LONG);
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            restoreHolder();
                        }
                    }).show();
                }

            }
        });


    }

    private void restoreHolder() {
        if(storedHolder!=null){
            restoreHolderItems();
            viewModel.insertHolder(storedHolder);
        }
    }

    private void restoreHolderItems() {
        if(storedHolderItems!=null){
            for(ListItem item:storedHolderItems){
                viewModel.insertListItem(item);
            }

            if(deleteHolderAndItsContent){
                showSnackbar("Item restored");
            } else {
                showSnackbar(storedHolderItems.size()+" Inner list Reinserted");
            }

        } storedHolderItems=null; storedHolder=null;
    }




    //Show Snackbar
    private void showSnackbar(String msg){
        Snackbar.make(getActivity().findViewById(R.id.list_fragment_RelativeLayout),msg,Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        viewModelListeners();
    }
}
