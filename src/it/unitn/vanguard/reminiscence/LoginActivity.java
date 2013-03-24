package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.LoginTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnTaskFinished, OnFocusChangeListener {

	private Button btnLogin;
	private Button btnRegistration;
	private EditText usernameEditText;
	private EditText passwordEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initializeButtons();
		initializeListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	private void initializeButtons() {
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
				new LoginTask(LoginActivity.this).execute(username, password);
				//startActivity(new Intent(LoginActivity.this,ViewStoriesFragmentActivity.class));
			}
		});
		btnRegistration.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent regIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
		        startActivityForResult(regIntent, 0); 
		        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			}
		});
		
		usernameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					usernameEditText.setBackgroundResource(R.drawable.txt_input_bordered_ok);
				} else {
					usernameEditText.setBackgroundResource(R.drawable.txt_input_bordered);
				}
			}
		});
			
		passwordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					passwordEditText.setBackgroundResource(R.drawable.txt_input_bordered_ok);
				} else {
					passwordEditText.setBackgroundResource(R.drawable.txt_input_bordered);
				}
			}
		});
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		//String resultText = getResources().getString(((res)?R.string.login_succes:R.string.login_failed));
		try {
			Toast.makeText(this, res.getString("success"), Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}
}
