package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.asynctasks.GetSuggLuogoNascita;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class LuogoNascitaActivity extends Activity implements OnTaskFinished {
	
	protected ProgressDialog p;
	private Button btnLuogoNascitaConfirm, btnLuogoNascitaBack;
	private AutoCompleteTextView txtLuogoNascita;
	private String first=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luogo_nascita);
		initializeButtons();
		initializeListeners();
	}
	
	private void initializeButtons() {
		btnLuogoNascitaConfirm = (Button) findViewById(R.id.btnLuogoNascita);
		btnLuogoNascitaBack = (Button) findViewById(R.id.btnLuogoNascitaBack);
		txtLuogoNascita = (AutoCompleteTextView) findViewById(R.id.txtLuogoNascita);
	}

	private void initializeListeners() {
		
		txtLuogoNascita.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				//new GetSuggLuogoNascita(LuogoNascitaActivity.this).execute(txtLuogoNascita.getText().toString());
				
				return false;
			}
		});
		
		txtLuogoNascita.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
				String h = txtLuogoNascita.getText().toString();
				h = h.trim();
				h = h.replace(" ", "+");
				if(!h.equals(""))
					new GetSuggLuogoNascita(LuogoNascitaActivity.this).execute(h);
				
			}
		});
		
		btnLuogoNascitaBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish(); 
			}
		});
		
		btnLuogoNascitaConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// intent
				Intent loginIntent = new Intent(getApplicationContext(), ViewStoriesFragmentActivity.class);
				loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(loginIntent, 0);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_password, menu);
		return true;
	}

	private List removeDuplicate(List sourceList){
		Set setPmpListArticle=new HashSet(sourceList);
		return new ArrayList(setPmpListArticle);
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		Log.e("asd", res.toString());
		ArrayList<String> sugg = new ArrayList<String>();
		try {
			sugg.add(res.getString("mun0"));
			sugg.add(res.getString("mun1"));
			sugg.add(res.getString("mun2"));
			sugg.add(res.getString("mun3"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), 
				android.R.layout.simple_dropdown_item_1line,removeDuplicate(sugg));
		txtLuogoNascita.setAdapter(adapter);
		txtLuogoNascita.showDropDown();
	}
}


















