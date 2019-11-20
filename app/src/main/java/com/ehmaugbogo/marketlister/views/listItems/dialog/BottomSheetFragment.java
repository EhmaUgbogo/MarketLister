package com.ehmaugbogo.marketlister.views.listItems.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ehmaugbogo.marketlister.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
	public static final String ARGS_TOTAL="BottomSheetDialogFragment_ARGS_TOTAL";
	public static final String ARGS_ITEMS="BottomSheetDialogFragment_ARGS_ITEMS";
	public static final String ARGS_BOOLEN="BottomSheetDialogFragment_ARGS_BOOLEAN";


	private static BottomSheetFragment instance;

	public static synchronized BottomSheetFragment newInstance(Integer total, String body, boolean isFromListActivity) {
		instance = new BottomSheetFragment();
		Bundle args=new Bundle();
		args.putInt(ARGS_TOTAL, total);
		args.putString(ARGS_ITEMS, body);
		args.putBoolean(ARGS_BOOLEN, isFromListActivity);
		instance.setArguments(args);

		return instance;
	}

	private BottomSheetFragment()
	{
		//Empty constructor needed
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view=inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
		TextView headDetails=view.findViewById(R.id.b_sheet_Details_TextView);
		TextView itemNo_TextView=view.findViewById(R.id.b_sheet_itemNo_TextView);
		TextView total_TextView=view.findViewById(R.id.b_sheet_Total_TextView);
		Button okButton=view.findViewById(R.id.b_sheet_Botton);


		if (getArguments().getBoolean(ARGS_BOOLEN))
		{
			int total=getArguments().getInt(ARGS_TOTAL);
			String totalFormatted=String.format("%,d", total);
			total_TextView.setText("Total Amount: N" + totalFormatted);
			
			total_TextView.setVisibility(View.VISIBLE);
			itemNo_TextView.setText("Items: " + getArguments().getString(ARGS_ITEMS));
			getDialog().setTitle("MarketList Details");
		}
		else
		{
			itemNo_TextView.setText(getArguments().getString(ARGS_ITEMS));
			okButton.setText("Exit");
			headDetails.setText("Edit History");
			total_TextView.setText("");
			total_TextView.setVisibility(View.GONE);
			getDialog().setTitle("Edit History");
		}



		okButton.setOnClickListener(new OnClickListener(){
				public void onClick(View view)
				{
					dismiss();
				}
			});
		return view;
	}

}