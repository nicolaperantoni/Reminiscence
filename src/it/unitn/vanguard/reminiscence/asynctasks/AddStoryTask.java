package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
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

public class AddStoryTask extends AsyncTask<String, Void, JSONObject> {

	public OnTaskFinished caller;
	public Exception ex;

	public AddStoryTask(OnTaskFinished caller) {
		super();
		this.caller = caller;
	}

	@Override
	protected JSONObject doInBackground(String... arg) {
		
		if (arg.length != 3) {
			throw new IllegalStateException("You should provide 3 parameters");
		}

		String token = FinalFunctionsUtilities
				.getSharedPreferences(Constants.TOKEN_KEY, ((Activity) caller).getApplicationContext());
		
		if (!token.equals("")) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("year", "" + arg[0]));
			params.add(new BasicNameValuePair("text", "" + arg[1]));
			params.add(new BasicNameValuePair("title", "" + arg[2]));
			params.add(new BasicNameValuePair("token", token));

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "addStory.php");
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = client.execute(post);
				return new JSONObject( EntityUtils.toString(response.getEntity()));
			} catch (Exception e) {
				Log.e(AddStoryTask.class.getName(), e.toString());
				e.printStackTrace();
				ex = e;
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(JSONObject res) {
		super.onPostExecute(res);
		caller.onTaskFinished(res);
	}
}