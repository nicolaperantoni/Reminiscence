package it.unitn.vanguard.reminiscence.frags;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import it.unitn.vanguard.reminiscence.CheckBoxAmici;
import it.unitn.vanguard.reminiscence.ProfileImageActivity;
import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.asynctasks.UploadPhotoTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StoryFragment extends DialogFragment implements OnTaskFinished {

	public static final String TITLE_PASSED_KEY = "storyTitle";
	public static final String DESCRIPTION_PASSED_KEY = "descriptionTitle";
	public static final String YEAR_PASSED_KEY = "year";

	//private TextView mTitleTv;
	private TextView mDescTv;
	private Integer mYear;
	private String story_id;

	// Image
	private ImageView view;
	private Button btn_aiuto_amico;
	private Button btn_upload_photo;
	protected ProgressDialog dialog;
	
	public static StoryFragment newIstance(String title,String desc,String year){
		StoryFragment sf = new StoryFragment();
		Bundle b = new Bundle();
		b.putString(TITLE_PASSED_KEY, title);
		b.putString(DESCRIPTION_PASSED_KEY, desc);
		b.putString(YEAR_PASSED_KEY, year);
		sf.setArguments(b);
		return sf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_story, container, false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();

		mYear = getArguments().getInt(YEAR_PASSED_KEY);

		//mTitleTv = (TextView) getView().findViewById(R.id.story_title_tv);

		mDescTv = (TextView) getView().findViewById(R.id.story_description_tv);

		btn_aiuto_amico = (Button) getView().findViewById(R.id.btn_aiuto_amico);
		btn_upload_photo = (Button) getView().findViewById(R.id.btn_upload_photo);
		view = (ImageView) getView().findViewById(R.id.photo);

		initializeTexts();
		
	}

	@Override
	public void onResume() {
		super.onResume();
		initializeTexts();
	}

	private void initializeTexts() {
		if (getArguments() != null) {
			Bundle b = getArguments();
			//mTitleTv.setText(b.getString(TITLE_PASSED_KEY));
			getDialog().setTitle(b.getString(TITLE_PASSED_KEY));
			mDescTv.setText(b.getString(DESCRIPTION_PASSED_KEY));
		}

		btn_aiuto_amico.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent checkbox_amici = new Intent(getActivity(),
						CheckBoxAmici.class);
				startActivity(checkbox_amici);

			}
		});
		
		btn_upload_photo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);
			}
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{	
	    super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode == Activity.RESULT_OK)
	    {
	    	/*
	        Uri chosenImageUri = data.getData();
	        
	        Bitmap mBitmap = null;
	        try {

				Bitmap bm = Media.getBitmap(context.getContentResolver(), chosenImageUri);
				mBitmap = Media.getBitmap(context.getContentResolver(), chosenImageUri);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
				bm.recycle();
				byte[] b = baos.toByteArray();
				
				baos.close();
				baos = null;
				
				String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image",encodedImage));

				try {
					// Show dialog
					dialog = new ProgressDialog(ProfileImageActivity.this);
					dialog.setTitle(getResources().getString(R.string.please));
					dialog.setMessage(getResources().getString(R.string.wait));
					dialog.setCancelable(false);
					dialog.show();
					
					if (FinalFunctionsUtilities.isDeviceConnected(context)) {
						new UploadPhotoTask(this, Constants.imageType.PROFILE,encodedImage, context).execute();
					} else {
						Toast.makeText(context, R.string.connection_fail, Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e) {
					Log.e("log_tag", "Error in http connection "+ e.toString());
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
       	}
	}

	public Integer getYear() {
		return mYear;
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		// TODO Auto-generated method stub
	}

}
