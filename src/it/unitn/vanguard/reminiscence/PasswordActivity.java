package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.RegistrationTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity implements OnTaskFinished {

	private Context context;

	private EditText editTextPassword;
	private Button btnBack, btnFrecciaBack;
	private Button btnConfirm, btnFrecciaConferma;
	private Button btnReloadPasswd;
	protected ProgressDialog dialog;

	private String name;
	private String surname;
	private String mail;
	private String day;
	private String month;
	private String year;
	private String password;

	// Password suggestion
	private ArrayList<String> suggestionList;
	private int rand;
	private boolean passwordOk = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_registration_password);
		initializeButtons();
		initializeVars();
		generateSuggestion();
		initializeListeners();
	}

	private void initializeVars() {

		Context context = getApplicationContext();
		name = FinalFunctionsUtilities.getSharedPreferences(Constants.NAME_KEY, context);

		surname = FinalFunctionsUtilities.getSharedPreferences(Constants.SURNAME_KEY, context);
		mail = FinalFunctionsUtilities.getSharedPreferences(Constants.MAIL_KEY, context);

		day = FinalFunctionsUtilities.getSharedPreferences(Constants.DAY_KEY, context);
		month = FinalFunctionsUtilities.getSharedPreferences(Constants.MONTH_KEY, context);
		year = FinalFunctionsUtilities.getSharedPreferences(Constants.YEAR_KEY, context);

		suggestionList = new ArrayList<String>(2);
		suggestionList.add(name);
		suggestionList.add(surname);
	}

	private void initializeButtons() {
		editTextPassword = (EditText) findViewById(R.id.registration_password_ET);
		btnBack = (Button) findViewById(R.id.button_back);
		btnConfirm = (Button) findViewById(R.id.button_conferma);
		btnFrecciaConferma = (Button) findViewById(R.id.button_freccia_conferma);
		btnFrecciaBack = (Button) findViewById(R.id.button_freccia_back);
		btnReloadPasswd = (Button) findViewById(R.id.btnReloadPassword);
	}

	OnClickListener onclickback = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(),
					DataNascitaActivity.class);
			startActivityForResult(intent, 0);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			finish();
		}
	};

	OnClickListener onclickconf = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			password = editTextPassword.getText().toString();
			passwordOk = !password.equals("");

			if (!passwordOk) {
				Toast.makeText(
						getApplicationContext(),
						getResources().getText(
								R.string.registration_password_empty),
						Toast.LENGTH_SHORT).show();
			} else if (!(passwordOk = passwordOk && !password.contains(" "))) {
				Toast.makeText(
						getApplicationContext(),
						getResources().getText(
								R.string.registration_password_contains_spaces),
						Toast.LENGTH_SHORT).show();
			} else if (FinalFunctionsUtilities
					.isDeviceConnected(getApplicationContext())) {
				dialog = new ProgressDialog(PasswordActivity.this);
				dialog.setTitle(getResources().getString(R.string.please));
				dialog.setMessage(getResources().getString(R.string.wait));
				dialog.setCancelable(false);
				dialog.show();
				new RegistrationTask(PasswordActivity.this).execute(name,
						surname, mail, password, day, month, year);
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.connection_fail),
						Toast.LENGTH_LONG).show();
			}
		}
	};

	private void initializeListeners() {

		btnBack.setOnClickListener(onclickback);
		btnFrecciaBack.setOnClickListener(onclickback);
		btnConfirm.setOnClickListener(onclickconf);
		btnFrecciaConferma.setOnClickListener(onclickconf);

		btnReloadPasswd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				generateSuggestion();
				Animation anim = AnimationUtils.loadAnimation(
						PasswordActivity.this, R.anim.rotate_arrow_360);
				v.startAnimation(anim);
			}
		});

		editTextPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {

				password = editTextPassword.getText().toString();
				passwordOk = !password.equals("");

				if (!passwordOk) {
					Toast.makeText(
							getApplicationContext(),
							getResources().getText(
									R.string.registration_password_empty),
							Toast.LENGTH_SHORT).show();
				} else if (!(passwordOk = passwordOk && !password.contains(" "))) {
					Toast.makeText(
							getApplicationContext(),
							getResources()
									.getText(
											R.string.registration_password_contains_spaces),
							Toast.LENGTH_SHORT).show();
				}

				if (passwordOk) {
					editTextPassword
							.setBackgroundResource(R.drawable.txt_input_bordered);
				} else {
					editTextPassword
							.setBackgroundResource(R.drawable.txt_input_bordered_error);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}

	private void generateSuggestion() {
		password = "";
		rand = (int) (Math.random() * suggestionList.size());
		password += suggestionList.get(rand);
		password += Constants.PASSWORD_MIN
				+ (int) (Math.random() * ((Constants.PASSWORD_MAX - Constants.PASSWORD_MIN) + 1));
		editTextPassword.setText(password);
	}

	@Override
	public void onTaskFinished(JSONObject res) {

		String ret = null;
		try {

			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			ret = res.getString("success"); // +res.getString("err1")+res.getString("err2")+res.getString("err3")+res.getString("err4")+res.getString("err5");
			if (ret.startsWith("true")) {
				
				FinalFunctionsUtilities.setSharedPreferences(Constants.TOKEN_KEY, res.getString("token"), getApplicationContext());
				Toast.makeText(this, getResources().getText(R.string.registration_succes), Toast.LENGTH_SHORT).show();
				FinalFunctionsUtilities.setSharedPreferences(Constants.PASSWORD_KEY, password, getApplicationContext());
				//Log.e("token", FinalFunctionsUtilities.getSharedPreferences("token", getApplicationContext()));
				// intent
				Intent intent = new Intent(getApplicationContext(), LuogoNascitaActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 0);
				finish();
			} else {
				Toast.makeText(this,
						getResources().getText(R.string.registration_failed),
						Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", getApplicationContext());
		Locale locale = new Locale(language);

		if (locale.toString().equals(Locale.ITALIAN.getLanguage())
				|| locale.toString().equals(locale.ITALY.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.it);
		} else if (locale.toString().equals(Locale.ENGLISH.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.en);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Locale locale = null;
		switch (item.getItemId()) {
		case R.id.action_language_it: {
			locale = Locale.ITALY;
			break;
		}
		case R.id.action_language_en: {
			locale = Locale.ENGLISH;
			break;
		}
		}

		if (locale != null
				&& FinalFunctionsUtilities.switchLanguage(locale, context)) {
			// Refresh activity
			finish();
			startActivity(getIntent());
		}
		return true;
	}
}
