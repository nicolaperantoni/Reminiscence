package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.adapters.Checkbox_adapter;
import it.unitn.vanguard.reminiscence.asynctasks.GetFriendsTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;

import org.json.JSONObject;

import android.R.id;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class CheckBoxAmici extends ListActivity implements OnTaskFinished{

	private Button invia_mail;
	private Button btnAddFriend;
	protected ProgressDialog dialog;
	
	ArrayList<Friend> friends = new ArrayList<Friend>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkbox_amici);
		if (FinalFunctionsUtilities.isDeviceConnected(this)) {
			dialog = new ProgressDialog(CheckBoxAmici.this);
			dialog.setTitle(getResources().getString(R.string.please));
			dialog.setMessage(getResources().getString(R.string.wait));
			dialog.setCancelable(false);
			dialog.show();
			new GetFriendsTask(this, this).execute();
		}

		initializeButtons();
		initializeItemListener();
	}

	private void initializeButtons() {
		
		btnAddFriend = (Button) findViewById(R.id.checklist_add_friend);
		invia_mail = (Button) findViewById(R.id.choosefriend_send_mail);
		
		
		invia_mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(),
						//"Funzione non disponibile!", Toast.LENGTH_LONG).show();
			ListView lv = (ListView) findViewById(id.list);
			ArrayList<String> nomi = new ArrayList<String>();
			CheckBox cbox;
			String nome;
			
			   for (int i=0; i<lv.getCount(); i++){
				   cbox = (CheckBox) lv.getAdapter().getView(i , null , lv).findViewById(R.id.check_friend);
				   nome = (String) lv.getAdapter().getView(i , null , lv).findViewById(R.id.item_name).toString();
				   
				   if(cbox.isChecked()){
					   nomi.add(nome);
				   }
			   }
			   for(int i=0; i<nomi.size(); i++){
				   System.out.println(nomi.get(i).toString());
			   }
			}
		});
		
		btnAddFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CheckBoxAmici.this,
						AddFriendActivity.class));
			}
		});
	}
	
	private void initializeItemListener(){
		ListView lv = (ListView) findViewById(id.list);
		lv.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CheckBox cb = (CheckBox) arg1.findViewById(R.id.check_friend);
				if( cb.isChecked()){
				cb.setChecked(false);
				}else{ cb.setChecked(true);}
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
		boolean success = false;
		
		if(dialog!=null && dialog.isShowing()) { 	dialog.dismiss(); }

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
				friends.clear();
				
				if (count < 1) {
					// caso in cui non ci siano amici LAMER
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
			}
		} catch (Exception e) {
			Log.e("Error: " + FriendListActivity.class.getName(), "success = false, " + e.toString());
		}
	}
}
