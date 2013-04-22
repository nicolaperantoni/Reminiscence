package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.R.layout;
import it.unitn.vanguard.reminiscence.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class ProfileFragment extends Fragment {
	
	public static final String NAME_PASSED_KEY = "name";
	public static final String SURNAME_PASSED_KEY = "surname";
	
	private TextView nameTextView;
	private TextView surnameTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile, container,false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		
		String name = getArguments().getString(NAME_PASSED_KEY);
		String surname = getArguments().getString(SURNAME_PASSED_KEY);
		
		nameTextView = (TextView) getView().findViewById(R.id.profile_name_textview);
		surnameTextView = (TextView) getView().findViewById(R.id.profile_surname_textview);
		
		nameTextView.setText(name);
		surnameTextView.setText(surname);
	}
	
	

}
