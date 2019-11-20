package com.ehmaugbogo.marketlister.views.main.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.ListItemViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.views.BaseFragment;
import com.ehmaugbogo.marketlister.views.main.ui.list.Adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;


public class BaseHolderFragment extends BaseFragment {

    private ViewPager viewPager;
    private ListItemViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_holder,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.fragment_holder_viewpager_container);
        TabLayout tabLayout = view.findViewById(R.id.fragment_holder_tabLayout);

        ViewPagerAdapter adapter = ViewPagerAdapter.newInstance(getActivity().getSupportFragmentManager());
        addFragAndTitle(adapter);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewModel = ViewModelProviders.of(getActivity()).get(ListItemViewModel.class);

        pageListener();

    }

    private void pageListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                viewModel.setAdapterPosition(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addFragAndTitle(ViewPagerAdapter adapter) {
        adapter.addFragAndTitle(new HolderFragment(),"List");
        adapter.addFragAndTitle(new NoteFragment(),"Note");
    }

}
