package com.ehmaugbogo.marketlister.views.listItems.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.ListItemViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.views.main.ui.list.Dialog.AddListDialogFragment;
import com.ehmaugbogo.marketlister.views.main.ui.list.PriorityState;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class AddItemDialog extends DialogFragment implements View.OnClickListener {
    private static final String HOLDER_KEY="com.ehmaugbogo.marketlister_HOLDER_KEY";
    private ListItemViewModel viewModel;
    private TextInputEditText name_editText;
    private TextInputEditText amount_editText;
    private TextView quantityTextView;
    private int quantity=1;
    private String name;
    private String amount;
    private long holder_id;


    public static AddItemDialog newInstance(long holder_id){
        AddItemDialog itemDialog=new AddItemDialog();
        Bundle args=new Bundle();
        args.putLong(HOLDER_KEY,holder_id);
        itemDialog.setArguments(args);
        return itemDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view=View.inflate(getActivity(), R.layout.dialog_add_item,null);

        viewModel = ViewModelProviders.of(getActivity()).get(ListItemViewModel.class);
        name_editText = view.findViewById(R.id.add_item_mainEditText);
        amount_editText = view.findViewById(R.id.add_item_amountEditText);
        quantityTextView = view.findViewById(R.id.add_item_quantityTextView);

        Button increaseBtn= view.findViewById(R.id.add_item_buttonIncrease);
        Button decreaseBtn=view.findViewById(R.id.add_item_buttonDecrease);
        Button addBtn=view.findViewById(R.id.add_item_buttonAdd);

        increaseBtn.setOnClickListener(this);
        decreaseBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);

        builder.setView(view);
        AlertDialog dialog=builder.create();

        if(getArguments()!=null){
            holder_id = getArguments().getLong(HOLDER_KEY);
        }

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Objects.requireNonNull(dialog.getWindow()).setLayout(650, 1200);
        return dialog;

    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.add_item_buttonAdd){
            if(verifyTextInput()){
                ListItem item=new ListItem(name,Integer.parseInt(amount),quantity,null,holder_id);
                item.setCreated_at(System.currentTimeMillis());
                item.setUpdated_at(item.getCreated_at());
                Long aLong = viewModel.insertListItem(item);
                if(aLong>=0){
                    dismiss();
                }
            }
        }
    }

    public void increase(){
        quantity++;
        quantityTextView.setText(Integer.toString(quantity));
    }

    public void decrease(){
        if(quantity==1){
            return;
        } quantity--; quantityTextView.setText(Integer.toString(quantity));
    }



    private boolean verifyTextInput() {
        name = name_editText.getText().toString().trim();
        amount = amount_editText.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            name_editText.setError("Fields Required");
            return false;
        }

        if(TextUtils.isEmpty(amount)){
            amount_editText.setError("Fields Required");
            return false;
        }


        return true;
    }

    protected void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
