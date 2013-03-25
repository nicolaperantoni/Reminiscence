package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmptyStoryFragment extends Fragment {

	public static final String YEAR_PASSED_KEY = "emptyStoryYear";

	private TextView mNoStoryTv;
	private EditText mTitleEt;
	private EditText mDescriptionEt;
	private Button mAddBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_emptystory, container,false);
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

		String s = String.format(getResources().getString(R.string.no_story), getArguments()
				.getInt(YEAR_PASSED_KEY));
		mNoStoryTv.setText(s);
	}

}
