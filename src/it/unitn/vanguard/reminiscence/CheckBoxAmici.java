package it.unitn.vanguard.reminiscence;

import java.util.ArrayList;

import org.json.JSONObject;

import it.unitn.vanguard.reminiscence.adapters.Checkbox_adapter;
import it.unitn.vanguard.reminiscence.adapters.FriendListAdapter;
import it.unitn.vanguard.reminiscence.asynctasks.GetFriendsTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckBoxAmici extends ListActivity implements OnTaskFinished{

	private Button invia_mail;
	ArrayList<Friend> friends = new ArrayList<Friend>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkbox_amici);
		if (FinalFunctionsUtilities.isDeviceConnected(this)) {
			new GetFriendsTask(this, this).execute();
		}
		
		initializeButtons();
	}

	private void initializeButtons() {
		invia_mail = (Button) findViewById(R.id.btn_invia_mail);
		invia_mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"Funzione non disponibile!", Toast.LENGTH_LONG).show();
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
		boolean status;
		int count = 10;
		boolean success = false;
		String op = null;

		// ottengo se l'operazione ha avuto successo e il numero di valori dal
		// json
		/*
		 * json: {"success":true, "numFriend":2 "f0":{"Nome":asd, "Cognome":asd,
		 * "Id":1} "f1":{"Nome":ciccio, "Cognome":ciccia, "Id":2}}
		 */
		try {
			success = res.getString("success").equals("true");
			op = res.getString("operation");
			if (success) {
				count = Integer.parseInt(res.getString("numFriend"));
			}
		} catch (Exception e) {
			Log.e("itemselected", e.toString());
		}

		if (success) {
			friends.clear();
			if (op != null && op.equals("getFriends")) {
				// scorro il json
				if (count < 1) {
					// caso in cui non ci siano amici LAMER
					friends.add(new Friend(getString(R.string.no_friends_a),
							getString(R.string.no_friends_b), "", -1));
				} else {
					// caso normale
					for (int i = 0; i < count; i++) {
						String ct = "f" + i;
						JSONObject json = null;
						try {
							json = new JSONObject(res.getString(ct));
							friends.add(new Friend(json.getString("Nome"), json
									.getString("Cognome"), json
									.getString("Email"), Integer.parseInt(json
									.getString("Id"))));
						} catch (Exception e) {
							Log.e("flf", e.toString());
						}
					}
				}
			} else {

			}
			setAdapter();
		} else {
			Log.e("json", "errore nell operazione success:false");
		}
	}

}
