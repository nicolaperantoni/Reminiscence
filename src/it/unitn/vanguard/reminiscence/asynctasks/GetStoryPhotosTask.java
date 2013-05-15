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

public class GetStoryPhotosTask extends AsyncTask<Integer, JSONObject, Boolean> {

	private OnTaskFinished caller;
	private Exception ex;
	private JSONObject json;
	private Context context;

	public GetStoryPhotosTask(OnTaskFinished caller, Context context) {
		this.caller = caller;
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Integer... arg0) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(1);

		// ottiene il token se presente
		String token = FinalFunctionsUtilities.getSharedPreferences(Constants.TOKEN_KEY, context);
		Log.e("token", "->"+token);

		if (!token.equals("") && FinalFunctionsUtilities.isDeviceConnected(context)) {
			
			params.add(new BasicNameValuePair("token", token));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "getProfileImage.php");
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
					return true;
				}
			} catch (Exception e) {
				this.ex = e;
				Log.e("Error", e.toString());
				return false;
			}
		}
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (!result && ex != null) {
			Log.e(GetStoryPhotosTask.class.getName(), ex.toString());
		}
		caller.onTaskFinished(json);
	}
}
