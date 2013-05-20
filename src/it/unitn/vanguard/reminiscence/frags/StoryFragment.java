package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.CheckBoxAmici;
import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Story;

import org.json.JSONObject;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryFragment extends DialogFragment implements OnTaskFinished {
	
	public static final String TITLE_PASSED_KEY = "storyTitle";
	public static final String DESCRIPTION_PASSED_KEY = "storyDescription";
	public static final String YEAR_PASSED_KEY = "storyYear";
	public static final String ID_PASSED_KEY = "storyId";
	public static final String COVER_PASSED_KEY = "storyCover";
	
	private TextView mTitleTv;
	private TextView mDescTv;
	private Integer mYear;
	private static String story_id;
	private static Story story;
	
	// Images
	private ImageView cover;
	private Bitmap coverBtm;
	private Button btn_aiuto_amico;
	private Button btn_upload_photo;
	protected ProgressDialog dialog;
	
	public static StoryFragment newInstance(Story storia) {
		StoryFragment sf = new StoryFragment();
		
		story = storia;
		Bundle b = new Bundle();
		b.putString(TITLE_PASSED_KEY, story.getTitle());
		b.putString(DESCRIPTION_PASSED_KEY, story.getDesc());
		b.putString(YEAR_PASSED_KEY, story.getAnno() + "");
		b.putString(ID_PASSED_KEY, story.getId());
		
		story_id = story.getId();
		
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
		story_id = getArguments().getString(ID_PASSED_KEY);
		
		mDescTv = (TextView) getView().findViewById(R.id.story_description_tv);
		btn_aiuto_amico = (Button) getView().findViewById(R.id.btn_aiuto_amico);
		btn_upload_photo = (Button) getView().findViewById(R.id.btn_upload_photo);
		cover = (ImageView) getView().findViewById(R.id.photo);
		
		if(story.getBackground() != null) {
			Bitmap bmp = story.getBackground();
			cover.setImageBitmap(bmp);
			cover.setVisibility(0); // Visible
		}
		
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
			getDialog().setTitle(b.getString(YEAR_PASSED_KEY) + ":\t" + b.getString(TITLE_PASSED_KEY));
			mDescTv.setText(b.getString(DESCRIPTION_PASSED_KEY));
			getDialog().setTitle(story.getAnno() + ":\t" + story.getTitle());
			mDescTv.setText(story.getDesc());

//			cover.setMaxWidth((int)(getActivity().getWindow().peekDecorView().getWidth()*0.7)); 
//			cover.setMaxHeight((int)(getActivity().getWindow().peekDecorView().getHeight()*0.4)); 
			
			getDialog().getWindow().setLayout(
					(int)(getActivity().getWindow().peekDecorView().getWidth()*0.8),
					(int)(getActivity().getWindow().peekDecorView().getHeight()*0.8)
			);
		}

		btn_aiuto_amico.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent checkbox_amici = new Intent(getActivity(), CheckBoxAmici.class);
				if(story != null) { checkbox_amici.putExtra("id_story", story.getId() + ""); }
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
		if (dialog != null && dialog.isShowing()) { dialog.dismiss(); }
		
//		try {
//			if (res.getString("success").equals("true")) {
//				if(res.getString("Operation").equals("GetStoryCover") &&
//						!res.getString("numImages").equals("0")) {
//					
//					// Inserisco la cover della storia..
//					byte[] decodedString = Base64.decode(res.getString("cover"), Base64.DEFAULT);
//					Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
//					cover.setImageBitmap(bitmap);
//				}
//			}
//		} catch (JSONException e) {
//			Log.e(StoryFragment.class.getName(), e.toString());
//			e.printStackTrace();
//		} catch (Exception e) {
//			Log.e(StoryFragment.class.getName(), e.toString());
//			e.printStackTrace();
//		}
	}
}