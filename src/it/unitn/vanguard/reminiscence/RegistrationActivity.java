package it.unitn.vanguard.reminiscence;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class RegistrationActivity extends Activity {

	private Button btnBack;
	private Button btnConfirm;
	
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
	}
	
	private void initializeListeners() {
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO call previous activity
				Intent registrationIntent = new Intent(v.getContext(), DataNascitaActivity.class);
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
