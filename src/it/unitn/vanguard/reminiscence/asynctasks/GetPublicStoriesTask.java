package it.unitn.vanguard.reminiscence.asynctasks;

import java.util.ArrayList;

import it.unitn.vanguard.reminiscence.interfaces.OnGetStoryTask;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import it.unitn.vanguard.reminiscence.utils.Story;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class GetPublicStoriesTask extends
		AsyncTask<Integer, JSONObject, Boolean> implements
		Comparable<GetPublicStoriesTask> {

	private OnGetStoryTask caller;
	private Exception ex;
	private JSONObject json;
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
			else
				throw new IllegalStateException("You should provide a year");
		}
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);

		params.add(new BasicNameValuePair("initdecade", "" + year));

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
				JSONObject story = new JSONObject(json.get("s" + i).toString());
				publishProgress(story);
			}
			return true;
		} catch (Exception e) {
			ex = e;
		}

		return false;
	}

	@Override
	protected void onProgressUpdate(JSONObject... values) {
		super.onProgressUpdate(values);
		String title;
		try {
			title = values[0].getString("Title");
			String desc = values[0].getString("Text");
			FinalFunctionsUtilities.stories.add(new Story(year, title, desc));
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
	public int compareTo(GetPublicStoriesTask another) {
		if (this.year > another.year)
			return -1;
		else if (this.year == another.year)
			return 0;
		return 1;
	}

}
