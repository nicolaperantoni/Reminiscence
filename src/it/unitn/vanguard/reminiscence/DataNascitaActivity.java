package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DataNascitaActivity extends Activity {

	private Context context;
	
	private String day, month, year;
	
	// Date values
	private int dayValue = 1, monthValue = 0, yearValue = 1940;
	
	private int maxYear, maxDay, maxMonth;
	
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
	private Button btnBack, arrowBackBtn;
	private Button btnConfirm, arrowConfirmBtn;
	
	private String[] mesi = {"Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
	        "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_data_nascita);
		initializeButtons();
		initializeVars();		
		initializeListeners();
		
		//getting current day, month and year		
	    Calendar rightNow = Calendar.getInstance();
		maxYear = rightNow.get(Calendar.YEAR);
		maxMonth = rightNow.get(Calendar.MONTH);
		maxDay = rightNow.get(Calendar.DATE);
	}
	
	private void initializeVars() {

		Context context = getApplicationContext();
		day = FinalFunctionsUtilities.getSharedPreferences(Constants.DAY_KEY, context);
		month = FinalFunctionsUtilities.getSharedPreferences(Constants.MONTH_KEY, context);
		year = FinalFunctionsUtilities.getSharedPreferences(Constants.YEAR_KEY, context);
		
		//inizializza le variabili
		dayValue = Integer.parseInt(day);
		yearValue = Integer.parseInt(year);
		for(int i = 0; i < mesi.length; i++){
			if(mesi[i].equals(month)){
				monthValue = i;
			}
		}
		
		//setta i campi di testo
		txtDay.setText(day);
    	txtMonth.setText(month);
    	txtYear.setText(year);
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
        
		btnBack = (Button) findViewById(R.id.datanascita_back_btn);
		btnConfirm = (Button) findViewById(R.id.datanascita_confirm_btn);
		arrowConfirmBtn = (Button) findViewById(R.id.datanascita_arrow_confirm_btn);
		arrowBackBtn = (Button) findViewById(R.id.datanascita_arrow_back_btn);
	}

	private void initializeListeners() {
		
		// DAY UP-DOWN EVENTS
		btnDayUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dayValue--;
				dayValue = FinalFunctionsUtilities.valiDate(dayValue, monthValue, yearValue); 
				if(FinalFunctionsUtilities.isOverCurrentDate(dayValue, monthValue, yearValue, maxDay, maxMonth, maxYear)){
					currentDateMsg();
				}
				else { 	txtDay.setText(String.valueOf(dayValue)); }
			}
		});
		
		btnDayDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dayValue++;
				dayValue = FinalFunctionsUtilities.valiDate(dayValue, monthValue, yearValue);
				if(FinalFunctionsUtilities.isOverCurrentDate(dayValue, monthValue, yearValue, maxDay, maxMonth, maxYear)){
					currentDateMsg();
				}
				else { txtDay.setText(String.valueOf(dayValue));	}
			}
		});
		
		// MONTH UP-DOWN EVENTS
		btnMonthUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(monthValue == 0)	{ 
					monthValue = 11; 
				} 
				else { 	
					monthValue--; 
				}
				if(FinalFunctionsUtilities.isOverCurrentDate(dayValue, monthValue, yearValue, maxDay, maxMonth, maxYear)){
					currentDateMsg();
				}
				else {	
					dayValue = FinalFunctionsUtilities.valiDate(dayValue, monthValue, yearValue);
					txtMonth.setText(mesi[monthValue]);
					txtDay.setText(String.valueOf(dayValue));					
				}
			}
		});
		
		btnMonthDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				if(monthValue == 11) { 
					monthValue = 0; 
				}
				else	 { 
					monthValue++; 
				}
				if(FinalFunctionsUtilities.isOverCurrentDate(dayValue, monthValue, yearValue, maxDay, maxMonth, maxYear)){
					currentDateMsg();
				}
				else{	
					dayValue = FinalFunctionsUtilities.valiDate(dayValue, monthValue, yearValue);
					txtMonth.setText(mesi[monthValue]);
					txtDay.setText(String.valueOf(dayValue));					
				}
			}
		});	
		
		// YEAR UP-DOWN EVENTS
		btnYearUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				yearValue++;
				if(FinalFunctionsUtilities.isOverCurrentDate(dayValue, monthValue, yearValue, maxDay, maxMonth, maxYear)){
					yearValue--;
					currentDateMsg();
				}
				else{
					txtYear.setText(String.valueOf(yearValue));
				}
					dayValue = FinalFunctionsUtilities.valiDate(dayValue, monthValue, yearValue);
					txtDay.setText(String.valueOf(dayValue));
				
			}
		});
	
		btnYearDown.setOnClickListener(new OnClickListener() {
			@Override
				public void onClick(View v) {
					yearValue--;
					if(yearValue == maxYear-120) {
						txtYear.setText(String.valueOf(yearValue = maxYear-120));
					}
					else {
						txtYear.setText(String.valueOf(yearValue));
					}
					dayValue = FinalFunctionsUtilities.valiDate(dayValue, monthValue, yearValue);
					txtDay.setText(String.valueOf(dayValue));
				}
		});
		
		OnClickListener onclickback = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), RegistrationActivity.class);
				startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				finish();
			}
		};
		
		OnClickListener onclickconfirm = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent passwordIntent = new Intent(v.getContext(), PasswordActivity.class);
				
				// Get shared preferences
				Context context = getApplicationContext();
				
				FinalFunctionsUtilities.setSharedPreferences(Constants.DAY_KEY, txtDay.getText().toString(), context);
				FinalFunctionsUtilities.setSharedPreferences(Constants.MONTH_KEY, txtMonth.getText().toString(), context);
				FinalFunctionsUtilities.setSharedPreferences(Constants.YEAR_KEY, txtYear.getText().toString(), context);
				
		        startActivityForResult(passwordIntent, 0);
		        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				finish();
			}
		};
		
		//Button back
		btnBack.setOnClickListener(onclickback);
		arrowBackBtn.setOnClickListener(onclickback);
		
		// Button confirm
		btnConfirm.setOnClickListener(onclickconfirm);
		arrowConfirmBtn.setOnClickListener(onclickconfirm);
	}
	
	//control on current date (easter egg toast)
	void currentDateMsg(){
			Context context = getApplicationContext();
			CharSequence text = "Davvero? Sei nato nel futuro?";
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		String language = FinalFunctionsUtilities.getSharedPreferences("language", getApplicationContext());
		Locale locale = new Locale(language);

		if(locale.toString().equals(Locale.ITALIAN.getLanguage()) || locale.toString().equals(locale.ITALY.getLanguage())) {
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
		
		if(locale != null && FinalFunctionsUtilities.switchLanguage(locale, context)) {
		    // Refresh activity
		    finish();
		    startActivity(getIntent());
	    }
	    return true;
	}
}
