package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.frags.StoryFragment;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskExecuted;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class GetStoriesTask extends AsyncTask<Integer, JSONObject, Boolean> {

	private OnTaskExecuted caller;
	private Exception ex;
	private JSONObject json;
	private int lastYear;

	public GetStoriesTask(OnTaskExecuted caller,int lastYear) {
		this.caller = caller;
		this.lastYear=lastYear;
	}

	@Override
	protected Boolean doInBackground(Integer... arg0) {

		if (arg0.length < 1) {
			throw new IllegalStateException("You should pass at least 1 params");
		}

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);

		// ottiene il token se presente
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(((Activity) caller)
						.getApplicationContext());
		String token = prefs.getString(Constants.TOKEN_KEY, "");

		if (!token.equals("") && arg0[0]>=lastYear) {
			params.add(new BasicNameValuePair("year",""+ arg0[0]));
			params.add(new BasicNameValuePair("token", token));

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "getStory.php");
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = client.execute(post);
				String s = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(s);
				int n = json.getInt("numStory");
				for (int i = 0; i < n; i++) {
					JSONObject story = new JSONObject(json.get("s" + i)
							.toString());
					publishProgress(story);
				}
				return true;
			} catch (Exception e) {
				ex = e;
			}

		}

		return false;
	}

	@Override
	protected void onProgressUpdate(JSONObject... values) {
		super.onProgressUpdate(values);
		StoryFragment story = new StoryFragment();
		String title;
		try {
			title = values[0].getString("Title");
			String desc = values[0].getString("Text");
			Bundle b = new Bundle();
			b.putString(StoryFragment.TITLE_PASSED_KEY, title);
			b.putString(StoryFragment.DESCRIPTION_PASSED_KEY, desc);
			story.setArguments(b);
			FinalFunctionsUtilities.stories.add(story);
			caller.OnProgress();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			caller.OnFinish(result);
		}
	}

}
