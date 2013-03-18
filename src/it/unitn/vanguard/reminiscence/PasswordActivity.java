package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.RegistrationTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	private String eMail;
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
		dispatchInfoFromIntent(getIntent());
		initializeButtons();
		generateSuggestion();
		initializeListeners();
	}
	
	private void dispatchInfoFromIntent(Intent i) {
		/*
		name=i.getExtras().getString("name");
		surname=i.getExtras().getString("surname");
		eMail=i.getExtras().getString("email");
		day=i.getExtras().getString("day");
		month=i.getExtras().getString("month");
		year=i.getExtras().getString("year");
		*/
		
		suggestionList = new ArrayList<String>(2);
		name = "Mionome";
		surname = "Miocognome";
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
				// TODO call previous activity
			}
		});
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new RegistrationTask(PasswordActivity.this).execute(name, surname, eMail, 
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
	public void onTaskFinished(boolean res) {
		//per ottenersi la stringa giusta in base al risultato
		String resultText = getResources().getString(((res)?R.string.registration_succes:R.string.registration_failed));
		Toast.makeText(this, resultText, Toast.LENGTH_SHORT).show();
	}
}
