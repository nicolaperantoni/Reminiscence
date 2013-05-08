package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.frags.StoryFragment;
import it.unitn.vanguard.reminiscence.interfaces.OnTask;
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

public class GetStoriesTask extends AsyncTask<Integer, JSONObject, Boolean> 
	implements Comparable<GetStoriesTask> {

	private OnTask caller;
	private Exception ex;
	private JSONObject json;
	private int year;

	public GetStoriesTask(OnTask caller, Integer year) {
		this.caller = caller;
		if (year != null)
			this.year = year;
	}

	@Override
	protected Boolean doInBackground(Integer... arg) {

		if (arg.length > 0) {
			if (arg[0] != null)
				this.year = arg[0];
			else
				throw new IllegalStateException("You should provide a year");
		}

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);

		// ottiene il token se presente
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(((Activity) caller)
						.getApplicationContext());
		String token = prefs.getString(Constants.TOKEN_KEY, "");

		if (!token.equals("")) {
			params.add(new BasicNameValuePair("year", "" + year));
			params.add(new BasicNameValuePair("token", token));

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + "getStory.php");
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = client.execute(post);
				String s = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(s);
				int n = json.getInt("numStory");
				if (n < 1)
					return false;
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
			b.putInt(StoryFragment.YEAR_PASSED_KEY, year);
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
		caller.OnFinish(result);
	}

	@Override
	public int compareTo(GetStoriesTask another) {
		if (this.year>another.year)
			return -1;
		else if(this.year==another.year)
			return 0;
		return 1;
	}

}
