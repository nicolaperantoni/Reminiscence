package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	private TextView mChangeProfile;
	private TextView mChangePasswd;
	private TextView mAddFriend;
	private TextView mChangeLocale;
	private TextView mFriendList;
	private TextView mLogout;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializeMenu();

		initializeTextViews();
		
		setSlidingActionBarEnabled(false);
		try{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}catch (Exception e) {
			// TODO: non farlo
		}
	}

	private void initializeTextViews() {
		mChangeLocale = (TextView) findViewById(R.id.hiddenmenu_changelocale_tv);
		mChangeLocale.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String language = FinalFunctionsUtilities.getSharedPreferences(
						"language", getApplicationContext());
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
				BaseActivity.this.finish();
				startActivity(getIntent());
			}
		});
		mChangePasswd = (TextView) findViewById(R.id.hiddenmenu_changepasswd_tv);
		mChangePasswd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(BaseActivity.this,
						ChangePassword.class));
			}
		});
		mChangeProfile = (TextView) findViewById(R.id.hiddebmenu_changeimage_tv);
		mChangeProfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(BaseActivity.this,
						ProfileImageActivity.class));
			}
		});
		mFriendList = (TextView) findViewById(R.id.hiddenmenu_friendList_tv);
		mFriendList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(BaseActivity.this,
						ViewFriendsProfileFragmentActivity.class));
			}
		});
		mAddFriend = (TextView) findViewById(R.id.hiddebmenu_addfriend_tv);
		mAddFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(BaseActivity.this, "not yet", Toast.LENGTH_SHORT).show();
			}
		});
		mLogout = (TextView) findViewById(R.id.hiddebmenu_logout_tv);
		mLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(BaseActivity.this, "not yet", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
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
