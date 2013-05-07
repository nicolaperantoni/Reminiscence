package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.asynctasks.UploadPhotoTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryFragment extends Fragment implements OnTaskFinished,Comparable<StoryFragment> {

	public static final String TITLE_PASSED_KEY = "storyTitle";
	public static final String DESCRIPTION_PASSED_KEY = "descriptionTitle";
	public static final String YEAR_PASSED_KEY = "year";
	
	private TextView mTitleTv;
	private TextView mDescTv;
	private int mYear;
	private String story_id;
	
	// Image
	private ImageView view;
	private Button btnPhoto;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_story, container,false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();

		String title = getArguments().getString(TITLE_PASSED_KEY);
		String description = getArguments().getString(DESCRIPTION_PASSED_KEY);
		mYear = getArguments().getInt(YEAR_PASSED_KEY);

		mTitleTv = (TextView) getView().findViewById(R.id.story_title_tv);
		mTitleTv.setText(title);

		mDescTv = (TextView) getView().findViewById(R.id.story_description_tv);
		mDescTv.setText(description);
		btnPhoto = (Button) getView().findViewById(R.id.btnPhoto);
		view = (ImageView) getView().findViewById(R.id.photo);

		initializeTexts();

	}

	private void initializeTexts() {
		if (getArguments() != null) {
			Bundle b = getArguments();
			mTitleTv.setText(b.getString(TITLE_PASSED_KEY));
			mDescTv.setText(b.getString(DESCRIPTION_PASSED_KEY));
		}

		btnPhoto.setOnClickListener(new View.OnClickListener() {

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
	    	
	        Uri chosenImageUri = data.getData();
	        
	        Bitmap mBitmap = null;
	        try {

				Bitmap bm = Media.getBitmap(getView().getContext().getContentResolver(), chosenImageUri);
				mBitmap = Media.getBitmap(getView().getContext().getContentResolver(), chosenImageUri);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
				bm.recycle();
				byte[] b = baos.toByteArray();
				
				baos.close();
				baos = null;
				
				String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image",encodedImage));

				try{
					new UploadPhotoTask(this, Constants.imageType.STORY, getActivity().getApplicationContext()).execute(encodedImage, story_id);
					view.setImageBitmap(mBitmap);
				}catch(Exception e){

					Log.e("log_tag", "Error in http connection "+ e.toString());
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	}
	}

	public int getYear() {
		return mYear;
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(StoryFragment arg0) {
		if(arg0.getYear()>this.mYear)
			return -1;
		else if(arg0.getYear()==this.mYear)
			return 0;
		else
			return 1;
	}

}
