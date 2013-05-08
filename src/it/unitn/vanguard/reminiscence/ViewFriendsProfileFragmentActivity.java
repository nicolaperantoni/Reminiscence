package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.frags.FriendListFragment;
import it.unitn.vanguard.reminiscence.frags.ProfileFragment;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ViewFriendsProfileFragmentActivity extends FragmentActivity implements FriendListFragment.OnItemSelectListener{
	
	private Context context;
	private FriendListFragment fl;
	private ProfileFragment pf;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_friendlist_profile);
		
		pf = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.user_profile_fragment);
		fl = (FriendListFragment) getSupportFragmentManager().findFragmentById(R.id.friend_list_fragment);
		
		context = getApplicationContext();
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		
	}
	
	
	@Override
	public void onItemSelect(String name, String surname) {
		Log.e("selecteditem", name + " " + surname);
		pf.update(name, surname);
	}
}
