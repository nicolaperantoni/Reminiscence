package it.unitn.vanguard.reminiscence;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DataNascitaActivity extends Activity {

	// Date values
	private int day = 1, month = 1, year = 1950;
	
	// Day views
	private Button btnDayUp, btnDayDown;
	private TextView txtDay;
	
	// Month Views
	private Button btnMonthUp, btnMonthDown;
	private TextView txtMonth;
	
	// Year Views
	private Button btnYearUp, btnYearDown;
    private TextView  txtYear;
	
    // Back - Confirm Buttons
	private Button btnBack;
	private Button btnConfirm;
	private String[] mesi = {"Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
	        "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_nascita);
		initializeButtons();
		initializeListeners();
	}
	
	private void initializeButtons() {
		
		//add day buttons
        btnDayUp = (Button) findViewById(R.id.btn_day_up);
        btnDayDown = (Button) findViewById(R.id.btn_day_down);
        txtDay = (TextView) findViewById(R.id.txt_day);
		
		//add month buttons
		btnMonthUp = (Button) findViewById(R.id.btn_month_up);
		btnMonthDown = (Button) findViewById(R.id.btn_month_down);
		txtMonth = (TextView) findViewById(R.id.txt_month);
		
        //add year buttons
        btnYearUp = (Button) findViewById(R.id.btn_year_up);
        btnYearDown = (Button) findViewById(R.id.btn_year_down);
        txtYear = (TextView) findViewById(R.id.txt_year);
        
		btnBack = (Button) findViewById(R.id.registration_back_btn);
		btnConfirm = (Button) findViewById(R.id.registration_confirm_btn);
	}

	private void initializeListeners() {
		
		// DAY UP-DOWN EVENTS
		btnDayUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtDay.setText("" + (++day) % 32);
			}
		});
		
		btnDayDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtDay.setText("" + (--day) % 32);
			}
		});
		
		// MONTH UP-DOWN EVENTS
		btnMonthUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtMonth.setText(mesi[(++month) % ( mesi.length + 1)]);
			}
		});
		
		btnMonthDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtMonth.setText(mesi[ (--month) % (mesi.length + 1)]);
			}
		});
		
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent regIntent = new Intent(v.getContext(), RegistrationActivity.class);
		        startActivityForResult(regIntent, 0);
			}
		});
		
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent passwordIntent = new Intent(v.getContext(), PasswordActivity.class);
				
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			    SharedPreferences.Editor editor = prefs.edit();

			    editor.putString("day", txtDay.getText().toString());
			    editor.putString("month", txtMonth.getText().toString());
			    editor.putString("year", txtYear.getText().toString());
				
				editor.commit();
				
		        startActivityForResult(passwordIntent, 0);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_data_nascita, menu);
		return true;
	}

}
