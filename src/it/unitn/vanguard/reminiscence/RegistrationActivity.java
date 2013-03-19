package it.unitn.vanguard.reminiscence;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends Activity {

	private Button btnBack;
	private Button btnConfirm;
	private EditText editTextName;
	private EditText editTextSurname;
	private EditText editTextMail;
	
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
    	editTextName = (EditText) findViewById(R.id.editTextregistrationName);
    	editTextSurname = (EditText) findViewById(R.id.editTextregistrationSurname);
    	editTextMail = (EditText) findViewById(R.id.editTextregistrationMail);
	}
	
	private void initializeListeners() {
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String name = editTextName.getText().toString();
				String surname = editTextSurname.getText().toString();
				String mail = editTextMail.getText().toString();
				
				Intent registrationIntent = new Intent(v.getContext(), DataNascitaActivity.class);
				registrationIntent.putExtra("name",name);
				registrationIntent.putExtra("surname",surname);
				registrationIntent.putExtra("email",mail);
		        startActivityForResult(registrationIntent, 0);
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent loginIntent = new Intent(v.getContext(), LoginActivity.class);
		        startActivityForResult(loginIntent, 0);
			}
		});
	}
    
}
