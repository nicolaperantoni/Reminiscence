package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class GetSuggLuogoNascita extends AsyncTask<String, Void, Boolean> {

	private OnTaskFinished caller; 
	private Exception ex;
	private JSONObject json;

	public GetSuggLuogoNascita(OnTaskFinished caller) {
		this.caller = caller;
	}

	@Override
	protected Boolean doInBackground(String... arg0) { 

		if (arg0.length < 1) {
			throw new IllegalStateException("You should pass at least 1 params");
		}
		String url = "https://www.dandelion.eu/api/v1/datagem/26/data.json?$order=&$limit=5&$offset=0&$where=istarts_with(municipality,'";
		url =url + arg0[0] + "')";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url); //find link from luca
		json = new JSONObject();
		String jsonString;
		try {
			jsonString = EntityUtils.toString(client.execute(post).getEntity());
			JSONArray arr = new JSONArray(jsonString);
			for(int i=0;i<arr.length();i++){
				JSONObject a = arr.getJSONObject(i);
				json.put("mun"+i, a.getString("municipality"));
			}
			return true;

		} catch (Exception e) {
			Log.e("ii","asddsa"); 
			this.ex = e;
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		caller.onTaskFinished(json);
	}
}
