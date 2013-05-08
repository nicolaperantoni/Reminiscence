package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.ArrayList;

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
import android.widget.Toast;

public class AddFriendTask extends AsyncTask<String, Void, Boolean> {

	OnTaskFinished caller;
	JSONObject json = null;
	Exception ex;

	public AddFriendTask(OnTaskFinished caller) {
		super();
		this.caller = caller;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		String token = FinalFunctionsUtilities.getSharedPreferences("token",
				(Activity) caller);

		if (arg0.length != 3) {
			throw new IllegalStateException(
					"Error too few arguments passed in the AddFriendTask");
		}

		if (FinalFunctionsUtilities.isDeviceConnected((Activity) caller)) {

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(6);
			params.add(new BasicNameValuePair("nome", arg0[0]));
			params.add(new BasicNameValuePair("cognome", arg0[1]));
			params.add(new BasicNameValuePair("email", arg0[2]));
			params.add(new BasicNameValuePair("token", token));

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL
					+ "addFriends.php");

			try {
				post.setEntity(new UrlEncodedFormEntity(params));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String jsonstring;

			try {
				jsonstring = EntityUtils.toString(client.execute(post)
						.getEntity());
				json = new JSONObject(jsonstring);
			} catch (Exception e) {
				ex = e;
				return false;
			}

		} else {
			Toast.makeText((Activity) caller, R.string.connection_fail,
					Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		caller.onTaskFinished(json);
	}

}
