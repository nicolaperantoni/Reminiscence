package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;

import java.io.UnsupportedEncodingException;
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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class ChangePasswordTask extends AsyncTask<String, Void, Boolean> {

	private OnTaskFinished caller;
	private Exception ex;
	private JSONObject json;

	public ChangePasswordTask(OnTaskFinished caller) {
		super();
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
		String old = prefs.getString("password", "");

		if (!token.equals("")) {
			params.add(new BasicNameValuePair("oldpass", old ));
			params.add(new BasicNameValuePair("newpass", arg0[0]));
			params.add(new BasicNameValuePair("token", token));
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "modificapass.php");
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			json = null;
			String jsonString;
			try {
				jsonString = EntityUtils.toString(client.execute(post).getEntity());
				json = new JSONObject(jsonString);
				if (json != null && json.getString("success").equals("true")) {
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("password", json.getString("password"));
					editor.commit();
					return true;
				}
	
			} catch (Exception e) {
				this.ex = e;
				return false;
			}
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (!result && ex != null) {
			Log.e(RegistrationTask.class.getName(), ex.toString());
		}
		caller.onTaskFinished(json);
	}
}