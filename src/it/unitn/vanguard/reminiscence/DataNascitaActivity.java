package it.unitn.vanguard.reminiscence;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DataNascitaActivity extends Activity implements OnClickListener{

	private Button btnMonthUp, btnMonthDown;
	private Button mBackButton;
	private Button mConfirmButton;
	private TextView txtMonth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_nascita);
		
		initializeButtons();
		initializeListeners();
		
		txtMonth.setText("Novembre");
	}
	
	private void initializeButtons() {
		btnMonthUp = (Button) findViewById(R.id.btn_month_up);
		btnMonthDown = (Button) findViewById(R.id.btn_month_down);
		txtMonth = (TextView) findViewById(R.id.txt_month);
		mBackButton = (Button) findViewById(R.id.registration_back_btn);
		mConfirmButton = (Button) findViewById(R.id.registration_confirm_btn);
	}
	
	private void initializeListeners() {
		mBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent regIntent = new Intent(v.getContext(), RegistrationActivity.class);
		        startActivityForResult(regIntent, 0);
			}
		});
		
		mConfirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent passwordIntent = new Intent(v.getContext(), PasswordActivity.class);
		        startActivityForResult(passwordIntent, 0);
			}
		});
		
		btnMonthUp.setOnClickListener(this);
		btnMonthDown.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_data_nascita, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
