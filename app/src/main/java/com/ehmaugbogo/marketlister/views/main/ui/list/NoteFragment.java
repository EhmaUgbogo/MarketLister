package com.ehmaugbogo.marketlister.views.main.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.ListItemViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.views.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class NoteFragment extends BaseFragment {

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);

        fab = view.findViewById(R.id.fragment_other_fab);
        setUpViewModel();

        return view;
    }

    private void setUpViewModel() {
        ListItemViewModel viewModel = ViewModelProviders.of(getActivity()).get(ListItemViewModel.class);
        viewModel.getAdapterPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != 1) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
    }


}
