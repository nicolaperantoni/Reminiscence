package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;


public class RegistrationTask extends AsyncTask<String, Void, Boolean> {
	
	private OnTaskFinished caller;
	private Exception ex;
	
	

	public RegistrationTask(OnTaskFinished caller) {
		super();
		this.caller = caller;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		
		if(arg0.length<7) {
			throw new IllegalStateException("You should pass at least 7 params");
		}
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(6);
		params.add(new BasicNameValuePair("nome", arg0[0]));
		params.add(new BasicNameValuePair("cognome", arg0[1]));
		params.add(new BasicNameValuePair("email", arg0[2]));
		params.add(new BasicNameValuePair("password", arg0[3]));
		params.add(new BasicNameValuePair("day", arg0[4]));
		params.add(new BasicNameValuePair("month", arg0[5]));
		params.add(new BasicNameValuePair("year", arg0[6]));
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constants.SERVER_URL+"registrazione.php");
		JSONObject json = null;
		String jsonString;
		try {
			jsonString = client.execute(post).getEntity().getContent().toString();
			json = new JSONObject(jsonString);
			Log.i(RegistrationTask.class.getName(), jsonString);
			
		} catch (Exception e) {
			this.ex=e;
			return false;
		}
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(!result && ex!=null)
			Log.e(RegistrationTask.class.getName(), ex.toString());
		caller.onTaskFinished(result);
	}
	
	

}
