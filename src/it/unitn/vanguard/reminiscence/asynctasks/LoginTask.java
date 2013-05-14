package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

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
import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<String, Void, Boolean> {

	private OnTaskFinished caller;
	private Exception ex;
	private JSONObject json;

	public LoginTask(OnTaskFinished caller) {
		super();
		this.caller = caller;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		if (arg0.length < 2) {
			throw new IllegalStateException("You should pass at least 2 params");
		}

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("email", arg0[0]));
		params.add(new BasicNameValuePair("password", arg0[1]));

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constants.SERVER_URL + "login.php");
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
				FinalFunctionsUtilities.setSharedPreferences(
						Constants.MAIL_KEY, arg0[0], (Activity) caller);
				FinalFunctionsUtilities.setSharedPreferences(
						Constants.PASSWORD_KEY, arg0[1], (Activity) caller);
				FinalFunctionsUtilities.setSharedPreferences(
						Constants.TOKEN_KEY, json.getString("token"),
						(Activity) caller);
			}

		} catch (Exception e) {
			this.ex = e;
			return false;
		}

		return true;
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