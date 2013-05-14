package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.Friend;
import it.unitn.vanguard.reminiscence.FriendListActivity;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;

public class DeleteFriendTask extends AsyncTask<String, Void, Boolean> {
	
	OnTaskFinished caller;
	JSONObject json;
	int position;

	public DeleteFriendTask(OnTaskFinished caller, int position) {
		super();
		this.position = position;
		this.caller = caller;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		String token = FinalFunctionsUtilities.getSharedPreferences("token",
				(Activity) caller);
		
		if (arg0.length != 3) {
			throw new IllegalStateException(
					"Error too few arguments passed in the AddFriendTask");
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		((FriendListActivity) caller).friendDeleted(position);
	}
}
