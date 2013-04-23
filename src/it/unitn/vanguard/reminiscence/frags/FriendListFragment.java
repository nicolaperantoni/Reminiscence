package it.unitn.vanguard.reminiscence.frags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.ViewFriendsProfileFragmentActivity;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FriendListFragment extends ListFragment {
	
	Context context;
	ListView lv;
	
	private OnItemSelectListener listener;
	
	String[] names = new String[]{"Marco", "Lucia", "Francesco", "Luca", "Erica", "Francesca", "Giovanni"
			, "Nicola", "Mario", "Ignazio"};
	
	String[] surnames = new String[]{"Rossi", "Bacchi", "Padovan", "Cabella", "Iacchini", "Zen"
			, "Fabbri", "Radini", "Verdi", "Zago"};
	
	String[] mails = new String[]{"a@a.a", "b@b.b", "c@c.c", "d@d.d", "e@e.e", "f@f.f", "g@g.g", "h@h.h"
			, "i@i.i", "l@l.l"};

	int[] images = new int[]{R.drawable.default_profile_image,
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_friendlist, container,false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();	

		context = getActivity();
		lv = (ListView) getActivity().findViewById(R.id.friend_list_view);
		
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);		
		
		setAdapter();
		
	}
	
	private void setAdapter() {

		List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
		 
		for(int i=0; i < names.length; i++) {
			HashMap<String, String> hm = new HashMap<String,String>();
			hm.put("name", names[i]);
			hm.put("surname", surnames[i]);
			hm.put("image", Integer.toString(images[i]));
			aList.add(hm);
		}
		
		String[] from = new String[]{"name", "surname", "image"};
		int[] to = new int[]{R.id.name, R.id.surname, R.id.flag};
		
		
		SimpleAdapter ad = new SimpleAdapter(getActivity(), aList, R.layout.friend_listview_item, from, to);
		ListView lv = (ListView) getView().findViewById(R.id.friendlistview);
		lv.setAdapter(ad);
	}
	
	public void setNames(String[] name, String[] surname, String[] mail) {
		if(name.length!=surname.length || name.length!=mail.length) {
			return;
		}
		names = name;
		surnames = surname;
		mails = mail;
		images = new int[name.length];
		for (int i=0; i<name.length; i++) {
			images[i] = R.drawable.default_profile_image;
		}
		
		setAdapter();
	}
	
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (activity instanceof OnItemSelectListener) {
			listener = (OnItemSelectListener) activity;
		} else {
	        throw new ClassCastException(activity.toString()
	                + " must implemenet FriendListFragment.OnItemSelectListener");
	    }
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(lv, v, position, id);
		
		((ViewFriendsProfileFragmentActivity) context).onItemSelect(names[position], surnames[position]);
	}



	public interface OnItemSelectListener {
		public void onItemSelect(String name, String surname);
	}
	
}
