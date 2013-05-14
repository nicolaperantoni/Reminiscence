package it.unitn.vanguard.reminiscence.frags;

import it.unitn.vanguard.reminiscence.Friend;
import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.ViewFriendsProfileFragmentActivity;
import it.unitn.vanguard.reminiscence.adapters.FriendListAdapter;
import it.unitn.vanguard.reminiscence.asynctasks.GetFriendsTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class FriendListFragment extends ListFragment implements OnTaskFinished {

	private OnItemSelectListener listener;

	ArrayList<Friend> friends = new ArrayList<Friend>();

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
		Friend fr[] = new Friend[friends.size()];
		friends.toArray(fr);
		FriendListAdapter t = new FriendListAdapter(getActivity(), fr);
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

		((ViewFriendsProfileFragmentActivity) getActivity())
				.onItemSelect(friends.get(position));
	}

	public interface OnItemSelectListener {
		public void onItemSelect(Friend friend);
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		boolean status;
		int count = 10;
		boolean success = false;

		// ottengo se l'operazione ha avuto successo e il numero di valori dal
		// json
		/*
		 * json: {"success":true, "numFriend":2 "f0":{"Nome":asd, "Cognome":asd,
		 * "Id":1} "f1":{"Nome":ciccio, "Cognome":ciccia, "Id":2}}
		 */
		try {
			success = res.getString("success").equals("true");
			if (success) {
				count = Integer.parseInt(res.getString("numFriend"));
			}
		} catch (Exception e) {
			Log.e("itemselected", e.toString());
		}

		if (success) {
			friends.clear();
			// scorro il json
			if (count < 1) {
				// caso in cui non ci siano amici LAMER
				friends.add(new Friend(getString(R.string.no_friends_a),
						getString(R.string.no_friends_b),"", -1));

			} else {
				// caso normale
				for (int i = 0; i < count; i++) {
					String ct = "f" + i;
					JSONObject json = null;
					try {
						json = new JSONObject(res.getString(ct));
						friends.add(new Friend(json.getString("Nome"), json
								.getString("Cognome"),"", Integer.parseInt(json
								.getString("Id"))));
					} catch (Exception e) {
						Log.e("flf", e.toString());
					}
				}
			}
			setAdapter();
		} else {
			Log.e("json", "errore nell operazione success:false");
		}
	}
}
