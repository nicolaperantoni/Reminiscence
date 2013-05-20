package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainPageActivity extends Activity {

	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = MainPageActivity.this;
		
		String language = FinalFunctionsUtilities
				.getSharedPreferences(Constants.LANGUAGE_KEY, context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_main_page);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		String language = FinalFunctionsUtilities
				.getSharedPreferences(Constants.LANGUAGE_KEY, context);
		Locale locale = new Locale(language);

		if(locale.toString().equals(Locale.ITALIAN.getLanguage()) || locale.toString().equals(Locale.ITALY.getLanguage())) {
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
		    case android.R.id.home: this.finish();break;
	    }
		
		// Refresh activity
		if(locale != null && FinalFunctionsUtilities.switchLanguage(locale, context)) {
		    finish();
		    startActivity(getIntent());
	    }
	    return true;
	}
}