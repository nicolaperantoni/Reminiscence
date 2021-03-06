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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class GetPrivateStoriesTask extends AsyncTask<Integer, JSONObject, Boolean> 
	implements Comparable<GetPrivateStoriesTask>{

	private OnGetStoryTask caller;
	private int year;

	public GetPrivateStoriesTask(OnGetStoryTask caller, Integer year) {
		this.caller = caller;
		if (year != null) {
			this.year = year;
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		caller.OnStart();
	}

	@Override
	protected Boolean doInBackground(Integer... arg) {
		
		if (arg.length > 0) {
			if (arg[0] != null) {
				this.year = arg[0];
			}
			else {
				throw new IllegalStateException("You should provide a year");
			}
		}

		String token = FinalFunctionsUtilities.getSharedPreferences(Constants.TOKEN_KEY, ((Activity) caller)
				.getApplicationContext());

		if (!token.equals("")) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("initdecade", "" + year));
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
					JSONObject story = new JSONObject(json.get("s" + i).toString());
					publishProgress(story);
				}
				return true;
			} catch (Exception e) {
				Log.e(GetPrivateStoriesTask.class.getName(), e.toString() + "asdada");
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	protected void onProgressUpdate(JSONObject... values) {
		super.onProgressUpdate(values);
		String id, title, desc, storyYear;
		try {
			storyYear = values[0].getString("Year");
			id = values[0].getString("IdStory");
			title = values[0].getString("Title");
			desc = values[0].getString("Text");
			FinalFunctionsUtilities.stories.add(new Story(Integer.parseInt(storyYear) , title, desc, id));
		} catch (JSONException e) {
			Log.e(GetPrivateStoriesTask.class.getName(), e.toString());
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		caller.OnFinish(result);
	}

	@Override
	public int compareTo(GetPrivateStoriesTask another) {
		if (this.year>another.year)
			return -1;
		else if(this.year==another.year)
			return 0;
		return 1;
	}
}