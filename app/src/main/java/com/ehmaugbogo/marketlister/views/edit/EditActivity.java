package com.ehmaugbogo.marketlister.views.edit;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.ehmaugbogo.marketlister.DataStoreArchitecture.viewModel.ListItemViewModel;
import com.ehmaugbogo.marketlister.R;
import com.ehmaugbogo.marketlister.model.EditHistory;
import com.ehmaugbogo.marketlister.model.ListItem;
import com.ehmaugbogo.marketlister.model.ListItemHolder;
import com.ehmaugbogo.marketlister.utils.Common;
import com.ehmaugbogo.marketlister.views.BaseActivity;
import com.ehmaugbogo.marketlister.views.edit.dailog.UserChoiceDialog;
import com.ehmaugbogo.marketlister.views.listItems.dialog.BottomSheetFragment;
import com.ehmaugbogo.marketlister.views.main.MainActivity;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class EditActivity extends BaseActivity implements UserChoiceDialog.OnUserChoiceListener
{
	public static final String ITEM_KEY="com.ehmaugbogo.marketlister_ITEM_KEY";

	//Notification
	public static final int NOTIFICATION_ID=001;
	public static final int REQUEST_CODE_PENDING=122;

	//For Image picking without Lib
	public static final int REQUEST_CODE_ACTION_IMAGE_CAPTURE=0001;
	public static final int REQUEST_CODE_ACTION_GET_CONTENT=0002;

	AlertDialog.Builder alertBuilder;

	private TextInputEditText edit_groceryName;
	private TextInputEditText edit_groceryAmount;
	private EditText edit_groceryNote;
	private NumberPicker quantityNumberPicker;
	private ImageView imageView;

	ListItemViewModel viewModel;

	private FloatingActionButton fab_save;
	private ListItem defaultItem;

	private String editHistoryMessage;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		Toolbar toolbar=findViewById(R.id.edit_activity_Toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//Clear Notification
		NotificationManager nM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		nM.cancel(NOTIFICATION_ID);


		initReference();

		if (getIntent().hasExtra(ITEM_KEY)) {
			defaultItem = getIntent().getParcelableExtra(ITEM_KEY);
			populateFields();
		}

		clickListeners();


	}

	public void initReference() {
		edit_groceryAmount = (TextInputEditText)findViewById(R.id.edit_groceryAmount);
		edit_groceryName = (TextInputEditText)findViewById(R.id.edit_groceryName);
		edit_groceryNote = (EditText)findViewById(R.id.edit_activity_noteEditText);
		quantityNumberPicker = (NumberPicker)findViewById(R.id.edit_numberPicker);

		imageView = (ImageView)findViewById(R.id.edit_activity_imageView);
		imageView.setMaxHeight(imageView.getMeasuredWidth());
		registerForContextMenu(imageView);

		fab_save = (FloatingActionButton)findViewById(R.id.edit_fab);

		viewModel = ViewModelProviders.of(this).get(ListItemViewModel.class);

		alertBuilder = new AlertDialog.Builder(this);

		quantityNumberPicker.setMinValue(1);
		quantityNumberPicker.setMaxValue(10);
	}


	public void clickListeners()
	{
		fab_save.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				String name = edit_groceryName.getText().toString();
				String amount = edit_groceryAmount.getText().toString();
				String extraNote = edit_groceryNote.getText().toString();

				if (amount.isEmpty()
					|| name.isEmpty()){
					showToast("Input grocery amount and/or name");
					return;
				}
				
				if (Long.valueOf(amount) > 2147483647){
					showToast("Amount too large");
					return;
				}

				if (name.trim().equals(defaultItem.getName())
					&& Integer.valueOf(amount) == defaultItem.getAmount()
					&& quantityNumberPicker.getValue() == defaultItem.getQuantity()
					&& extraNote.trim().equals(defaultItem.getExtraNote())){
						showToast("Item Unchanged");
						finish();
				} else {
					ListItem item=new ListItem(name,Integer.valueOf(amount), quantityNumberPicker.getValue(),extraNote,defaultItem.getHolder_id());
					item.setId(defaultItem.getId());
					item.setAccomplised(defaultItem.isAccomplised());
					item.setCreated_at(defaultItem.getCreated_at());
					item.setUpdated_at(System.currentTimeMillis());
					viewModel.updateListItem(item);

					ListItemHolder currentHolder = Common.getCurrentHolder();
					currentHolder.setUpdated_at(item.getUpdated_at());
					viewModel.updateHolder(currentHolder);

					showNotification();
					finish();
				} 
			}
		});
	}


	public void showNotification() {
		String content="Item: " + edit_groceryName.getText().toString().trim() +
			" | Amount: N" + String.format("%,d", Integer.valueOf(edit_groceryAmount.getText().toString())) +
			" | Quantity: " + quantityNumberPicker.getValue();

		Intent mainIntent=new Intent(EditActivity.this, MainActivity.class);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent mPendingIntent=PendingIntent.getActivity(EditActivity.this, REQUEST_CODE_PENDING, mainIntent, PendingIntent.FLAG_ONE_SHOT);

		Intent editIntent=new Intent(EditActivity.this, EditActivity.class);
		editIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent ePendIntent=PendingIntent.getActivity(this, REQUEST_CODE_PENDING, editIntent, PendingIntent.FLAG_ONE_SHOT);

		Bitmap largeIcon=BitmapFactory.decodeResource(getResources(), R.drawable.logo_list_icon);


		Notification notification= new NotificationCompat.Builder(EditActivity.this, Common.NOTIFICATION_CHANNEL_ID)
				.setSmallIcon(R.drawable.logo_list_icon_3)
				.setContentTitle("New Item Edit From: "+edit_groceryName.getText().toString().trim())
				.setContentText(content)
				.setColor(getResources().getColor(R.color.colorPrimary))
				.setAutoCancel(true)
				.setContentIntent(mPendingIntent)
				.addAction(R.drawable.logo_list_icon_3, "Visit", ePendIntent)
				.setLargeIcon(largeIcon)
				.setSubText("Recent edit")
				.setStyle(new NotificationCompat.BigPictureStyle()
					  .bigPicture(largeIcon).bigLargeIcon(null)
					  .setBigContentTitle("New Edit Made"))
				.setPriority(NotificationManager.IMPORTANCE_HIGH)
				.build();


		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
		notificationManagerCompat.notify(NOTIFICATION_ID, notification);
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (!(edit_groceryName.getText().toString().equals(defaultItem.getName()))
					|| !(edit_groceryNote.getText().toString().equals(defaultItem.getExtraNote()))
					|| edit_groceryAmount.getText().toString().length() != String.valueOf(defaultItem.getAmount()).length()
					|| quantityNumberPicker.getValue() != defaultItem.getQuantity()) {
					alertPopUp();
					return true;
				} else
				{return false;}

			case R.id.menu_edit_add_photo: alertTakePhoto();
				return true;
			case R.id.menu_edit_restoreDefaultText: populateFields();
				return true;
			case R.id.menu_edit_history: getHistories();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private void getHistories()
	{
		editHistoryMessage = "";
		viewModel.getEditHistoriesWithHolderId(defaultItem.getHolder_id()).observe(this, new Observer<List<EditHistory>>() {
			@Override
			public void onChanged(List<EditHistory> editHistories) {
				for (EditHistory history:editHistories)
				{
					editHistoryMessage = editHistoryMessage + history.getBody() + "\n\n";
				}
			}
		});

		//showToast(msg);

		BottomSheetFragment bottomSheetFragment=BottomSheetFragment.newInstance(0, editHistoryMessage, false);
		bottomSheetFragment.show(getSupportFragmentManager(), "bottomSheet_TAG");
	}

	private void populateFields()
	{
		edit_groceryName.setText(defaultItem.getName());
		edit_groceryAmount.setText(String.valueOf(defaultItem.getAmount()));
		quantityNumberPicker.setValue(defaultItem.getQuantity());
		edit_groceryNote.setText(defaultItem.getExtraNote());
	}

	private void alertPopUp()
	{
		alertBuilder.setTitle("MarketList Unsaved")
			.setMessage("Are you sure you want leave without saving inputs")
			.setIcon(R.drawable.logo_list_icon)
			.setPositiveButton("Yes Leave", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int i)
				{
					showToast("MarketList Unsaved");
					finish();
				}
			}).setNeutralButton("No", null).show();
	}



	private void alertTakePhoto()
	{
		UserChoiceDialog openPhotoDialog=new UserChoiceDialog();
		openPhotoDialog.show(getSupportFragmentManager().beginTransaction(), "Tag");
	}

	@Override
	public void onUserChoice(String choice)
	{
		final String[] photoOptions=getResources().getStringArray(R.array.photo_choice_array);
		if (choice.equals(photoOptions[0])) {
			getPhotoFromCamera();
		} else {
			getExistingPhoto();
		}
	}


	private void getPhotoFromCamera()
	{
		showToast("Opening Camera");

		ImagePicker.create(this)
				.returnMode(ReturnMode.CAMERA_ONLY) // set whether pick and / or camera action should return immediate result or not.
				//.folderMode(true) // folder mode (false by default)
				//.toolbarFolderTitle("Folder") // folder selection title
				//.toolbarImageTitle("Tap to select") // image selection title
				//.toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
				//.includeVideo(true) // Show video on image picker
				.single() // single mode
				//.multi() // multi mode (default mode)
				.limit(10) // max images can be selected (99 by default)
				.showCamera(true) // show camera or not (true by default)
				.imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
				//.origin(images) // original selected images, used in multi mode
				//.exclude(images) // exclude anything that in image.getPath()
				//.excludeFiles(files) // same as exclude but using ArrayList<File>
				.theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
				.enableLog(false) // disabling log
				.start(); // start image picker activity with request code


		//Without Library Logic incase i want to go back
		/*Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (cameraIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(cameraIntent, REQUEST_CODE_ACTION_IMAGE_CAPTURE);
		}*/
	}
	private void getExistingPhoto()
	{
		showToast("Opening Photos");
		ImagePicker.create(this)
				.returnMode(ReturnMode.GALLERY_ONLY) // set whether pick and / or camera action should return immediate result or not.
				.folderMode(true) // folder mode (false by default)
				.toolbarFolderTitle("Folder") // folder selection title
				.toolbarImageTitle("Tap to select") // image selection title
				.toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
				//.includeVideo(true) // Show video on image picker
				.single() // single mode
				//.multi() // multi mode (default mode)
				.limit(10) // max images can be selected (99 by default)
				//.showCamera(true) // show camera or not (true by default)
				//.imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
				//.origin(images) // original selected images, used in multi mode
				//.exclude(images) // exclude anything that in image.getPath()
				//.excludeFiles(files) // same as exclude but using ArrayList<File>
				.theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
				.enableLog(false) // disabling log
				.start(); // start image picker activity with request code

		//Without Library Logic incase i want to go back
		/*Intent photoIntent=new Intent(Intent.ACTION_GET_CONTENT);
		photoIntent.setType("image/*");
		if (photoIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(photoIntent, REQUEST_CODE_ACTION_GET_CONTENT);
		}*/

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
			// Get a list of picked images
			//List<Image> images = ImagePicker.getImages(data);
			// or get a single image only

			Image image = ImagePicker.getFirstImageOrNull(data);
			imageView.setImageURI(data.getData());
		}


		//Without Library Logic incase i want to go back
		/*if (requestCode == REQUEST_CODE_ACTION_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			showToast("Photo Received");
			Bitmap bitmap=(Bitmap)data.getExtras().get("data");
			imageView.setImageBitmap(bitmap);

		} else if (requestCode == REQUEST_CODE_ACTION_GET_CONTENT && resultCode == RESULT_OK) {
			//Bitmap bitmap=BitmapFactory.de
			showToast("Photo Received");
			imageView.setImageURI(data.getData());
		}*/
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		menu.setHeaderTitle("Choose an option");
		switch (v.getId())
		{
			case R.id.edit_activity_imageView:
				menu.add(1, 121, 0, "Delete Image");
				menu.add(1, 122, 1, "Save to download");
				menu.add(1, 123, 2, "Replace Image");
				break;
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 121: showToast("Image Removed");
				break;
			case 122: showToast("Image Saved");
				break;
			case 123: alertTakePhoto();
				break;
		}
		return super.onContextItemSelected(item);
	}


	@Override
	public void onBackPressed()
	{
		if (!(edit_groceryName.getText().toString().equals(defaultItem.getName()))
			|| !(edit_groceryNote.getText().toString().equals(defaultItem.getExtraNote()))
			|| edit_groceryAmount.getText().toString().length() != String.valueOf(defaultItem.getAmount()).length()
			|| quantityNumberPicker.getValue() != defaultItem.getQuantity()) {

			alertPopUp();
		} else {
			super.onBackPressed();
		}
	}



}