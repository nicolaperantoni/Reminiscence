package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.LoginTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnTaskFinished {

	private Button btnLogin;
	private Button btnRegistration;
	private EditText usernameEditText;
	private EditText passwordEditText;

	protected ProgressDialog p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		// Se l'utente aveva già affettuato il login in precedenza salta dirett nella timeline
		if(FinalFunctionsUtilities.isLoggedIn(getApplicationContext())) { 
			Intent changePasswd = new Intent(getApplicationContext(),
					ViewStoriesFragmentActivity.class);
			startActivityForResult(changePasswd, 0);
			finish();
		}*/
		setContentView(R.layout.activity_login);
		initializeButtons();
		initializeListeners();
	}

	private void initializeButtons() {
		
		TextView logo = (TextView)findViewById(R.id.Logo);
		Typeface typeFace = Typeface.createFromAsset(getAssets(),"Pacifico.ttf");
		logo.setTypeface(typeFace);
		
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegistration = (Button) findViewById(R.id.btnRegistration);
		usernameEditText = (EditText) findViewById(R.id.edittextUsername);
		passwordEditText = (EditText) findViewById(R.id.edittextPassword);
	}

	private void initializeListeners() {

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				boolean isEmptyUsername = username.trim().equals("");
				boolean isEmptyPassword = password.trim().equals("");
				
				if(isEmptyUsername && isEmptyPassword) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_empty_both),
							Toast.LENGTH_LONG).show();
				}
				else if(isEmptyUsername) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_empty_username),
							Toast.LENGTH_LONG).show();
				}
				else if(isEmptyPassword) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_empty_password),
							Toast.LENGTH_LONG).show();
				}
				else if(FinalFunctionsUtilities.isDeviceConnected(getApplicationContext())) {
					p = new ProgressDialog(LoginActivity.this);
					p.setTitle(getResources().getString(R.string.please));
					p.setMessage(getResources().getString(R.string.wait));
					p.setCancelable(false);
					p.show();
					new LoginTask(LoginActivity.this).execute(username, password);
				}
				else {
					if(p!=null && p.isShowing()) { 	p.dismiss(); }
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_fail),
							Toast.LENGTH_LONG).show();
				}
			}
		});
		btnRegistration.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent regIntent = new Intent(getApplicationContext(),
						RegistrationActivity.class);
				startActivityForResult(regIntent, 0);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_left);
			}
		});
		
		usernameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					usernameEditText
							.setBackgroundResource(R.drawable.txt_input_bordered_ok);
				} else {
					usernameEditText
							.setBackgroundResource(R.drawable.txt_input_bordered);
				}
			}
		});

		passwordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					passwordEditText
							.setBackgroundResource(R.drawable.txt_input_bordered_ok);
				} else {
					passwordEditText
							.setBackgroundResource(R.drawable.txt_input_bordered);
				}
			}
		});
	} 

	@Override
	public void onTaskFinished(JSONObject res) {
		if(p!=null && p.isShowing()) { 	p.dismiss(); }
		try {
			if (res.getString("success").equals("true"))
				startActivity(new Intent(LoginActivity.this,
						ViewStoriesFragmentActivity.class));
			else
				Toast.makeText(this, getResources().getString(R.string.login_failed),
						Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Log.e(LoginActivity.class.getName(), e.toString());
		}
	}
}
