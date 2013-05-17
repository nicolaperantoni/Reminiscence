package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.GetProfilePhotoTask;
import it.unitn.vanguard.reminiscence.asynctasks.UploadPhotoTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileImageActivity extends Activity implements OnTaskFinished {
	
	private Context context;
	private Button btnChangeImage;
	private ImageView imageView;
	protected ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ProfileImageActivity.this;
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_profile_image);
		initializeButtons();
		initializeListeners();
		getProfileImage();
	}

	private void initializeButtons() {		
		btnChangeImage = (Button) findViewById(R.id.btnChangeImgProfile);
		imageView = (ImageView) findViewById(R.id.imgProfile);
	}
	
	private void initializeListeners() {

		btnChangeImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);
			}
		});
	}
	
	private void getProfileImage(){
		try {
			if(dialog == null) {
				dialog = new ProgressDialog(ProfileImageActivity.this);
				dialog.setTitle(getResources().getString(R.string.please));
				dialog.setMessage(getResources().getString(R.string.wait));
				dialog.setCancelable(false);
				dialog.show();
			}
			if (FinalFunctionsUtilities.isDeviceConnected(context)) {
				new GetProfilePhotoTask(this, context).execute();
			} else {
				Toast.makeText(context, R.string.connection_fail, Toast.LENGTH_LONG).show();
			}
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection "+ e.toString());
			e.printStackTrace();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{	
	    super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode == Activity.RESULT_OK)
	    {
	    	
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
			}
       	}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		Locale locale = new Locale(language);

		if(locale.toString().equals(Locale.ITALIAN.getLanguage()) || locale.toString().equals(locale.ITALY.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.it);
		}
		else if(locale.toString().equals(Locale.ENGLISH.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.en);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Locale locale = null;
		switch (item.getItemId()) {
		    case R.id.action_language_it: { locale = Locale.ITALY; break; }
		    case R.id.action_language_en: { locale = Locale.ENGLISH; break; }
		    case android.R.id.home: this.finish();break;
	    }
		
		if(locale != null && FinalFunctionsUtilities.switchLanguage(locale, context)) {
		    // Refresh activity
		    finish();
		    startActivity(getIntent());
	    }
	    return true;
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		if(dialog!=null && dialog.isShowing()) { dialog.dismiss(); }
		try {
			if(res.getString("Operation").equals("GetProfileImage")) {
				if (res.getString("success").equals("true")) {
					byte[] decodedString = Base64.decode(res.getString("photo"), Base64.DEFAULT);
					Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
					imageView.setImageBitmap(bitmap);
				}
			}
			else if(res.getString("Operation").equals("AddProfileImage")){
				if (res.getString("success").equals("true")) {
					getProfileImage();
					Toast.makeText(context, getResources().getString(R.string.profile_image_change_success), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, getResources().getString(R.string.profile_image_change_failed), Toast.LENGTH_LONG).show();
				}
			}
		}
		catch (JSONException e) {
			Log.e(ProfileImageActivity.class.getName(), e.toString());
		}
		catch (NullPointerException e) {
			Log.e(ProfileImageActivity.class.getName(), e.toString());
		}
	}
}
