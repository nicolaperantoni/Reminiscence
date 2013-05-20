package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.Friend;
import it.unitn.vanguard.reminiscence.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		nameTextView = (TextView) getView().findViewById(R.id.profile_name_textview);
		surnameTextView = (TextView) getView().findViewById(R.id.profile_surname_textview);
	}

	public void update(Friend friend) {
		nameTextView.setText(friend.getName());
		surnameTextView.setText(friend.getSurname());
	}	

}
