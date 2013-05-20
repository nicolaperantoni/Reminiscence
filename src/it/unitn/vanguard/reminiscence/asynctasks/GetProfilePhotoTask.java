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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetProfilePhotoTask extends AsyncTask<Integer, JSONObject, Boolean> {

	private OnTaskFinished caller;
	private JSONObject json;
	private Context context;

	public GetProfilePhotoTask(OnTaskFinished caller, Context context) {
		this.caller = caller;
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Integer... arg0) {

		String token = FinalFunctionsUtilities.getSharedPreferences(Constants.TOKEN_KEY, context);

		if (!token.equals("")) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(1);
			params.add(new BasicNameValuePair("token", token));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "getProfileImage.php");
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			json = null;
			String jsonString;
			try {
				jsonString = EntityUtils.toString(client.execute(post).getEntity());
				json = new JSONObject(jsonString);
				if (json != null && json.getString("success").equals("true")) {
					return true;
				}
			} catch (Exception e) {
				Log.e(GetProfilePhotoTask.class.getName(), e.toString());
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
