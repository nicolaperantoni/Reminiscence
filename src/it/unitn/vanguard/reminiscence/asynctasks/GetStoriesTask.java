package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class GetStoriesTask extends AsyncTask<String, Void, Boolean> {

	private OnTaskFinished caller;
	private Exception ex;
	private JSONObject json;

	public GetStoriesTask(OnTaskFinished caller) {
		this.caller = caller;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		if (arg0.length < 1) {
			throw new IllegalStateException("You should pass at least 1 params");
		}

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		
		//ottiene il token se presente
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(((Activity) caller)
						.getApplicationContext());
		String token = prefs.getString("token", "");

		if (!token.equals("")) {
			params.add(new BasicNameValuePair("year", arg0[0]));
			params.add(new BasicNameValuePair("token", token));
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "login.php"); //find link from luca
		}

		return null;
	}
}
