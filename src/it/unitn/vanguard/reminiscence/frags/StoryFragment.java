package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.CheckBoxAmici;
import it.unitn.vanguard.reminiscence.LoginActivity;
import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.ViewStoriesActivity;
import it.unitn.vanguard.reminiscence.asynctasks.GetStoryCoverTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import it.unitn.vanguard.reminiscence.utils.Story;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
	public static final String ID_PASSED_KEY = "id";

	//private TextView mTitleTv;
	private TextView mDescTv;
	private Integer mYear;
	private String story_id;

	// Images
	private ImageView view;
	private Button btn_aiuto_amico;
	private Button btn_upload_photo;
	protected ProgressDialog dialog;
	
	public static StoryFragment newIstance(String title,String desc,String year,String id) {
		StoryFragment sf = new StoryFragment();
		Bundle b = new Bundle();
		b.putString(TITLE_PASSED_KEY, title);
		b.putString(DESCRIPTION_PASSED_KEY, desc);
		b.putString(YEAR_PASSED_KEY, year);
		b.putString(ID_PASSED_KEY, id);
		sf.setArguments(b);
		return sf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_story, container, false);
		
		//if(FinalFunctionsUtilities.isDeviceConnected(v.getContext())) {
			/*
			dialog = new ProgressDialog(v.getContext());
			dialog.setTitle(getResources().getString(R.string.please));
			dialog.setMessage(getResources().getString(R.string.wait));
			dialog.setCancelable(false);
			dialog.show();
			*/
			//String token = FinalFunctionsUtilities.getSharedPreferences(Constants.TOKEN_KEY, getActivity());
			//new GetStoryCoverTask(this, getActivity().getApplicationContext()).execute(token,story_id);
		//}
		
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();

		mYear = getArguments().getInt(YEAR_PASSED_KEY);
		story_id = getArguments().getString(ID_PASSED_KEY);
		
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
				if(story_id != null) {
					checkbox_amici.putExtra("id_story", story_id);
					Log.e("story_id:",story_id);
				}
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
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		try {
			if (res.getString("success").equals("true")) {
				if(res.getString("Operation").equals("GetStoryCover") &&
						!res.getString("numImages").equals("0")) {
					
					// Inserisco la cover della storia..
					byte[] decodedString = Base64.decode(res.getString("cover"), Base64.DEFAULT);
					Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
					view.setImageBitmap(bitmap);
				}
			}
			else {
			}
		} catch (JSONException e) {
			Log.e(LoginActivity.class.getName(), e.toString());
		}
	}

}
