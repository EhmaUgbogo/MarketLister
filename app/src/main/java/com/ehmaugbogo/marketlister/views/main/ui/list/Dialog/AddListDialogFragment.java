package com.ehmaugbogo.marketlister.views.main.ui.list.Dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.ListItemViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.views.main.ui.list.PriorityState;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import static android.provider.MediaStore.Audio.AudioColumns.TITLE_KEY;

public class AddListDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String HOLDER_KEY="com.ehmaugbogo.marketlister_HOLDER_KEY";


    private TextInputEditText titleEditText;
    private TextInputEditText discriptionEditText;
    private ProgressBar progressBar;
    private Spinner prioritySpinner;
    private ListItemViewModel viewModel;

    private String title;
    private String description;
    private PriorityState priorityState;

    public static AddListDialogFragment newInstance(ListItemHolder holder){
        AddListDialogFragment dialogFragment=new AddListDialogFragment();
        Bundle args=new Bundle();
        args.putParcelable(HOLDER_KEY,holder);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view=View.inflate(getActivity(), R.layout.dialog_add_holder,null);

        viewModel = ViewModelProviders.of(getActivity()).get(ListItemViewModel.class);
        titleEditText = view.findViewById(R.id.addlist_name_editText);
        discriptionEditText = view.findViewById(R.id.addlist_discription_editText);
        prioritySpinner = view.findViewById(R.id.addlist_spinner);
        progressBar = view.findViewById(R.id.addlist_progressbar);

        spinnerListener();

        view.findViewById(R.id.addlist_btn).setOnClickListener(this);

        if(getArguments()!=null){
            ListItemHolder editableHolder= getArguments().getParcelable(HOLDER_KEY);
            populateViews(editableHolder);
        }

        builder.setView(view);
        AlertDialog dialog=builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Objects.requireNonNull(dialog.getWindow()).setLayout(650, 1200);
        return dialog;

    }

    private void populateViews(ListItemHolder editableHolder) {
        titleEditText.setText(editableHolder.getTitle());
        discriptionEditText.setText(editableHolder.getDescription());
        prioritySpinner.setSelection(editableHolder.getPriority());
    }

    private void spinnerListener() {
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String)adapterView.getSelectedItem();
                switch (i){
                    case 0: priorityState=PriorityState.NONE;
                        break;
                    case 1: priorityState=PriorityState.URGENT;
                        break;
                    case 2: priorityState=PriorityState.IMPORTANT;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String notingSelectedItem = (String)adapterView.getSelectedItem();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.addlist_btn){
            if(!verifyTextInput()){
                return;
            }
            ListItemHolder holder=new ListItemHolder(title, description);
            holder.setCreated_at(System.currentTimeMillis());
            holder.setUpdated_at(holder.getCreated_at());
            holder.setPriority(priorityState.getStateValue());
            Long aLong = viewModel.insertHolder(holder);

            if(aLong>=0){
                dismiss();
            }
        }
    }

    private boolean verifyTextInput() {
        title = titleEditText.getText().toString().trim();
        description = discriptionEditText.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            titleEditText.setError("Fields Required");
            return false;
        }

        if(TextUtils.isEmpty(description)){
            discriptionEditText.setError("Fields Required");
            return false;
        }


        return true;
    }


}
