package it.unitn.vanguard.reminiscence;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DataNascitaActivity extends Activity implements OnClickListener{

	private Button btnMonthUp, btnMonthDown;
	private TextView txtMonth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_nascita);
		
		btnMonthUp = (Button) findViewById(R.id.btn_month_up);
		btnMonthDown = (Button) findViewById(R.id.btn_month_down);
		txtMonth = (TextView) findViewById(R.id.txt_month);
		
		btnMonthUp.setOnClickListener(this);
		btnMonthDown.setOnClickListener(this);
		
		txtMonth.setText("Novembre");
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
