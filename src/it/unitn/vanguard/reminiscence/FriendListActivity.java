package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.adapters.FriendListAdapter;
import it.unitn.vanguard.reminiscence.asynctasks.GetFriendsTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

public class FriendListActivity extends ListActivity implements OnTaskFinished {

	ArrayList<Friend> friends = new ArrayList<Friend>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		// Show the Up button in the action bar.
		setupActionBar();
		if (FinalFunctionsUtilities.isDeviceConnected(this)) {
			new GetFriendsTask(this, this).execute();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", this);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), this);
	}

	private void setAdapter() {
		Friend fr[] = new Friend[friends.size()];
		friends.toArray(fr);
		FriendListAdapter t = new FriendListAdapter(this, fr);
		setListAdapter(t);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	//	LayoutInflater inflater = (LayoutInflater) this
	//			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		boolean status;
		int count = 10;
		boolean success = false;

		// ottengo se l'operazione ha avuto successo e il numero di valori dal
		// json
		/*
		 * json: {"success":true, "numFriend":2 "f0":{"Nome":asd, "Cognome":asd,
		 * "Id":1} "f1":{"Nome":ciccio, "Cognome":ciccia, "Id":2}}
		 */
		try {
			success = res.getString("success").equals("true");
			if (success) {
				count = Integer.parseInt(res.getString("numFriend"));
			}
		} catch (Exception e) {
			Log.e("itemselected", e.toString());
		}

		if (success) {
			friends.clear();
			// scorro il json
			if (count < 1) {
				// caso in cui non ci siano amici LAMER
				friends.add(new Friend(getString(R.string.no_friends_a),
						getString(R.string.no_friends_b), -1));
			} else {
				// caso normale
				for (int i = 0; i < count; i++) {
					String ct = "f" + i;
					JSONObject json = null;
					try {
						json = new JSONObject(res.getString(ct));
						friends.add(new Friend(json.getString("Nome"), json
								.getString("Cognome"), Integer.parseInt(json
								.getString("Id"))));
					} catch (Exception e) {
						Log.e("flf", e.toString());
					}
				}
			}
			setAdapter();
		} else {
			Log.e("json", "errore nell operazione success:false");
		}
	}

}
