package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StoryFragment extends Fragment {
	
	public final String TITLE_PASSED_KEY="storyTitle";
	public final String DESCRIPTION_PASSED_KEY="descriptionTitle";
	
	private TextView mTitleTv;
	private TextView mDescTv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_story, container);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		
		String title = getArguments().getString(TITLE_PASSED_KEY);
		String description = getArguments().getString(DESCRIPTION_PASSED_KEY);
		
		mTitleTv = (TextView) getView().findViewById(R.id.story_title_tv);
		mTitleTv.setText(title);
		
		mDescTv = (TextView) getView().findViewById(R.id.story_description_tv);
		mDescTv.setText(description);
		
	}
	
	
}
