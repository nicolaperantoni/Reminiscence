package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.FriendListActivity;
import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DeleteFriendTask extends AsyncTask<String, Void, Boolean> {

	OnTaskFinished caller;
	JSONObject json;
	int position;
	Exception ex;

	public DeleteFriendTask(OnTaskFinished caller, int position) {
		super();
		this.position = position;
		this.caller = caller;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		
		if (arg0.length != 1) {
			throw new IllegalStateException("You should pass 1 parameter");
		}
		
		String token = FinalFunctionsUtilities
				.getSharedPreferences(Constants.TOKEN_KEY, (Activity) caller);
		
		if (!token.equals("")) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("token", token));
			params.add(new BasicNameValuePair("idFriend", arg0[0]));

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "removeFriend.php");

			String jsonstring;
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
				jsonstring = EntityUtils.toString(client.execute(post).getEntity());
				json = new JSONObject(jsonstring);
			} catch (Exception e) {
				Log.e(DeleteFriendTask.class.getName(), e.toString());
				e.printStackTrace();
				return false;
			}
		} else {
			Toast.makeText((Activity) caller, R.string.connection_fail,
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) { ((FriendListActivity) caller).friendDeleted(position); }
		caller.onTaskFinished(json);
	}
}