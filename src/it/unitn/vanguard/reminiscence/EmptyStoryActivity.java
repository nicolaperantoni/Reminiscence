package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.AddStoryTask;
import it.unitn.vanguard.reminiscence.asynctasks.UploadPhotoTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import eu.giovannidefrancesco.DroidTimeline.widget.HorizontalListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class EmptyStoryActivity extends BaseActivity implements OnTaskFinished {

	private Context context;

	public static final String YEAR_PASSED_KEY = "emptyStoryYear";

	private TextView mNoStoryTv;
	private EditText mTitleEt;
	private EditText mDescriptionEt;
	private EditText mYearEt;
	private Button mAddBtn;
	private ImageView mMediaButton;
	private HorizontalListView mMedias;
	private ArrayList<ImageView> imgs;
	private ImageViewAdapter mAdapter;
	private int idStoria;
	private Queue<UploadPhotoTask> toUpload;
	private int totalimgs = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_emptystory);
		context = this;

		mNoStoryTv = (TextView) findViewById(R.id.emptystory_nostory_tv);
		mTitleEt = (EditText) findViewById(R.id.emptystory_title_et);
		mDescriptionEt = (EditText) findViewById(R.id.emptystory_desc_et);
		mAddBtn = (Button) findViewById(R.id.emptystory_add_btn);
		mMediaButton = (ImageView) findViewById(R.id.emptystory_addmedia_imv);
		mYearEt = (EditText) findViewById(R.id.emptystory_year_et);
		mYearEt.setText(""+getIntent().getExtras().getInt(YEAR_PASSED_KEY));

		mMedias = (HorizontalListView) findViewById(R.id.emptystories_imgs_hlv);
		imgs = new ArrayList<ImageView>();
		mAdapter = new ImageViewAdapter();
		mMedias.setAdapter(mAdapter);

		toUpload = new PriorityQueue<UploadPhotoTask>();

		initializeListeners();

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
				if (FinalFunctionsUtilities.isDeviceConnected(context))
					if (checkYearInput(year))
						new AddStoryTask(EmptyStoryActivity.this).execute(year,
								mDescriptionEt.getText().toString(), mTitleEt
										.getText().toString());
					else
						showToast(R.string.story_year_broken);

				else
					showToast(R.string.connection_fail);
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
		String s;
		try {
			s = res.getString("success");
			if (s.equals(s.equals("true"))) {
				try {
					idStoria = res.getInt("idadded");
					sendPhotos();
				} catch (Exception e) {
					if (idStoria > 0) {
						sendPhotos();
					}
				}
			}
			finish();
		} catch (JSONException e) {
			showToast(false);
		}
	}

	private void sendPhotos() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setAutoCancel(true);
		builder.setContentTitle(String.format(
				getString(R.string.story_notification_upload_title),
				toUpload.size(), totalimgs));
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setProgress(toUpload.size(), 0, true);

		if (!toUpload.isEmpty()) {
			FinalFunctionsUtilities.showNotification(context, 1234, builder);
			toUpload.remove().execute();
		} else
			FinalFunctionsUtilities.removeNotification(context, 1234);

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
					toUpload.add(new UploadPhotoTask(this,
							Constants.imageType.STORY, encodedImage, context));
					totalimgs++;
					ImageView image = new ImageView(context);
					image.setImageBitmap(mBitmap);
					image.setAdjustViewBounds(true);
					imgs.add(image);
					mAdapter.notifyDataSetChanged();
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
				.getSharedPreferences(Constants.YEAR_KEY, context));
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

	private class ImageViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return imgs.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return imgs.get(arg0);
		}

	}
}
