package it.unitn.vanguard.reminiscence;

import org.json.JSONObject;

import it.unitn.vanguard.reminiscence.asynctasks.UploadPhotoTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class PhotoUploaderService extends Service implements OnTaskFinished {

	public static final String PATHS_KEY = "imgstouplad";
	public static final String ID_STORY_KEY = "idStorytoupload";

	private NotificationManager mNM;
	private String[] imgs;
	private String idStoria;

	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		imgs = extras.getStringArray(PATHS_KEY);
		idStoria = extras.getString(ID_STORY_KEY);
		new UploadPhotoTask(this, Constants.imageType.STORY, imgs[0], this)
				.execute(idStoria);
		showNotification(0);
		imgs[0] = "-1";
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void showNotification(int n) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getApplicationContext());
		builder.setAutoCancel(true);
		builder.setContentTitle(String.format(
				getString(R.string.story_notification_upload_title), n,
				imgs.length));
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setProgress(n, imgs.length, false);
		builder.setDefaults(Notification.DEFAULT_LIGHTS
				| Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
		builder.setWhen(System.currentTimeMillis());
		if (mNM == null)
			mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNM.notify(1234, builder.build());
	}

	private void removeNotification() {
		mNM.cancel(1234);
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		boolean finished = false;
		for (int i = 0; i < imgs.length; i++) {
			if (i == imgs.length - 1)
				finished = true;
			if (!imgs[i].equals("-1")) {
				new UploadPhotoTask(this, Constants.imageType.STORY, imgs[i],
						this).execute(idStoria);
				showNotification(i);
				imgs[i] = "-1";
				break;
			}
		}
		// if (finished)
		// removeNotification();
	}
}
