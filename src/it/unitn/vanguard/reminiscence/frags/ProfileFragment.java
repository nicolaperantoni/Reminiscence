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

	public void update(String name, String surname) {
		nameTextView.setText(name);
		surnameTextView.setText(surname);
	}	

}
