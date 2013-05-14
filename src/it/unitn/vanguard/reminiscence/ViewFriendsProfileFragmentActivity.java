package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.frags.FriendListFragment;
import it.unitn.vanguard.reminiscence.frags.ProfileFragment;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

public class ViewFriendsProfileFragmentActivity extends FragmentActivity implements
		FriendListFragment.OnItemSelectListener {

	private Context context;
	private FriendListFragment fl;
	private ProfileFragment pf;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = ViewFriendsProfileFragmentActivity.this;

		setContentView(R.layout.activity_friendlist_profile);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		pf = (ProfileFragment) getSupportFragmentManager().findFragmentById(
				R.id.user_profile_fragment);
		fl = (FriendListFragment) getSupportFragmentManager().findFragmentById(
				R.id.friend_list_fragment);

		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);

	}

	@Override
	public void onItemSelect(Friend friend) {
		Log.e("selecteditem", friend.getName() + " " + friend.getSurname());
		pf.update(friend);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
