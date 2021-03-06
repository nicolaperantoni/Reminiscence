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

public class ChangePasswordTask extends AsyncTask<String, Void, Boolean> {

	private OnTaskFinished caller;
	private JSONObject json;

	public ChangePasswordTask(OnTaskFinished caller) {
		super();
		this.caller = caller;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {


		if (arg0.length != 2) {
			throw new IllegalStateException("You should pass 2 parameters: old_password and new_password");
		}
		
		String token = FinalFunctionsUtilities
				.getSharedPreferences(Constants.TOKEN_KEY, ((Activity) caller).getApplicationContext());
		String old = FinalFunctionsUtilities
				.getSharedPreferences(Constants.PASSWORD_KEY, ((Activity) caller).getApplicationContext());
		
		if (!token.equals("")) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
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
					FinalFunctionsUtilities
					.setSharedPreferences(Constants.PASSWORD_KEY, arg0[0], ((Activity) caller)
							.getApplicationContext());
					return true;
				}
			} catch (Exception e) {
				Log.e(ChangePasswordTask.class.getName(), e.toString());
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		caller.onTaskFinished(json);
	}
}