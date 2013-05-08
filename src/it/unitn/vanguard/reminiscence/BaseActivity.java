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

public class BaseActivity extends SlidingFragmentActivity /*implements OnTaskFinished*/{

	private TextView mChangeProfile;
	private TextView mChangePasswd;
	private TextView mAddFriend;
	private TextView mChangeLocale;
	private TextView mFriendList;
	protected TextView mLogout;
	
	private ProgressDialog dialog;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializeMenu();

		initializeTextViews();

		setSlidingActionBarEnabled(false);
		try {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (Exception e) {
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
				Toast.makeText(BaseActivity.this, "not yet", Toast.LENGTH_SHORT)
						.show();
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

//	@Override
//	public void onTaskFinished(JSONObject res) {
//		if (dialog != null && dialog.isShowing()) {
//			dialog.dismiss();
//		}
//		try {
//			Log.e("", res.toString());
//			if (res.getString("success").equals("true")) {
//				Toast.makeText(getApplicationContext(),
//						getResources().getString(R.string.logout_success),
//						Toast.LENGTH_LONG).show();
//				startActivity(new Intent(BaseActivity.this,
//						LoginActivity.class));
//				this.finish();
//			} else {
//				Toast.makeText(this,
//						getResources().getString(R.string.logout_failed),
//						Toast.LENGTH_LONG).show();
//			}
//		} catch (JSONException e) {
//			Log.e(LoginActivity.class.getName(), e.toString());
//		}
//	}
}
