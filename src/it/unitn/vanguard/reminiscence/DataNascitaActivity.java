package it.unitn.vanguard.reminiscence;

import java.util.Calendar;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DataNascitaActivity extends Activity {

	// Date values
	private int day = 1, month = 0, year = 1940;
	
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
		
		//getting current day, month and year		
	    Calendar rightNow = Calendar.getInstance();
		maxYear = rightNow.get(Calendar.YEAR);
		maxMonth = rightNow.get(Calendar.MONTH);
		maxDay = rightNow.get(Calendar.DATE);
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
				day++;
				day = FinalFunctionsUtilities.valiDate(day, month, year); 
				if(FinalFunctionsUtilities.isOverCurrentDate(day, month, year, maxDay, maxMonth, maxYear)){
					currentDateMsg(day,month, year);
					day--;
				}
				else					
					txtDay.setText(String.valueOf(day));	
			}
		});
		
		btnDayDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				day--;
				day = FinalFunctionsUtilities.valiDate(day, month, year);
				if(FinalFunctionsUtilities.isOverCurrentDate(day, month, year, maxDay, maxMonth, maxYear)){
					currentDateMsg(day,month, year);
					day++;
				}
				else					
					txtDay.setText(String.valueOf(day));	
			}
		});
		
		// MONTH UP-DOWN EVENTS
		btnMonthUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(month == 11)	
					month = 0;					
				else
					month++;				
				if(FinalFunctionsUtilities.isOverCurrentDate(day, month, year, maxDay, maxMonth, maxYear)){
					currentDateMsg(day,month, year);
					month--;
				}				
				else{	
					day = FinalFunctionsUtilities.valiDate(day, month, year);
					txtMonth.setText(mesi[month]);
					txtDay.setText(String.valueOf(day));					
				}
			}
		});
		
		btnMonthDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				if(month == 0)
					month = 11;
				else					
					month--;
				if(FinalFunctionsUtilities.isOverCurrentDate(day, month, year, maxDay, maxMonth, maxYear)){
					currentDateMsg(day,month, year);
					month++;
				}
				else{	
					day = FinalFunctionsUtilities.valiDate(day, month, year);
					txtMonth.setText(mesi[month]);
					txtDay.setText(String.valueOf(day));					
				}
			}
		});	
		
		// YEAR UP-DOWN EVENTS
		btnYearUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				year++;
				if(FinalFunctionsUtilities.isOverCurrentDate(day, month, year, maxDay, maxMonth, maxYear)){
					currentDateMsg(day, month, year);
					year--;
				}
				else{
					txtYear.setText(String.valueOf(year));
					day = FinalFunctionsUtilities.valiDate(day, month, year);
					txtDay.setText(String.valueOf(day));
				}
			}
		});
	
		btnYearDown.setOnClickListener(new OnClickListener() {
			@Override
				public void onClick(View v) {
					if(year == maxYear-120)
						txtYear.setText(String.valueOf(year = maxYear-120));
					else
						txtYear.setText(String.valueOf(--year));
					day = FinalFunctionsUtilities.valiDate(day, month, year);
					txtDay.setText(String.valueOf(day));
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
	
	//control on current date (easter egg toast)
		void currentDateMsg(int day, int month, int year){
				Context context = getApplicationContext();
				CharSequence text = "Davvero? Sei nato nel futuro?";
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_data_nascita, menu);
		return true;
	}
}
