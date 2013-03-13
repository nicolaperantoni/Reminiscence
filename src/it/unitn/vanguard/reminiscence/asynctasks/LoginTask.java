package it.unitn.vanguard.reminiscence.asynctasks;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<String, Void, Boolean> {

	private OnTaskFinished caller;
	private Exception ex;
	
	public LoginTask(OnTaskFinished caller) {
		super();
		this.caller=caller;
	}
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		
		if(arg0.length<2) {
			throw new IllegalStateException("You should pass at least 2 params");
		}
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("email", arg0[0]));
		params.add(new BasicNameValuePair("password", arg0[1]));
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constants.SERVER_URL+"registrazione.php");
		JSONObject json = null;
		String content = null;
		
		try {
			content = client.execute(post).getEntity().getContent().toString();
			json = new JSONObject(content);
			Log.i(LoginTask.class.getName(), content);
		} catch (Exception e) {
			this.ex=e;
			return false;
		}
		
		String retval;
		try {
			retval = json.getString("success");
		} catch (JSONException e) {
			retval = null;
		}
		
		return (retval=="true")?true:false;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(!result && ex!=null)
			Log.e(RegistrationTask.class.getName(), ex.toString());
		caller.onTaskFinished(result);
	}
	
}
