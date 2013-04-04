package it.unitn.vanguard.reminiscence;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChangePassword extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_password, menu);
		return true;
	}

}
