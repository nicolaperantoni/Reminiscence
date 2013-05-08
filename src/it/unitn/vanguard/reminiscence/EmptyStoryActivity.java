package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.asynctasks.AddStoryTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
		mAddBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AddStoryTask(EmptyStoryActivity.this).execute(mYearEt.getText().toString(),
						mDescriptionEt.getText().toString(), mTitleEt.getText()
								.toString());
			}
		});

	}

	@Override
	public void onTaskFinished(JSONObject res) {
		String n;
		try {
			n = res.getString("success");
			Toast.makeText(
					this,
					getResources().getString(
							n.equals("true") ? R.string.story_add_ok
									: R.string.story_add_fail),
					Toast.LENGTH_SHORT).show();
			finish();
		} catch (JSONException e) {
			Toast.makeText(this,
					getResources().getString(R.string.story_add_fail),
					Toast.LENGTH_SHORT).show();
		}

	}
}
