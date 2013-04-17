package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.LoginTask;
import it.unitn.vanguard.reminiscence.asynctasks.RegistrationTask;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity implements OnTaskFinished {

	private EditText editTextPassword;
	private Button btnBack,btnFrecciaBack;
	private Button btnConfirm,btnFrecciaConferma;
	private Button btnReloadPasswd;
	protected ProgressDialog p;
	
	private String name;
	private String surname;
	private String mail;
	private String day;
	private String month;
	private String year;
	private String password;
	
	// Password suggestion
	private ArrayList<String> suggestionList;
	private int rand;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_password);
		initializeButtons();
		initializeVars();
		generateSuggestion();
		initializeListeners();
	}

	private void initializeVars() {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		name = prefs.getString("name", "");
		surname = prefs.getString("surname", "");
		mail = prefs.getString("mail", "");
		day = prefs.getString("day", "");
		month = prefs.getString("month", "");
		year = prefs.getString("year", "");
		
		//Toast.makeText(this, mail, Toast.LENGTH_SHORT).show();
		
		suggestionList = new ArrayList<String>(2);
		suggestionList.add(name); 
		suggestionList.add(surname);
		
	}

	private void initializeButtons() {
		editTextPassword = (EditText) findViewById(R.id.registration_password_ET);
		btnBack = (Button) findViewById(R.id.button_back);
		btnConfirm = (Button) findViewById(R.id.button_conferma);
		btnFrecciaConferma = (Button) findViewById(R.id.button_freccia_conferma);
		btnFrecciaBack = (Button) findViewById(R.id.button_freccia_back);
		btnReloadPasswd = (Button) findViewById(R.id.btnReloadPassword);
	}
	
	OnClickListener onclickback = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	OnClickListener onclickconf = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			password = editTextPassword.getText().toString();
			boolean isPasswordEmpty = password.trim().equals("");
			if(isPasswordEmpty) {
				Toast.makeText(getApplicationContext(), getResources().getText(R.string.registration_password_empty),
						Toast.LENGTH_LONG).show();
			}
			else if(FinalFunctionsUtilities.isDeviceConnected(getApplicationContext())) {
				p = new ProgressDialog(PasswordActivity.this);
				p.setTitle(getResources().getString(R.string.please));
				p.setMessage(getResources().getString(R.string.wait));
				p.setCancelable(false);
				p.show();
				new RegistrationTask(PasswordActivity.this).execute(name, surname, mail, 
					password, day, month, year);
			}
			else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_fail),
						Toast.LENGTH_LONG).show();
			}
		}
	};
	
	private void initializeListeners() {
		
		btnBack.setOnClickListener(onclickback);
		btnFrecciaBack.setOnClickListener(onclickback);
		btnConfirm.setOnClickListener(onclickconf);
		btnFrecciaConferma.setOnClickListener(onclickconf);
		
		btnReloadPasswd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				generateSuggestion();
				//Toast.makeText(getApplicationContext(), btnReloadPasswd.toString(), Toast.LENGTH_SHORT).show();
				Animation anim = AnimationUtils.loadAnimation(PasswordActivity.this, R.anim.rotate_arrow_360);
				v.startAnimation(anim);
			}
		});
	}

	private void generateSuggestion() {
		password = "";
		rand = (int)(Math.random() * suggestionList.size());
		password += suggestionList.get(rand);
		password += Constants.PASSWORD_MIN + (int)(Math.random() * ((Constants.PASSWORD_MAX - Constants.PASSWORD_MIN) + 1));
		editTextPassword.setText(password);
	}
	
	@Override
	public void onTaskFinished(JSONObject res) {
		//per ottenersi la stringa giusta in base al risultato
		//String resultText = getResources().getString(((res)?R.string.registration_succes:R.string.registration_failed));
		String ret = null;
		/*try { 
			ret = res.getString("success")+res.getString("err1")+res.getString("err2")+res.getString("err3")+res.getString("err4")+res.getString("err5");
			Toast.makeText(this, ret, Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//se la registrazione ha successo torna alla pagina di login
		/*if (ret.startsWith("true")) {
			Intent loginIntent = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivityForResult(loginIntent, 0);
		} else*/
			
		try { 
			ret = res.getString("success")+res.getString("err1")+res.getString("err2")+res.getString("err3")+res.getString("err4")+res.getString("err5");
			if(p!=null && p.isShowing()){
				p.dismiss();
			}
			if (ret.startsWith("true")) {
			
				Toast.makeText(this,
				getResources().getText(R.string.registration_succes),
				Toast.LENGTH_SHORT).show();
				

				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			    SharedPreferences.Editor editor = prefs.edit();

			    editor.putString("password", password);
				
				editor.commit();
				
				// intent
				Intent loginIntent = new Intent(getApplicationContext(), ViewStoriesFragmentActivity.class);
				loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(loginIntent, 0);
			}
			else {
				Toast.makeText(this,
				getResources().getText(R.string.registration_failed),
				Toast.LENGTH_SHORT).show();
				/*
				 * 	
				 */
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
