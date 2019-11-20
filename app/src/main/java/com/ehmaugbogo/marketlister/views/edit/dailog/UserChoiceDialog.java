package com.ehmaugbogo.marketlister.views.edit.dailog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.ehmaugbogo.marketlister.R;

import androidx.fragment.app.DialogFragment;

public class UserChoiceDialog extends DialogFragment
{
	AlertDialog.Builder builder;
	
	String[] userOptions;
	String alertTitle="Photo Options";
	
	public static final String ARGS_STRING_TITLE="com.mycompany.sqliterecycler.Fragments_ARGS_STRING_TITLE";
	
	
	public static UserChoiceDialog newInstance(String title){
		UserChoiceDialog dialog=new UserChoiceDialog();
		Bundle args=new Bundle();
		args.putString(ARGS_STRING_TITLE,title);
		dialog.setArguments(args);
		
		return dialog;
	}
	
	
	//Interface
	OnUserChoiceListener listener;
	
	public interface OnUserChoiceListener{
		void onUserChoice(String choice);
	}
	
	public void setOnPhotoChoiceListener(OnUserChoiceListener listener){
		this.listener=listener;
	}
	
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		builder=new AlertDialog.Builder(getActivity());
		
		
		if(getArguments()!=null&&getArguments().getString(ARGS_STRING_TITLE)=="Sort By"){
			userOptions=getResources().getStringArray(R.array.sort_by_array);
			alertTitle=getArguments().getString(ARGS_STRING_TITLE);
			alertShowSort();
		} else{
			userOptions=getResources().getStringArray(R.array.photo_choice_array);
			alertTakePhoto();
		}
		
		
		
		
		return builder.create();
	}
	
	
	private void alertTakePhoto(){
		builder.setTitle(alertTitle)
			.setIcon(R.drawable.logo_list_icon)
			.setSingleChoiceItems(R.array.photo_choice_array,-1,new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d,int which){
					listener.onUserChoice(userOptions[which]);
					dismiss();
				}
			});
			
	}
	
	private void alertShowSort(){
		builder.setTitle(alertTitle)
			.setIcon(R.drawable.logo_list_icon)
			.setSingleChoiceItems(R.array.sort_by_array,-1,new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d,int which){
					listener.onUserChoice(userOptions[which]);
					dismiss();
				}
			});

	}
	

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try{
			listener=(OnUserChoiceListener)activity;
		} catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+ "must implement method OnPhotoChoiceListener");
		}
	}

	
	
	
	
	
	
}