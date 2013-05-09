package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.AddStoryTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EmptyStoryActivity extends BaseActivity implements OnTaskFinished {

	public static final String YEAR_PASSED_KEY = "emptyStoryYear";

	private TextView mNoStoryTv;
	private EditText mTitleEt;
	private EditText mDescriptionEt;
	private EditText mYearEt;
	private Button mAddBtn;
	private Button mMediaButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_emptystory);
		mNoStoryTv = (TextView) findViewById(R.id.emptystory_nostory_tv);
		mTitleEt = (EditText) findViewById(R.id.emptystory_title_et);
		mDescriptionEt = (EditText) findViewById(R.id.emptystory_desc_et);
		mAddBtn = (Button) findViewById(R.id.emptystory_add_btn);
		mMediaButton = (Button) findViewById(R.id.emptystory_btnPhoto);
		mYearEt = (EditText) findViewById(R.id.emptystory_year_et);
		mYearEt.setText(getIntent().getExtras().getString(YEAR_PASSED_KEY));

		initializeListeners();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	private void initializeListeners() {

		mYearEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {

				if (!checkYearInput(s.toString())) {
					mYearEt.setBackgroundResource(R.drawable.txt_input_bordered_error);
				} else {
					mYearEt.setBackgroundResource(R.drawable.txt_input_bordered);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		mAddBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String year = mYearEt.getText().toString();
				if (FinalFunctionsUtilities
						.isDeviceConnected(EmptyStoryActivity.this))
					if (checkYearInput(year))
						new AddStoryTask(EmptyStoryActivity.this).execute(year,
								mDescriptionEt.getText().toString(), mTitleEt
										.getText().toString());
					else
						showToast(R.string.story_year_broken);

				else
					showToast(false);
			}
		});

		mMediaButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);
			}
		});
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		String n;
		try {
			n = res.getString("success");
			showToast(n.equals("true"));
			finish();
		} catch (JSONException e) {
			showToast(false);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {

			Uri chosenImageUri = data.getData();

			Bitmap mBitmap = null;
			try {

				Bitmap bm = Media.getBitmap(getContentResolver(),
						chosenImageUri);
				mBitmap = Media.getBitmap(getContentResolver(), chosenImageUri);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
				bm.recycle();
				byte[] b = baos.toByteArray();

				baos.close();
				baos = null;

				String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("image", encodedImage));

				try {
					// TODO upload and view of the photo
					// new UploadPhotoTask(this, Constants.imageType.STORY,
					// EmptyStoryActivity.this).execute(encodedImage, story_id);
					// view.setImageBitmap(mBitmap);
				} catch (Exception e) {

					Log.e("log_tag", "Error in http connection " + e.toString());
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

	private boolean checkYearInput(String year) {
		int bornyear = Integer.parseInt(FinalFunctionsUtilities
				.getSharedPreferences(Constants.YEAR_KEY,
						EmptyStoryActivity.this));
		int actualyear = Calendar.getInstance().get(Calendar.YEAR);

		try {
			int iyear = Integer.parseInt(year);
			return (iyear >= bornyear && iyear <= actualyear);
		} catch (Exception e) {
			return false;
		}
	}

	private void showToast(boolean success) {
		Toast.makeText(
				this.getApplicationContext(),
				getResources().getString(
						success ? R.string.story_add_ok
								: R.string.story_add_fail), Toast.LENGTH_SHORT)
				.show();
	}

	private void showToast(int resource) {
		Toast.makeText(this.getApplicationContext(),
				getResources().getString(resource), Toast.LENGTH_SHORT).show();
	}
}
