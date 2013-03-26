package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends Activity {

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
        setContentView(R.layout.activity_registration);
		initializeButtons();
		initializeListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration, menu);
        return true;
    }
    
    private void initializeButtons() {
    	btnBack = (Button) findViewById(R.id.registration_back_btn);
    	btnConfirm = (Button) findViewById(R.id.registration_confirm_btn);
    	btnArrowBack = (Button) findViewById(R.id.btnArrowBack);
    	btnArrowConfirm = (Button) findViewById(R.id.btnArrowConfirm);
    	editTextName = (EditText) findViewById(R.id.editTextregistrationName);
    	editTextSurname = (EditText) findViewById(R.id.editTextregistrationSurname);
    	editTextMail = (EditText) findViewById(R.id.editTextregistrationEmail);

    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	
    	name = prefs.getString("name", "");
    	surname = prefs.getString("surname", "");
    	mail = prefs.getString("mail", "");
    	
    	editTextName.setText(name);
    	editTextSurname.setText(surname);
    	editTextMail.setText(mail);
    	
    	nameOk = !(name.equals(""));
    	surnameOk = !(surname.equals(""));
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
					Intent registrationIntent = new Intent(v.getContext(), DataNascitaActivity.class);
					
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				    SharedPreferences.Editor editor = prefs.edit();

				    editor.putString("name", editTextName.getText().toString());
				    editor.putString("surname", editTextSurname.getText().toString());
				    editor.putString("mail", editTextMail.getText().toString());
					
					editor.commit();
					
			        startActivityForResult(registrationIntent, 0);	
				}
			}
		};
		
		OnClickListener backListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent loginIntent = new Intent(v.getContext(), LoginActivity.class);
		        startActivityForResult(loginIntent, 0);
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
				nameOk = !(name.equals(""));
				
				if(!nameOk) {
					editTextName.setBackgroundResource(R.drawable.txt_input_bordered_error);
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
				surnameOk = !(surname.equals(""));
				
				if(!surnameOk) {
					editTextSurname.setBackgroundResource(R.drawable.txt_input_bordered_error);
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
    
}
