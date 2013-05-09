package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.asynctasks.AddStoryTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EmptyStoryActivity extends BaseActivity implements OnTaskFinished {

	public static final String YEAR_PASSED_KEY = "emptyStoryYear";

	private TextView mNoStoryTv;
	private EditText mTitleEt;
	private EditText mDescriptionEt;
	private EditText mYearEt;
	private Button mAddBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_emptystory);
		mNoStoryTv = (TextView) findViewById(R.id.emptystory_nostory_tv);
		mTitleEt = (EditText) findViewById(R.id.emptystory_title_et);
		mDescriptionEt = (EditText) findViewById(R.id.emptystory_desc_et);
		mAddBtn = (Button) findViewById(R.id.emptystory_add_btn);
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
				if (checkYearInput(year))
					if(FinalFunctionsUtilities.isDeviceConnected(EmptyStoryActivity.this))
					new AddStoryTask(EmptyStoryActivity.this).execute(year,
							mDescriptionEt.getText().toString(), mTitleEt
									.getText().toString());
					else
						showToast(false);
				else
					showToast(R.string.story_year_broken);
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
