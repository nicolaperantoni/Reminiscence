package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.frags.FriendListFragment;
import it.unitn.vanguard.reminiscence.frags.ProfileFragment;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
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

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_friendlist_profile);
		try {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (Exception e) {
			// TODO: non farlo
			//sarebbe meglio implementare actionbarsherlock!
		}

		pf = (ProfileFragment) getSupportFragmentManager().findFragmentById(
				R.id.user_profile_fragment);
		fl = (FriendListFragment) getSupportFragmentManager().findFragmentById(
				R.id.friend_list_fragment);

		context = getApplicationContext();
		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);

	}

	@Override
	public void onItemSelect(String name, String surname) {
		Log.e("selecteditem", name + " " + surname);
		pf.update(name, surname);
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
