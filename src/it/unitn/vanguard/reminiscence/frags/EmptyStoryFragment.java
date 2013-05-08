package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.CheckBoxAmici;
import it.unitn.vanguard.reminiscence.R;

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

public class EmptyStoryFragment extends Fragment {

	public static final String YEAR_PASSED_KEY = "emptyStoryYear";

	private TextView mNoStoryTv;
	private EditText mTitleEt;
	private EditText mDescriptionEt;
	private Button mAddBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_emptystory, container,
				false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();

		mNoStoryTv = (TextView) getView().findViewById(
				R.id.emptystory_nostory_tv);
		mTitleEt = (EditText) getView().findViewById(R.id.emptystory_title_et);
		mDescriptionEt = (EditText) getView().findViewById(
				R.id.emptystory_desc_et);
		mAddBtn = (Button) getView().findViewById(R.id.emptystory_add_btn);
		

		String s = String.format(getResources().getString(R.string.no_story),
				getArguments().getInt(YEAR_PASSED_KEY));
		mNoStoryTv.setText(s);

		initializeListeners();
	}

	private void initializeListeners() {
		mAddBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Funzione non disponibile!",
						Toast.LENGTH_LONG).show();
			}
		});
		
	}
}
