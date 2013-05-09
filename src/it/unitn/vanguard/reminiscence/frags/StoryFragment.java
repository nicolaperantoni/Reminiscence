package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.CheckBoxAmici;
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

public class StoryFragment extends Fragment implements OnTaskFinished {

	public static final String TITLE_PASSED_KEY = "storyTitle";
	public static final String DESCRIPTION_PASSED_KEY = "descriptionTitle";
	public static final String YEAR_PASSED_KEY = "year";

	private TextView mTitleTv;
	private TextView mDescTv;
	private Integer mYear;
	private String story_id;

	// Image
	private ImageView view;
	private Button btn_aiuto_amico;

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

		mTitleTv = (TextView) getView().findViewById(R.id.story_title_tv);

		mDescTv = (TextView) getView().findViewById(R.id.story_description_tv);

		btn_aiuto_amico = (Button) getView().findViewById(R.id.btn_aiuto_amico);
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
			mTitleTv.setText(b.getString(TITLE_PASSED_KEY));
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
	}

	public Integer getYear() {
		return mYear;
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		// TODO Auto-generated method stub

	}

}
