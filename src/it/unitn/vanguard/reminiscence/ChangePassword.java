package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.ChangePasswordTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Activity implements OnTaskFinished {
	
	protected ProgressDialog p;
	private Button btnChangePassword;
	private EditText txtNewPassword, txtConfirmPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		initializeButtons();
		initializeListeners();
	}
	
	private void initializeButtons() {
		btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
		txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
		txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
	}

	private void initializeListeners() {
		btnChangePassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String new_pass1 = txtNewPassword.getText().toString();
				String new_pass2 = txtConfirmPassword.getText().toString();
				
				if(new_pass1.trim().equals("") || new_pass2.trim().equals("")) {
					Toast.makeText(ChangePassword.this, getResources().getString(R.string.empty_password),
							Toast.LENGTH_LONG).show();
				}
				else {
					if(!new_pass1.equals(new_pass2)) {
						Toast.makeText(ChangePassword.this, getResources().getString(R.string.wrong_password),
								Toast.LENGTH_LONG).show();
					}
					else {

						p = new ProgressDialog(ChangePassword.this);
						p.setTitle(getResources().getString(R.string.please));
						p.setMessage(getResources().getString(R.string.wait));
						p.setCancelable(false);
						p.show();
						new ChangePasswordTask(ChangePassword.this).execute(new_pass1, new_pass2);
					}
				}
			} 
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_password, menu);
		return true;
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		
		//ottiene il token se presente
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String old = prefs.getString("password", "");
		Log.e("passwooooooooooooord vecchiaaaaaaaaaaaaa", old);
		
		Log.e("jasonnnnnnnnnnnnnnnnnnnnnnnnnn" + LoginActivity.class.getName(), res.toString());
		
		if(p!=null && p.isShowing())
			p.dismiss();
		try {
			if (res.getString("success").equals("true")) {
				Toast.makeText(ChangePassword.this, getResources().getString(R.string.correct_password),
						Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(this, getResources().getString(R.string.login_failed),
						Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Log.e(LoginActivity.class.getName(), e.toString());
		}
	}
}
