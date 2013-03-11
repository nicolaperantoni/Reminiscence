package it.unitn.vanguard.reminiscence;

import android.app.Activity;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_password);

		initializeButton();
		initializeListeners();
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
