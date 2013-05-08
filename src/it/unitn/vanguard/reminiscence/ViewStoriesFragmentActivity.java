package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.QuestionPopUpHandler.QuestionPopUp;
import it.unitn.vanguard.reminiscence.asynctasks.GetStoriesTask;
import it.unitn.vanguard.reminiscence.asynctasks.LogoutTask;
import it.unitn.vanguard.reminiscence.frags.BornFragment;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskExecuted;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;
import java.util.PriorityQueue;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import eu.giovannidefrancesco.DroidTimeline.view.TimeLineView;
import eu.giovannidefrancesco.DroidTimeline.view.YearView;



public class ViewStoriesFragmentActivity extends BaseActivity implements
		OnTaskFinished, QuestionPopUp, OnTaskExecuted {

	private Context context;

	private ViewPager mViewPager;
	private TimeLineView mTimeLine;
	private ProgressDialog dialog;

	private TextView mQuestionTv;
	private ImageView mCloseQuestionImgV;

	private StoriesAdapter mStoriesAdapter;

	private int initialYear;

	private PriorityQueue<GetStoriesTask> requests;

	@Override
	public void onCreate(Bundle arg0) {

		super.onCreate(arg0);

		setContentView(R.layout.activity_viewstories);

		context = getApplicationContext();
		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);

		setContentView(R.layout.activity_viewstories);

		mViewPager = (ViewPager) findViewById(R.id.viewstories_pager);
		mTimeLine = (TimeLineView) findViewById(R.id.viewstories_tlv);
		requests = new PriorityQueue<GetStoriesTask>();

		FragmentManager fm = getSupportFragmentManager();
		mStoriesAdapter = new StoriesAdapter(fm);
		mViewPager.setAdapter(mStoriesAdapter);
		
		// e' per avere lo 0 alla fine degli anni.(per avere l'intera decade, praticmanete)
		String year = FinalFunctionsUtilities
				.getSharedPreferences("year", this);
		year = year.substring(0, year.length() - 1);
		initialYear = Integer.parseInt(year + '0');
		mTimeLine.setStartYear(initialYear);

		setListeners();

		initializePopUps();
		
		initializeStoryList();
		
	}
	
	private void initializeStoryList() {
		if (FinalFunctionsUtilities.stories.isEmpty()) {
			Fragment f = new BornFragment();
			Bundle b = new Bundle();
			b.putString(BornFragment.BORN_CITY_PASSED_KEY,
					FinalFunctionsUtilities.getSharedPreferences(
							Constants.LOUGO_DI_NASCITA_PREFERENCES_KEY,
							ViewStoriesFragmentActivity.this));
			f.setArguments(b);
			FinalFunctionsUtilities.stories.add(f);
		}
	}

	private void initializePopUps(){
		Bundle b = new Bundle();
		b.putString(QuestionPopUpHandler.QUESTION_PASSED_KEY,
				"Sei mai andato in crociera?");
		Message msg = new Message();
		msg.setData(b);
		new QuestionPopUpHandler(this).sendMessageDelayed(msg,
				Constants.QUESTION_INTERVAL);
		new GetStoriesTask(this, initialYear).execute(initialYear);
	}
	
	private void setListeners() {
		mTimeLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int year = ((YearView) arg1).getYear();
				requests.add(new GetStoriesTask(
						ViewStoriesFragmentActivity.this, year));
				// initialYear=year;
			}
		});

		mQuestionTv = (TextView) findViewById(R.id.viewtories_addstory_hint);
		mQuestionTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				OnHide();
			}
		});

		mCloseQuestionImgV = (ImageView) findViewById(R.id.viewstories_addstory_hint_close);
		mCloseQuestionImgV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				OnHide();
			}
		});
		mLogout = (TextView) findViewById(R.id.hiddebmenu_logout_tv);
		mLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FinalFunctionsUtilities
						.isDeviceConnected(getApplicationContext())) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ViewStoriesFragmentActivity.this);

					builder.setMessage(R.string.exit_message)
							.setPositiveButton(R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialogInterface,
												int id) {
											dialog = new ProgressDialog(
													ViewStoriesFragmentActivity.this);
											dialog.setTitle(getResources()
													.getString(R.string.please));
											dialog.setMessage(getResources()
													.getString(R.string.wait));
											dialog.setCancelable(false);
											dialog.show();

											String email = FinalFunctionsUtilities
													.getSharedPreferences(
															Constants.MAIL_KEY,
															ViewStoriesFragmentActivity.this);
											String password = FinalFunctionsUtilities
													.getSharedPreferences(
															Constants.PASSWORD_KEY,
															ViewStoriesFragmentActivity.this);

											new LogoutTask(
													ViewStoriesFragmentActivity.this)
													.execute(email, password);
										}
									})
							.setNegativeButton(R.string.no,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

										}
									});

					AlertDialog alert = builder.create();
					alert.show();
					((TextView) alert.findViewById(android.R.id.message))
							.setGravity(Gravity.CENTER);
					((Button) alert.getButton(AlertDialog.BUTTON_POSITIVE))
							.setBackgroundResource(R.drawable.bottone_logout);
					((Button) alert.getButton(AlertDialog.BUTTON_POSITIVE))
							.setTextColor(Color.WHITE);

				} else {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.connection_fail),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private class StoriesAdapter extends FragmentPagerAdapter {

		public StoriesAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return FinalFunctionsUtilities.stories.get(arg0);
		}

		@Override
		public int getCount() {
			return FinalFunctionsUtilities.stories.size();
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", getApplicationContext());
		Locale locale = new Locale(language);

		if (locale.toString().equals(Locale.ITALIAN.getLanguage())
				|| locale.toString().equals(locale.ITALY.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.it);
		} else if (locale.toString().equals(Locale.ENGLISH.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.en);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Locale locale = null;

		switch (item.getItemId()) {
		// Languages
		case R.id.action_language_it: {
			locale = Locale.ITALY;
			break;
		}
		case R.id.action_language_en: {
			locale = Locale.ENGLISH;
			break;
		}

		}

		if (locale != null
				&& FinalFunctionsUtilities.switchLanguage(locale, context)) {
			// Refresh activity
			finish();
			startActivity(getIntent());
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTaskFinished(JSONObject res) {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		try {
			Log.e("", res.toString());
			if (res.getString("success").equals("true")) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.logout_success),
						Toast.LENGTH_LONG).show();
				startActivity(new Intent(ViewStoriesFragmentActivity.this,
						LoginActivity.class));
				this.finish();
			} else {
				Toast.makeText(this,
						getResources().getString(R.string.logout_failed),
						Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			Log.e(LoginActivity.class.getName(), e.toString());
		}
	}

	@Override
	public void OnShow(String question) {
		togglePopup(true);
		mQuestionTv.setText(question);
	}

	@Override
	public void OnHide() {
		togglePopup(false);
	}

	private void togglePopup(boolean visibility) {
		if (!visibility) {
			mQuestionTv.setVisibility(View.GONE);
			mCloseQuestionImgV.setVisibility(View.GONE);
		} else {
			mQuestionTv.setVisibility(View.VISIBLE);
			mCloseQuestionImgV.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void OnFinish(Boolean result) {
		// TODO display a toast in case of error
		// TODO call it for the the next year
		if (!requests.isEmpty())
			requests.remove().execute();
		else {
			initialYear++;
			new GetStoriesTask(this, initialYear).execute();
		}
	}

	@Override
	public void OnProgress() {
		mStoriesAdapter.notifyDataSetChanged();
	}
}
