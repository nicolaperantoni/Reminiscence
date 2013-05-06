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
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FriendListFragment extends ListFragment {
	
	
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
	public void onStart() {
		super.onStart();	
		String language = FinalFunctionsUtilities.getSharedPreferences("language", getActivity());
		FinalFunctionsUtilities.switchLanguage(new Locale(language), getActivity());
		
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setAdapter();
	}


	private void setAdapter() {

		ArrayList<String> aList = new ArrayList<String>();
		 
		for(int i=0; i < names.length; i++) {
			aList.add(names[i] + " " + surnames[i]);
		}
		
		ArrayAdapter<String> t = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, aList);
		//ListView listv = (ListView) getActivity().findViewById(R.id.friendlistview);
		setListAdapter(t);
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
		super.onListItemClick(l, v, position, id);
		
		((ViewFriendsProfileFragmentActivity) getActivity()).onItemSelect(names[position], surnames[position]);
	}



	public interface OnItemSelectListener {
		public void onItemSelect(String name, String surname);
	}
	
}
