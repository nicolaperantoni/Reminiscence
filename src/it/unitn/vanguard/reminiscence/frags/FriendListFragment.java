package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.ViewFriendsProfileFragmentActivity;
import it.unitn.vanguard.reminiscence.asynctasks.GetFriendsTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendListFragment extends ListFragment implements OnTaskFinished {

	private OnItemSelectListener listener;

	String[] names = new String[] { "Marco", "Lucia", "Francesco", "Luca",
			"Erica", "Francesca", "Giovanni", "Nicola", "Mario", "Ignazio" };

	String[] surnames = new String[] { "Rossi", "Bacchi", "Padovan", "Cabella",
			"Iacchini", "Zen", "Fabbri", "Radini", "Verdi", "Zago" };

	int[] ids = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	@Override
	public void onStart() {
		super.onStart();
		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", getActivity());
		FinalFunctionsUtilities.switchLanguage(new Locale(language),
				getActivity());

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (FinalFunctionsUtilities.isDeviceConnected(getActivity())) {
			new GetFriendsTask(this, getActivity()).execute();
		}
	}

	private void setAdapter() {
		Friend aList[] = new Friend[names.length];
		for (int i = 0; i < names.length; i++) {
			aList[i] = new Friend(names[i], surnames[i]);
		}
		CustomAdapter t = new CustomAdapter(getActivity(), aList);
		setListAdapter(t);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof OnItemSelectListener) {
			listener = (OnItemSelectListener) activity;
		} else {
			throw new ClassCastException(
					activity.toString()
							+ " must implemenet FriendListFragment.OnItemSelectListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// super.onListItemClick(l, v, position, id);

		((ViewFriendsProfileFragmentActivity) getActivity()).onItemSelect(
				names[position], surnames[position]);
	}

	public interface OnItemSelectListener {
		public void onItemSelect(String name, String surname);
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		boolean status;
		int count = 10;
		boolean success = false;

		// ottengo se l'operazione ha avuto successo e il numero di valori dal
		// json
		try {
			success = res.getString("success").equals("true");
			if (success) {
				count = Integer.parseInt(res.getString("numFriend"));
			}
		} catch (Exception e) {
			Log.e("itemselected", e.toString());
		}

		if (success) {
			// scorro il json
			if (count < 1) {
				names = new String[1];
				surnames = new String[1];
				ids = new int[1];
				names[0] = getString(R.string.no_friends_a);
				surnames[0] = getString(R.string.no_friends_b);
				ids[0] = -1;
			} else {
				names = new String[count];
				surnames = new String[count];
				ids = new int[count];
				for (int i = 0; i < count; i++) {
					String ct = "f" + i;
					JSONObject json = null;
					try {
						json = new JSONObject(res.getString(ct));
						names[i] = json.getString("Nome");
						surnames[i] = json.getString("Cognome");
						ids[i] = Integer.parseInt(json.getString("Id"));
					} catch (Exception e) {
						Log.e("tuamadre", e.toString());
					}
				}
			}
			setAdapter();
			((ViewFriendsProfileFragmentActivity) getActivity()).onItemSelect(
					names[0], surnames[0]);
		} else {
			Log.e("json", "errore nell operazione success:false");
		}
	}

	private class CustomAdapter extends ArrayAdapter<Friend> {

		public final Context context;
		public final Friend friend[];

		public CustomAdapter(Context context, Friend friend[]) {
			super(context, R.layout.friend_listview_item, friend);
			this.context = context;
			this.friend = friend;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.friend_listview_item,
					parent, false);
			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.item_name);
			TextView surnameTextView = (TextView) rowView
					.findViewById(R.id.item_surname);
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.item_profile_image);
			nameTextView.setText(friend[position].name);
			surnameTextView.setText(friend[position].surname);
			imageView.setImageResource(R.drawable.default_profile_image);

			return rowView;
		}

	}

	private class Friend {

		public String name;
		public String surname;
		public ImageView image = null;
		public boolean request = false;

		public Friend(String name, String surname) {
			super();
			this.name = name;
			this.surname = surname;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}

		public ImageView getImage() {
			return image;
		}

		public void setImage(ImageView image) {
			this.image = image;
		}

		public boolean isRequest() {
			return request;
		}

		public void setRequest(boolean request) {
			this.request = request;
		}
	}
}
