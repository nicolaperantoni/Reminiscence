package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ProfileFragment extends Fragment {

	private ImageView profilePhoto;
	private String name;
	private String surname;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_story, container, false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		
		
	}

}
