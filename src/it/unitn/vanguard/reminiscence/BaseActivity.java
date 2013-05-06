package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	private TextView mChangeProfile;
	private TextView mChangePasswd;
	private TextView mChangeLocale;
	private TextView mFriendList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializeMenu();

		initializeTextViews();
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

	}

	private void initializeMenu() {
		setBehindContentView(R.layout.hidden_menu);
		SlidingMenu menu = getSlidingMenu();
		menu.setBehindWidthRes(R.dimen.menu_offset);
		menu.setFadeDegree(0.35f);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}
}
