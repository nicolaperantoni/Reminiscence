package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public abstract class BaseActivity extends SlidingFragmentActivity {

	private Context context;
	
	private TextView mChangeProfile;
	private TextView mChangePasswd;
	private TextView mAddFriend;
	private TextView mChangeLocale;
	private TextView mFriendList;
	protected TextView mLogout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = BaseActivity.this;
		initializeMenu();
		initializeTextViews();
		setSlidingActionBarEnabled(false);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initializeMenu();
		initializeTextViews();
	}

	private void initializeTextViews() {
		mChangeLocale = (TextView) findViewById(R.id.hiddenmenu_changelocale_tv);
		mChangeLocale.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String language = FinalFunctionsUtilities
						.getSharedPreferences(Constants.LANGUAGE_KEY, context);
				Locale locale = new Locale(language);

				if (locale.toString().equals(Locale.ITALIAN.getLanguage())
						|| locale.toString().equals(Locale.ITALY.getLanguage())) {
					locale = Locale.ENGLISH;
				} else if (locale.toString().equals(
						Locale.ENGLISH.getLanguage())) {
					locale = Locale.ITALY;
				}
				
				FinalFunctionsUtilities.switchLanguage(locale, context);
				
				// HOTFIX
				FinalFunctionsUtilities.stories.clear();
				finish();
				startActivity(getIntent());
			}
		});
		mChangePasswd = (TextView) findViewById(R.id.hiddenmenu_changepasswd_tv);
		mChangePasswd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, ChangePassword.class));
			}
		});
		mChangeProfile = (TextView) findViewById(R.id.hiddebmenu_changeimage_tv);
		mChangeProfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, ProfileImageActivity.class));
			}
		});
		mFriendList = (TextView) findViewById(R.id.hiddenmenu_friendList_tv);
		mFriendList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, FriendListActivity.class));
			}
		});
		mAddFriend = (TextView) findViewById(R.id.hiddebmenu_addfriend_tv);
		mAddFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(context, AddFriendActivity.class));
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			getSlidingMenu().toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initializeMenu() {
		setBehindContentView(R.layout.hidden_menu);
		SlidingMenu menu = getSlidingMenu();
		menu.invalidate();
		menu.setBehindWidthRes(R.dimen.menu_offset);
		menu.setFadeDegree(0.35f);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}
}
