package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.LoginTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnTaskFinished {

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
				
				// TODO call previous activity
				//Intent loginIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
		        //startActivityForResult(loginIntent, 0);
				new LoginTask(LoginActivity.this).execute(username, password);
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
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		// TODO Auto-generated method stub
		//String resultText = getResources().getString(((res)?R.string.login_succes:R.string.login_failed));
		try {
			Toast.makeText(this, res.getString("success"), Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
