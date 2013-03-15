package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.LoginTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity implements OnTaskFinished {

	private Button btnLogin;
	private Button btnRegistration;
	
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
	}
	
	private void initializeListeners() {
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO call previous activity
				Intent loginIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
		        startActivityForResult(loginIntent, 0);
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
	public void onTaskFinished(boolean res) {
		// TODO Auto-generated method stub
		
	}
}
