package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.adapters.Checkbox_adapter;
import it.unitn.vanguard.reminiscence.asynctasks.GetFriendsTask;
import it.unitn.vanguard.reminiscence.asynctasks.RequestHelpTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONObject;

import android.R.id;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class CheckBoxAmici extends ListActivity implements OnTaskFinished {

	private Button invia_mail;
	private Button btnAddFriend;
	protected ProgressDialog dialog;
	private Context context;
	private Bundle extras;
	private String id_story;

	ArrayList<Friend> friends = new ArrayList<Friend>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		context = CheckBoxAmici.this;
		
		if (FinalFunctionsUtilities.isDeviceConnected(context)) {
			dialog = new ProgressDialog(context);
			dialog.setTitle(getResources().getString(R.string.please));
			dialog.setMessage(getResources().getString(R.string.wait));
			dialog.setCancelable(false);
			dialog.show();
			try {
				new GetFriendsTask(CheckBoxAmici.this, CheckBoxAmici.this).execute();
			} catch (Exception e) {
				Log.e(CheckBoxAmici.class.getName(), e.toString());
			}
		} else {
			Toast.makeText(
					context,
					getResources().getString(R.string.connection_fail),
					Toast.LENGTH_LONG).show();
		}
		
		String language = FinalFunctionsUtilities
				.getSharedPreferences(Constants.LANGUAGE_KEY, context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.checkbox_amici);

		initializeButtons();
		initializeItemListener();
		extras = getIntent().getExtras();
		id_story = (extras.get("id_story").toString());
	}

	private void initializeButtons() {
		
		btnAddFriend = (Button) findViewById(R.id.checklist_add_friend);
		invia_mail = (Button) findViewById(R.id.choosefriend_send_mail);
		
		invia_mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				ListView lv = (ListView) findViewById(id.list);
				
				CheckBox cbox;
				String ids = "";
			
			   for (int i=0; i<lv.getCount(); i++){
				   cbox = (CheckBox) lv.getChildAt(i).findViewById(R.id.check_friend);
				   if(cbox.isChecked()){
					   ids += (friends.get(i).getId()) + ",";
				   }
			   }

			   // Elimino ultima virgola..
			   if(ids.lastIndexOf(',') == (ids.length() - 1)) {
				   ids = ids.substring(0, ids.length() - 1);
			   }
			     
			   if(FinalFunctionsUtilities.isDeviceConnected(context)) {
				   dialog = new ProgressDialog(context);
				   dialog.setTitle(getResources().getString(R.string.please) + getResources().getString(R.string.wait));
				   dialog.setMessage("We are sending an email for each friend selected..");
				   dialog.setCancelable(false);
				   dialog.show();
				   try {
					   new RequestHelpTask(CheckBoxAmici.this).execute(id_story, ids);
				   } catch (Exception e) {
					Log.e(CheckBoxAmici.class.getName(), e.toString());
				   }
			   }
			}
		});
		
		btnAddFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FinalFunctionsUtilities.setSharedPreferences("CheckBoxAmici", "true", context);
				finish();
				startActivity(new Intent(context, AddFriendActivity.class));
			}
		});
	}
	
	private void initializeItemListener() {
		
		ListView lv = (ListView) findViewById(id.list);
		lv.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CheckBox cb = (CheckBox) arg1.findViewById(R.id.check_friend);
				if(cb.isChecked()){ cb.setChecked(false); } else{ cb.setChecked(true); }
			}
		});
	}
	
	private void setAdapter() {
		Friend fr[] = new Friend[friends.size()];
		friends.toArray(fr);
		Checkbox_adapter t = new Checkbox_adapter(this, fr);
		setListAdapter(t);
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
				if (res.getString("Operation").equals("getFriends")) {
					try {
						
						count = Integer.parseInt(res.getString("numFriend"));
						friends.clear();
						if (count < 1) {
							// Caso in cui non ci siano amici LAMER
							friends.add(new Friend(getString(R.string.no_friends_a),
									getString(R.string.no_friends_b), "", -1));
						} else {
							for (int i = 0; i < count; i++) {
								String ct = "f" + i;
								JSONObject json = null;
								try {
									json = new JSONObject(res.getString(ct));
									friends.add(new Friend(
											json.getString("Nome"),
											json.getString("Cognome"), 
											json.getString("Email"),
											Integer.parseInt(json.getString("Id"))));
									} catch (Exception e) {
									Log.e("flf", e.toString());
								}
							}
							setAdapter();
						}
					} catch (Exception e) {
						Log.e(CheckBoxAmici.class.getName(), e.toString());
						e.printStackTrace();
						Toast.makeText(
								context,
								getResources().getString(R.string.registration_failed),
								Toast.LENGTH_LONG).show();
					}
				} else if(res.getString("Operation").equals("requestHelp")) {
					Toast.makeText(
							context,
							"Emails sent",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						context,
						getResources().getString(R.string.connection_fail),
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Log.e(CheckBoxAmici.class.getName(), e.toString());
			e.printStackTrace();
		}
	}
	
}