package it.unitn.vanguard.reminiscence;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author forna
 * 
 */
public class PasswordActivity extends Activity {

	private EditText mPasswordEditText;
	private Button mBackButton;
	private Button mConfirmButton;
	
	private String name;
	private String surname;
	private String eMail;
	private String day;
	private String month;
	private String year;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_password);

		dispatchInfoFromIntent(getIntent());
		initializeButton();
		initializeListeners();
	}
	
	private void dispatchInfoFromIntent(Intent i) {
		name=i.getExtras().getString("name");
		surname=i.getExtras().getString("surname");
		eMail=i.getExtras().getString("email");
		day=i.getExtras().getString("day");
		month=i.getExtras().getString("month");
		year=i.getExtras().getString("year");
	}

	private void initializeButton() {
		mPasswordEditText = (EditText) findViewById(R.id.registration_password_ET);
		mBackButton = (Button) findViewById(R.id.registration_back_btn);
		mConfirmButton = (Button) findViewById(R.id.registration_confirm_btn);
	}

	private void initializeListeners() {
		mBackButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO call previous activity
			}
		});
		mConfirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO post
			}
		});
	}

}
