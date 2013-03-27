package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.LoginTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnTaskFinished {

	private ImageView immagine;
	private Button btnLogin;
	private Button btnRegistration;
	private EditText usernameEditText;
	private EditText passwordEditText;
	protected ProgressDialog p;

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
		immagine = (ImageView) findViewById(R.id.imageView1);
		
	}

	private void initializeListeners() {
		
		immagine.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent regIntent = new Intent(v.getContext(), MainPageActivity.class);			
				 startActivityForResult(regIntent, 0);
			}
		});

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				p = new ProgressDialog(LoginActivity.this);
				p.setTitle(getResources().getString(R.string.please));
				p.setMessage(getResources().getString(R.string.wait));
				p.setCancelable(false);
				p.show();
				new LoginTask(LoginActivity.this).execute(username, password);
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
		if(p!=null && p.isShowing())
			p.dismiss();
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
