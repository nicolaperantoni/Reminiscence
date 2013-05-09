package it.unitn.vanguard.reminiscence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CheckBoxAmici extends Activity {

	private Button invia_mail;
	String[] genre = { "party lol", "toni champa", "lollosissimo lui",
			"san girolamo", "peter pan", "lol", "lol", "lol", "lol", "lol",
			"lol", "lol", "lol", "lol", "lol", "lol", "lol" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkbox_amici);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, genre);
		ListView lv = (ListView) findViewById(R.id.lista_amici_checkbox);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(adapter);

		initializeButtons();
	}

	private void initializeButtons() {
		invia_mail = (Button) findViewById(R.id.btn_invia_mail);
		invia_mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"Funzione non disponibile!", Toast.LENGTH_LONG).show();
			}
		});
	}

}
