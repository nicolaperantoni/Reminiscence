package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewFriendsProfileFragmentActivity extends FragmentActivity {
	
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		context = getApplicationContext();
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_viewfriendprofile);
		
		String[] names = new String[]{"gino", "pippo", "pluto", "paperino", "topolino", "gina", "paperone"
				, "ciccio", "roxy", "mirko"};
		
		String[] surnames = new String[]{"postino", "spazzino", "poliziotto", "insegnante", "operaio", "zen"
				, "rossi", "verdi", "kjhso", "sdlgknf"};
		
		List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
		 
		for(int i=0; i<names.length; i++) {
			HashMap<String, String> hm = new HashMap<String,String>();
			hm.put("name", names[i]);
			hm.put("surname", surnames[i]);
			aList.add(hm);
		}
		
		String[] from = new String[]{"name", "surname"};
		int[] to = new int[]{R.id.name_list_textview, R.id.surname_list_textview};
		
		
		SimpleAdapter ad = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_item_row, from, to);
		ListView lv = (ListView) findViewById(R.id.friend_list_view);
		lv.setAdapter(ad);
	}

}
