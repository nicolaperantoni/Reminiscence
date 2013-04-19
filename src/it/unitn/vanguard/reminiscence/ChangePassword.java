package it.unitn.vanguard.reminiscence;

import java.util.Locale;

import it.unitn.vanguard.reminiscence.asynctasks.ChangePasswordTask;
import it.unitn.vanguard.reminiscence.asynctasks.LoginTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Activity implements OnTaskFinished {
	
	protected ProgressDialog dialog;
	private Button btnChangePassword, btnChangePasswordBack;
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
		btnChangePasswordBack = (Button) findViewById(R.id.btnChangePasswordBack);
		txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
		txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
	}

	private void initializeListeners() {
		btnChangePassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String new_pass1 = txtNewPassword.getText().toString();
				String new_pass2 = txtConfirmPassword.getText().toString();
				boolean isEmptyNewPassword = new_pass1.trim().equals("");
				boolean isEmptyConfPassword = new_pass2.trim().equals("");
				
				if(isEmptyNewPassword || isEmptyConfPassword) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_password),
							Toast.LENGTH_LONG).show();
				}
				else {
					if(!new_pass1.equals(new_pass2)) {
						Toast.makeText(ChangePassword.this, getResources().getString(R.string.wrong_password),
								Toast.LENGTH_LONG).show();
					}
					else {

						dialog = new ProgressDialog(ChangePassword.this);
						dialog.setTitle(getResources().getString(R.string.please));
						dialog.setMessage(getResources().getString(R.string.wait));
						dialog.setCancelable(false);
						dialog.show();
						new ChangePasswordTask(ChangePassword.this).execute(new_pass1, new_pass2);
						finish();
					}
				}
			} 
		});
		
		btnChangePasswordBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onTaskFinished(JSONObject res) {

		if(dialog!=null && dialog.isShowing())
			dialog.dismiss();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		
		String language = FinalFunctionsUtilities.getSharedPreferences("language", getApplicationContext());
		Locale locale = new Locale(language);
		
		if(locale.toString().equals(Locale.ITALIAN.getLanguage()) || locale.toString().equals(locale.ITALY.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.it);
		}
		else if(locale.toString().equals(Locale.ENGLISH.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.en);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Locale locale = null;
		switch (item.getItemId()) {
		    case R.id.action_language_it: { locale = Locale.ITALY; break; }
		    case R.id.action_language_en: { locale = Locale.ENGLISH; break; }
	    }
		if(locale != null) {
			// Get Language
			FinalFunctionsUtilities.setSharedPreferences("language", locale.getLanguage(), getApplicationContext());
		    
			android.content.res.Configuration config = getApplicationContext().getResources().getConfiguration();
		    config.locale = locale;
		    getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
		    // Refresh activity
		    finish();
		    startActivity(getIntent());
	    }
	    return true;
	}
}
