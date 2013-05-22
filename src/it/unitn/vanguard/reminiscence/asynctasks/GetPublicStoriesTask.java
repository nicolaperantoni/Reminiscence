package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnGetStoryTask;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import it.unitn.vanguard.reminiscence.utils.Story;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class GetPublicStoriesTask extends
		AsyncTask<Integer, JSONObject, Boolean> implements
		Comparable<GetPublicStoriesTask> {

	private OnGetStoryTask caller;
	private int year;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		caller.OnStart();
	}

	public GetPublicStoriesTask(OnGetStoryTask caller, Integer year) {
		this.caller = caller;
		if (year != null)
			this.year = year;
	}

	@Override
	protected Boolean doInBackground(Integer... arg) {

		if (arg.length > 0) {
			if (arg[0] != null)
				this.year = arg[0];
			else {
				throw new IllegalStateException("You should provide a year");
			}
		}

		HttpClient client = new DefaultHttpClient();
		HttpGet post = new HttpGet(
				"http://test.reminiscens.me/lifecontext/api/events?decade="
						+ year);
		try {
			HttpResponse response = client.execute(post);
			String s = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(s);
			JSONArray events = json.getJSONArray("events");
			for (int i = 0; i < events.length(); i++) {
				JSONObject story = events.getJSONObject(i);
				publishProgress(story);
			}
			return true;
		} catch (Exception e) {
			Log.e(GetPublicStoriesTask.class.getName(), e.toString() + "asdada");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onProgressUpdate(JSONObject... values) {
		super.onProgressUpdate(values);
		String title, desc, id, storyYear;
		try {
			id="-1";
			Log.i("json", values[0].toString());
			storyYear = values[0].getJSONObject("time").getString("datetime").split("-")[0]; 
			title = values[0].getString("headline");
			desc = values[0].getString("text");
			Story s = new Story(Integer.parseInt(storyYear), title, desc, id);
			FinalFunctionsUtilities.stories.add(s);
		} catch (JSONException e) {
			Log.e(GetPublicStoriesTask.class.getName(), e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(GetPublicStoriesTask.class.getName(), e.toString());
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		caller.OnFinish(result);
	}

	@Override
	public int compareTo(GetPublicStoriesTask another) {
		if (this.year > another.year)
			return -1;
		else if (this.year == another.year)
			return 0;
		return 1;
	}
}