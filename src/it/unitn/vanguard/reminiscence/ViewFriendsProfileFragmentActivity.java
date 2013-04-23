package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import it.unitn.vanguard.reminiscence.frags.FriendListFragment;
import it.unitn.vanguard.reminiscence.frags.ProfileFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewFriendsProfileFragmentActivity extends FragmentActivity implements FriendListFragment.OnItemSelectListener{
	
	private Context context;
	private FriendListFragment fl;
	private ProfileFragment pf;
	
	String[] names = new String[]{"Marco", "Lucia", "Francesco", "Luca", "Erica", "Francesca", "Giovanni"
			, "Nicola", "Mario", "Ignazio"};
	
	String[] surnames = new String[]{"Rossi", "Bacchi", "Padovan", "Cabella", "Iacchini", "Zen"
			, "Fabbri", "Radini", "Verdi", "Zago"};
	
	String[] mails = new String[]{"a@a.a", "b@b.b", "c@c.c", "d@d.d", "e@e.e", "f@f.f", "g@g.g", "h@h.h"
			, "i@i.i", "l@l.l"};

	int[] flags = new int[]{R.drawable.default_profile_image,
							R.drawable.default_profile_image,
							R.drawable.default_profile_image,
							R.drawable.default_profile_image,
							R.drawable.default_profile_image,
							R.drawable.default_profile_image,
							R.drawable.default_profile_image,
							R.drawable.default_profile_image,
							R.drawable.default_profile_image,
							R.drawable.default_profile_image,};
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		pf = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.user_profile_fragment);
		fl = (FriendListFragment) getSupportFragmentManager().findFragmentById(R.id.friend_list_fragment);
		
		context = getApplicationContext();
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_friendlist_profile);		
		
		List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
		 
		for(int i=0; i < names.length; i++) {
			HashMap<String, String> hm = new HashMap<String,String>();
			hm.put("name", names[i]);
			hm.put("surname", surnames[i]);
			hm.put("image", Integer.toString(flags[i]));
			aList.add(hm);
		}
		
		String[] from = new String[]{"name", "surname", "image"};
		int[] to = new int[]{R.id.name, R.id.surname, R.id.flag};
		
		
		SimpleAdapter ad = new SimpleAdapter(getBaseContext(), aList, R.layout.friend_listview_item, from, to);
		ListView lv = (ListView) findViewById(R.id.friendlistview);
		lv.setAdapter(ad);
	}
	
	
	@Override
	public void onItemSelect(String name, String surname) {
		pf.update(name, surname);
	}





}
