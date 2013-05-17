package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.io.UnsupportedEncodingException;
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
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetStoryCoverTask extends AsyncTask<String, Void, JSONObject> {

	private OnTaskFinished caller;
	private Exception ex;
	private JSONObject json;
	private Context context;

	public GetStoryCoverTask(OnTaskFinished caller, Context context) {
		this.caller = caller;
		this.context = context;
	}

	@Override
	protected JSONObject doInBackground(String... arg) {
		
		if (arg.length < 2) {
			throw new IllegalStateException("You should provide a story id");
		}
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		if (!arg[1].equals("")) {
			params.add(new BasicNameValuePair("token", arg[0]));
			params.add(new BasicNameValuePair("story_id", "" + arg[1]));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "getStoryCover.php");
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = client.execute(post);
				String s = EntityUtils.toString(response.getEntity());
				Log.e("getStoryCover", " json: " + s);
				return new JSONObject(s);
			} catch (Exception e) {
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
