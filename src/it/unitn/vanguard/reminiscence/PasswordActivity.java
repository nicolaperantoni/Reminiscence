package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.RegistrationTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity implements OnTaskFinished {

	private EditText editTextPassword;
	private Button btnBack;
	private Button btnConfirm;
	private Button btnReloadPasswd;
	
	private String name;
	private String surname;
	private String mail;
	private String day;
	private String month;
	private String year;
	
	// Password suggestion
	private ArrayList<String> suggestionList;
	private int rand;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_password);
		initializeButtons();
		initializeVars();
		generateSuggestion();
		initializeListeners();
	}

	private void initializeVars() {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		name = prefs.getString("name", "");
		surname = prefs.getString("surname", "");
		mail = prefs.getString("mail", "");
		day = prefs.getString("day", "");
		month = prefs.getString("month", "");
		year = prefs.getString("year", "");
		
		//Toast.makeText(this, mail, Toast.LENGTH_SHORT).show();
		
		suggestionList = new ArrayList<String>(2);
		suggestionList.add(name); 
		suggestionList.add(surname);
		
	}

	private void initializeButtons() {
		editTextPassword = (EditText) findViewById(R.id.registration_password_ET);
		btnBack = (Button) findViewById(R.id.registration_back_btn);
		btnConfirm = (Button) findViewById(R.id.registration_confirm_btn);
		btnReloadPasswd = (Button) findViewById(R.id.btnReloadPassword);
	}

	private void initializeListeners() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent regIntent = new Intent(v.getContext(), DataNascitaActivity.class);
		        startActivityForResult(regIntent, 0);
			}
		});
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new RegistrationTask(PasswordActivity.this).execute(name, surname, mail, 
					editTextPassword.getText().toString(), day, month, year);
			}
		});
		btnReloadPasswd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				generateSuggestion();
				//Toast.makeText(getApplicationContext(), btnReloadPasswd.toString(), Toast.LENGTH_SHORT).show();
				Animation anim = AnimationUtils.loadAnimation(PasswordActivity.this, R.anim.rotate_arrow_360);
				v.startAnimation(anim);
			}
		});
	}

	private void generateSuggestion() {
		String suggestion = "";
		rand = (int)(Math.random() * suggestionList.size());
		suggestion += suggestionList.get(rand);
		suggestion += Constants.PASSWORD_MIN + (int)(Math.random() * ((Constants.PASSWORD_MAX - Constants.PASSWORD_MIN) + 1));
		editTextPassword.setText(suggestion);
	}
	
	@Override
	public void onTaskFinished(JSONObject res) {
		//per ottenersi la stringa giusta in base al risultato
		//String resultText = getResources().getString(((res)?R.string.registration_succes:R.string.registration_failed));
		String ret = null;
		/*try { 
			ret = res.getString("success")+res.getString("err1")+res.getString("err2")+res.getString("err3")+res.getString("err4")+res.getString("err5");
			Toast.makeText(this, ret, Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//se la registrazione ha successo torna alla pagina di login
		if (ret.startsWith("true")) {
			Intent loginIntent = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivityForResult(loginIntent, 0);
		} else
			Toast.makeText(this,
					getResources().getText(R.string.registration_failed),
					Toast.LENGTH_SHORT).show();
	}
}
