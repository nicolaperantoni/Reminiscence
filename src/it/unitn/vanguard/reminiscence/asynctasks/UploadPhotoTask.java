package it.unitn.vanguard.reminiscence.asynctasks;

import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.Constants.imageType;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UploadPhotoTask extends AsyncTask<String, Void, Boolean> implements Comparable<UploadPhotoTask> {

	private OnTaskFinished caller;
	private imageType imageType;
	private Context context;
	private Exception ex;
	private JSONObject json;
	private String image;

	public UploadPhotoTask(OnTaskFinished caller, 
			Constants.imageType imageType,String image, Context context) {
		super();
		this.caller = caller;
		this.imageType = imageType;
		this.context = context;
		this.image=image;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		
		String token = FinalFunctionsUtilities.getSharedPreferences(Constants.TOKEN_KEY, context);
		
		if (!token.equals("")) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("image", image));
			params.add(new BasicNameValuePair("token", token ));
			
			String serverPath;
			if(imageType == Constants.imageType.PROFILE) { serverPath = "addProfileImage.php"; }
			else {
				serverPath = "addImage.php";
				params.add(new BasicNameValuePair("story_id", arg0[0]));
			}
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constants.SERVER_URL + serverPath);
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException e1) {
				Log.e(UploadPhotoTask.class.getName(), e1.toString());
				e1.printStackTrace();
			}
			json = null;
			String jsonString;
			try {
				jsonString = EntityUtils.toString(client.execute(post).getEntity());
				json = new JSONObject(jsonString);
				if (json != null && json.getString("success").equals("true")) {
					return true;
				}
			} catch (Exception e) {
				Log.e(UploadPhotoTask.class.getName(), e.toString());
				e.printStackTrace();
				this.ex = e;
				return false;
			}
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (!result && ex != null) {
			Log.e(RegistrationTask.class.getName(), ex.toString());
		}
		caller.onTaskFinished(json);
	}

	@Override
	public int compareTo(UploadPhotoTask another) {
		return 0;
	}
}