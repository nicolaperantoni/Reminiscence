package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	private Context context;
	
	private Button btnBack;
	private Button btnConfirm;
	private Button btnArrowBack;
	private Button btnArrowConfirm;
	private EditText editTextName;
	private EditText editTextSurname;
	private EditText editTextMail;
	private String name, surname, mail;
	private boolean nameOk = false, surnameOk = false, mailOk = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = RegistrationActivity.this;
        
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
        setContentView(R.layout.activity_registration);
        
		initializeButtons();
		initializeListeners();
    }
    
    private void initializeButtons() {
    	btnBack = (Button) findViewById(R.id.registration_back_btn);
    	btnConfirm = (Button) findViewById(R.id.registration_confirm_btn);
    	btnArrowBack = (Button) findViewById(R.id.btnArrowBack);
    	btnArrowConfirm = (Button) findViewById(R.id.btnArrowConfirm);
    	editTextName = (EditText) findViewById(R.id.editTextregistrationName);
    	editTextSurname = (EditText) findViewById(R.id.editTextregistrationSurname);
    	editTextMail = (EditText) findViewById(R.id.editTextregistrationEmail);

    	name = FinalFunctionsUtilities.getSharedPreferences(Constants.NAME_KEY, context);
    	surname = FinalFunctionsUtilities.getSharedPreferences(Constants.SURNAME_KEY, context);
    	mail = FinalFunctionsUtilities.getSharedPreferences(Constants.MAIL_KEY, context);
    	
    	editTextName.setText(name);
    	editTextSurname.setText(surname);
    	editTextMail.setText(mail);
    	
    	nameOk = !(name.trim().equals(""));
    	surnameOk = !(surname.trim().equals(""));
    	mailOk = FinalFunctionsUtilities.isValidEmailAddress(mail);
	}
	
	private void initializeListeners() {
		OnClickListener confirmListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(!nameOk) {
					editTextName.setBackgroundResource(R.drawable.txt_input_bordered_error);
				} else {
					editTextName.setBackgroundResource(R.drawable.txt_input_bordered);
				}
				
				if(!surnameOk) {
					editTextSurname.setBackgroundResource(R.drawable.txt_input_bordered_error);
				} else {
					editTextSurname.setBackgroundResource(R.drawable.txt_input_bordered);
				}
				
				if(!mailOk) {
					editTextMail.setBackgroundResource(R.drawable.txt_input_bordered_error);
				} else {
					editTextMail.setBackgroundResource(R.drawable.txt_input_bordered);
				}
				
				if( nameOk && surnameOk && mailOk ) {
					Intent registrationIntent = new Intent(context, DataNascitaActivity.class);
					
					FinalFunctionsUtilities.setSharedPreferences(Constants.NAME_KEY, name, context);
					FinalFunctionsUtilities.setSharedPreferences(Constants.SURNAME_KEY, surname, context);
					FinalFunctionsUtilities.setSharedPreferences(Constants.MAIL_KEY, mail, context);

			        startActivityForResult(registrationIntent, 0);
			        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			        finish();
				}
			}
		};
		
		OnClickListener backListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, LoginActivity.class);
				startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				finish();
			}
		};
		
		btnConfirm.setOnClickListener(confirmListener);
		btnArrowConfirm.setOnClickListener(confirmListener);
		btnBack.setOnClickListener(backListener);
		btnArrowBack.setOnClickListener(backListener);
		
		editTextName.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				
				name = editTextName.getText().toString();
				if(name.startsWith(" ") && name.length()>1)
					name=name.substring(1,name.length()-1);
				if(name.endsWith(" ")&& name.length()>1)
					name=name.substring(0,name.length()-2);
				
				nameOk = !name.trim().equals("");
				if(!nameOk) {
					editTextName.setBackgroundResource(R.drawable.txt_input_bordered_error);
					Toast.makeText(context, getResources().getText(R.string.registration_name_empty), Toast.LENGTH_SHORT).show();
				}
				else if(!name.matches("[a-zA-Z]+")){
					
					editTextName.setBackgroundResource(R.drawable.txt_input_bordered_error);
					Toast.makeText(context, getResources().getText(R.string.registration_name_invalid), Toast.LENGTH_SHORT).show();
				}
				else {
					editTextName.setBackgroundResource(R.drawable.txt_input_bordered);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
		});
		
		editTextSurname.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				
				surname = editTextSurname.getText().toString();

				if(surname.startsWith(" ")&& surname.length()>1)
					surname=surname.substring(1,surname.length()-1);
				if(surname.endsWith(" ")&& surname.length()>1)
					surname=surname.substring(0,surname.length()-2);
				surnameOk = !surname.trim().equals("");
				if(!surnameOk) {
					editTextSurname.setBackgroundResource(R.drawable.txt_input_bordered_error);
					Toast.makeText(context, getResources().getText(R.string.registration_surname_empty), Toast.LENGTH_SHORT).show();
				}
				else if (!surname.matches("[a-zA-Z]+")) {
					editTextSurname.setBackgroundResource(R.drawable.txt_input_bordered_error);
					Toast.makeText(context, getResources().getText(R.string.registration_surname_invalid), Toast.LENGTH_SHORT).show();
				}
				else {
					editTextSurname.setBackgroundResource(R.drawable.txt_input_bordered);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
		});
		
		editTextMail.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				
				mail = editTextMail.getText().toString();
				mailOk = FinalFunctionsUtilities.isValidEmailAddress(mail);
				
				if(!mailOk) {
					editTextMail.setBackgroundResource(R.drawable.txt_input_bordered_error);
				} else {
					editTextMail.setBackgroundResource(R.drawable.txt_input_bordered);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu: this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		String language = FinalFunctionsUtilities
				.getSharedPreferences(Constants.LANGUAGE_KEY, context);
		Locale locale = new Locale(language);

		if(locale.toString().equals(Locale.ITALIAN.getLanguage()) || locale.toString().equals(Locale.ITALY.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.it);
		}
		else if(locale.toString().equals(Locale.ENGLISH.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.en);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Locale locale = null;
		switch (item.getItemId()) {
		    case R.id.action_language_it: { locale = Locale.ITALY; break; }
		    case R.id.action_language_en: { locale = Locale.ENGLISH; break; }
	    }
		
		// Refresh activity
		if(locale != null && FinalFunctionsUtilities.switchLanguage(locale, context)) {
		    finish();
		    startActivity(getIntent());
	    }
	    return true;
	}
}
