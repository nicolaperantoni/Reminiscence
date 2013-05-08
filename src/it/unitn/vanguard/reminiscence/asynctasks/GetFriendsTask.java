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
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;

public class GetFriendsTask extends AsyncTask<Integer, JSONObject, Boolean> {
	
	OnTaskFinished caller;
	Context context;
	private JSONObject json;	
	private Exception ex = null;
	

	public GetFriendsTask(OnTaskFinished caller, Context context) {
		super();
		this.caller = caller;
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Integer... arg0) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		

		String token = FinalFunctionsUtilities.getSharedPreferences(Constants.TOKEN_KEY, context);
		Log.e("token", "->"+token);
		
		if (!token.equals("") && FinalFunctionsUtilities.isDeviceConnected(context)) {
			Log.e("friendlisttask","invio la richiesta per la lista amici");
			params.add(new BasicNameValuePair("token", token));

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "getFriends.php");
			
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
				Log.e("friendlisttask", json.toString());
			} catch (Exception e) {
				this.ex = e;
				return false;
			}
			return true;
		}
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		caller.onTaskFinished(json);
	}
	
	

}
