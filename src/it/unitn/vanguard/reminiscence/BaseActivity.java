package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.LogoutTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	private Context context;
	
	private TextView mChangeProfile;
	private TextView mChangePasswd;
	private TextView mAddFriend;
	private TextView mChangeLocale;
	private TextView mFriendList;
	protected TextView mLogout;

	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = BaseActivity.this;
		
		initializeMenu();
		initializeTextViews();
		setSlidingActionBarEnabled(false);
		
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void initializeTextViews() {
		mChangeLocale = (TextView) findViewById(R.id.hiddenmenu_changelocale_tv);
		mChangeLocale.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String language = FinalFunctionsUtilities.getSharedPreferences(
						"language", context);
				Locale locale = new Locale(language);

				if (locale.toString().equals(Locale.ITALIAN.getLanguage())
						|| locale.toString().equals(locale.ITALY.getLanguage())) {
					locale = Locale.ENGLISH;
				} else if (locale.toString().equals(
						Locale.ENGLISH.getLanguage())) {
					locale = Locale.ITALY;
				}
				FinalFunctionsUtilities.switchLanguage(locale,
						BaseActivity.this);
				//this is an hotfix!
				FinalFunctionsUtilities.stories.clear();
				BaseActivity.this.finish();
				startActivity(getIntent());
			}
		});
		mChangePasswd = (TextView) findViewById(R.id.hiddenmenu_changepasswd_tv);
		mChangePasswd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,
						ChangePassword.class));
			}
		});
		mChangeProfile = (TextView) findViewById(R.id.hiddebmenu_changeimage_tv);
		mChangeProfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,
						ProfileImageActivity.class));
			}
		});
		mFriendList = (TextView) findViewById(R.id.hiddenmenu_friendList_tv);
		mFriendList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,
						ViewFriendsProfileFragmentActivity.class));
			}
		});
		mAddFriend = (TextView) findViewById(R.id.hiddebmenu_addfriend_tv);
		mAddFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context,
						AddFriendActivity.class));
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
		menu.setBehindWidthRes(R.dimen.menu_offset);
		menu.setFadeDegree(0.35f);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}
}
