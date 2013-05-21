package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.adapters.FriendListAdapter;
import it.unitn.vanguard.reminiscence.asynctasks.DeleteFriendTask;
import it.unitn.vanguard.reminiscence.asynctasks.GetFriendsTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendListActivity extends ListActivity implements OnTaskFinished {

	protected static final int ADD_NEW_FRIEND = 0;
	private ArrayList<Friend> friends = new ArrayList<Friend>();
	protected ProgressDialog dialog;
	private Context context;
	private Button btnAddFriend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		context = FriendListActivity.this;
		
		String language = FinalFunctionsUtilities
				.getSharedPreferences(Constants.LANGUAGE_KEY, context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_friend_list);
		
		// Show the Up button in the action bar.
		setupActionBar();
		getFriendsList();
		
		btnAddFriend = (Button) findViewById(R.id.friendlist_add_friend);
		btnAddFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FinalFunctionsUtilities.setSharedPreferences("FriendListActivity", "true", FriendListActivity.this);
				//FriendListActivity.this.finish();
				startActivityForResult(new Intent(context,
						AddFriendActivity.class), ADD_NEW_FRIEND);
			}
		});
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==ADD_NEW_FRIEND) {
			if (resultCode==RESULT_OK) {
				Bundle bd = data.getExtras();
				int id = (Integer) bd.get("id");
				String name = (String) bd.get("name");
				String surname = (String) bd.get("surname");
				String mail = (String) bd.get("mail");
				friends.add(new Friend(name, surname, mail, id));
				setAdapter();
			}
		}
	}



	@Override
	protected void onStart() {
		super.onStart();
		String language = FinalFunctionsUtilities
				.getSharedPreferences(Constants.LANGUAGE_KEY, context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), this);
	}

	private void setAdapter() {
		Friend fr[] = new Friend[friends.size()];
		friends.toArray(fr);
		FriendListAdapter t = new FriendListAdapter(context, fr);
		setListAdapter(t);
	}
	
	private void getFriendsList() {
		if (FinalFunctionsUtilities.isDeviceConnected(context)) {
			dialog = new ProgressDialog(context);
			dialog.setTitle(getResources().getString(R.string.please));
			dialog.setMessage(getResources().getString(R.string.wait));
			dialog.setCancelable(false);
			dialog.show();
			try {
				new GetFriendsTask(this, this).execute();
			} catch (Exception e) {
				Log.e(FriendListActivity.class.getName(), e.toString());
			}
		} else {
			Toast.makeText(context, R.string.connection_fail, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		final int pos = position;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		String delFriend = "";
		delFriend += getResources().getString(R.string.deleteFriendPopupMessage1);
		delFriend += "\n\n" + friends.get(position).getName() + " ";
		delFriend += friends.get(position).getSurname() + " (";
		delFriend += friends.get(position).getEmail() + ")\n\n";
		delFriend += getResources().getString(R.string.deleteFriendPopupMessage2);
		
		builder.setMessage(delFriend)
			.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int id) {
						
						if(FinalFunctionsUtilities.isDeviceConnected(context)) {
							dialog = new ProgressDialog(context);
							dialog.setTitle(getResources().getString(R.string.please));
							dialog.setMessage(getResources().getString(R.string.wait));
							dialog.setCancelable(false);
							dialog.show();
							deleteFriend(pos);
						}
						else {
							Toast.makeText(
									context,
									getResources().getString(R.string.connection_fail),
									Toast.LENGTH_LONG).show();
						}
					}
			})
			.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) { } });

		AlertDialog alert = builder.create();
		alert.show();
		((TextView) alert.findViewById(android.R.id.message))
				.setGravity(Gravity.CENTER);
		((Button) alert.getButton(AlertDialog.BUTTON_POSITIVE))
				.setBackgroundResource(R.drawable.bottone_logout);
		((Button) alert.getButton(AlertDialog.BUTTON_POSITIVE))
				.setTextColor(Color.WHITE);
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
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void friendDeleted(int position) {
		friends.remove(position);
	}

	private void deleteFriend(int position) {
		Friend fr = friends.get(position);
		if(dialog == null) {
			dialog = new ProgressDialog(context);
			dialog.setTitle(getResources().getString(R.string.please));
			dialog.setMessage(getResources().getString(R.string.wait));
			dialog.setCancelable(false);
			dialog.show();
		}
		if (FinalFunctionsUtilities.isDeviceConnected(context)) {
			try {
				new DeleteFriendTask(this, position).execute(Integer.toString(fr.getId()));
			} catch (Exception e) {
				Log.e(FriendListActivity.class.getName(), e.toString());
			}
		}
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		
		int count = 10;
		if(dialog!=null && dialog.isShowing()) { dialog.dismiss(); }

		/*
		 * Ottengo il numero di valori dal json
		 * 
		 * json: {"success":true, "numFriend":2 "f0":{"Nome":asd, "Cognome":asd,
		 * "Id":1} "f1":{"Nome":ciccio, "Cognome":ciccia, "Id":2}}
		 */
		
		try {
			
			if (res.getString("success").equals("true")) {
				try {
					count = Integer.parseInt(res.getString("numFriend"));
					friends.clear();
					for (int i = 0; i < count; i++) {
						String ct = "f" + i;
						JSONObject json = null;
						try {
							json = new JSONObject(res.getString(ct));
							friends.add(new Friend(json.getString("Nome"), json
									.getString("Cognome"), json.getString("Email"),
									Integer.parseInt(json.getString("Id"))));
						} catch (Exception e) {
							Log.e("flf", e.toString());
						}
					}
					setAdapter();
				} catch (Exception e) {
					count = 10;
					Toast.makeText(
							context,
							getResources().getString(R.string.registration_failed),
							Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			Log.e(FriendListActivity.class.getName(), e.toString());
			e.printStackTrace();
		}
	}
}
