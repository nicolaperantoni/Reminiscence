package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.ChangePasswordTask;
import it.unitn.vanguard.reminiscence.asynctasks.RegistrationTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Activity implements OnTaskFinished {
	
	private Context context;
	protected ProgressDialog dialog;
	private Button btnChangePassword, btnChangePasswordBack;
	private EditText txtNewPassword, txtConfirmPassword;
	private String new_pass1;
	
	// Controllo sintassi password e conferma
	private boolean passwordOk, confirmOk;
	private String password, confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ChangePassword.this;
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
			
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
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
				
				new_pass1 = txtNewPassword.getText().toString();
				String new_pass2 = txtConfirmPassword.getText().toString();
				boolean isEmptyNewPassword = new_pass1.trim().equals("");
				boolean isEmptyConfPassword = new_pass2.trim().equals("");
				
				if(isEmptyNewPassword || isEmptyConfPassword) {
					Toast.makeText(context, getResources().getString(R.string.empty_password),
							Toast.LENGTH_LONG).show();
				}
				else {
					if(!new_pass1.equals(new_pass2)) {
						Toast.makeText(ChangePassword.this, getResources().getString(R.string.wrong_password),
								Toast.LENGTH_LONG).show();
					}
					else if(!FinalFunctionsUtilities.isDeviceConnected(context)) {
						Toast.makeText(context, getResources().getString(R.string.connection_fail), Toast.LENGTH_LONG).show();
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
		
		txtNewPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				
				password = txtNewPassword.getText().toString();
				passwordOk = !password.equals("");
				
				if(!passwordOk) {
					Toast.makeText(context, getResources().getText(R.string.registration_password_empty), Toast.LENGTH_SHORT).show();
				}
				else if (!(passwordOk = passwordOk && !password.contains(" "))) {
					Toast.makeText(context, getResources().getText(R.string.registration_password_contains_spaces), Toast.LENGTH_SHORT).show();
				}
				
				if(passwordOk) { txtNewPassword.setBackgroundResource(R.drawable.txt_input_bordered); }
				else { 	txtNewPassword.setBackgroundResource(R.drawable.txt_input_bordered_error); }
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
		});
		
		
		txtConfirmPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				
				confirm = txtConfirmPassword.getText().toString();
				confirmOk = !confirm.equals("");
				
				if(!confirmOk) {
					Toast.makeText(context, getResources().getText(R.string.registration_password_empty), Toast.LENGTH_SHORT).show();
				}
				else if (!(confirmOk = confirmOk && !confirm.contains(" "))) {
					Toast.makeText(context, getResources().getText(R.string.registration_password_contains_spaces), Toast.LENGTH_SHORT).show();
				}
				
				if(confirmOk) { txtConfirmPassword.setBackgroundResource(R.drawable.txt_input_bordered); }
				else { 	txtConfirmPassword.setBackgroundResource(R.drawable.txt_input_bordered_error); }
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
		});
	}

	@Override
	public void onTaskFinished(JSONObject res) {

		if(dialog!=null && dialog.isShowing())
			dialog.dismiss();
		try {
			Log.e("", FinalFunctionsUtilities.getSharedPreferences(Constants.PASSWORD_KEY, context));
			if (res.getString("success").equals("true")) {
				FinalFunctionsUtilities.setSharedPreferences(Constants.PASSWORD_KEY, new_pass1, context);
				Toast.makeText(context, getResources().getString(R.string.correct_password),
						Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(context, getResources().getString(R.string.login_failed),
						Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Log.e(ChangePassword.class.getName(), e.toString());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		Locale locale = new Locale(language);

		if(locale.toString().equals(Locale.ITALIAN.getLanguage()) || locale.toString().equals(locale.ITALY.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.it);
		}
		else if(locale.toString().equals(Locale.ENGLISH.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.en);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Locale locale = null;
		switch (item.getItemId()) {
		    case R.id.action_language_it: { locale = Locale.ITALY; break; }
		    case R.id.action_language_en: { locale = Locale.ENGLISH; break; }
		    case android.R.id.home: this.finish();break;
	    }
		
		if(locale != null && FinalFunctionsUtilities.switchLanguage(locale, context)) {
		    // Refresh activity
		    finish();
		    startActivity(getIntent());
	    }
	    return true;
	}
}
