package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.ChangePasswordTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
				String username = txtNewPassword.getText().toString();
				String password = txtConfirmPassword.getText().toString();
				p = new ProgressDialog(ChangePassword.this);
				p.setTitle(getResources().getString(R.string.please));
				p.setMessage(getResources().getString(R.string.wait));
				p.setCancelable(false);
				p.show();
				new ChangePasswordTask(ChangePassword.this).execute(username, password);
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
		// TODO Auto-generated method stub
		
	}

}
