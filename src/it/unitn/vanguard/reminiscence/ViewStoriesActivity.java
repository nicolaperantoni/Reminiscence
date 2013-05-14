package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.QuestionPopUpHandler.QuestionPopUp;
import it.unitn.vanguard.reminiscence.asynctasks.GetStoriesTask;
import it.unitn.vanguard.reminiscence.asynctasks.LogoutTask;
import it.unitn.vanguard.reminiscence.frags.StoryFragment;
import it.unitn.vanguard.reminiscence.interfaces.OnGetStoryTask;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;
import it.unitn.vanguard.reminiscence.utils.Constants;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;
import it.unitn.vanguard.reminiscence.utils.Story;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import eu.giovannidefrancesco.DroidTimeline.view.TimeLineView;
import eu.giovannidefrancesco.DroidTimeline.view.YearView;

public class ViewStoriesActivity extends BaseActivity implements
		OnTaskFinished, QuestionPopUp, OnGetStoryTask {

	private static final String SAVE_INDEX = "saved_index";

	private Context context;

	// private ViewPager mViewPager;
	private GridView mCards;
	private TimeLineView mTimeLine;
	private ProgressDialog dialog;

	private TextView mQuestionTv;
	private ImageView mCloseQuestionImgV;
	private StoriesAdapter mStoriesAdapter;
	private int selectedIndex;
	// private View selected;
	private int startYear;
	private int requestYear;

	@Override
	public void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(arg0);
		setContentView(R.layout.activity_viewstories);
		context = ViewStoriesActivity.this;

		String language = FinalFunctionsUtilities.getSharedPreferences(
				"language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);

		mCards = (GridView) findViewById(R.id.viewstroies_cards_gw);
		mTimeLine = (TimeLineView) findViewById(R.id.viewstories_tlv);

		mStoriesAdapter = new StoriesAdapter();
		mCards.setAdapter(mStoriesAdapter);

		// e' per avere lo 0 alla fine degli anni.(per avere l'intera decade,
		// praticmanete)
		String year = FinalFunctionsUtilities
				.getSharedPreferences("year", this);
		year = year.substring(0, year.length() - 1);
		requestYear = Integer.parseInt(year + '0');
		startYear = requestYear;
		
		YearView selected = (YearView) mTimeLine.getItemAtPosition(selectedIndex);
		selected.setBackgroundColor(getResources().getColor(
				R.color.pomegranate));
		
		if (arg0 != null){
			selectedIndex = arg0.getInt(SAVE_INDEX);
			requestYear = selected.getYear();
			mTimeLine.setStartYear(startYear);
		}
		else{
			selectedIndex = 0;
			mTimeLine.setStartYear(requestYear);
		}
		

		setListeners();

		initializePopUps();

		initializeStoryList();
	}

	private void initializeStoryList() {

		// Comincia a chiedere al server le storie
		if (FinalFunctionsUtilities.isDeviceConnected(context)) {
			new GetStoriesTask(this, requestYear).execute();
		} else {
			Toast.makeText(context, R.string.connection_fail, Toast.LENGTH_LONG)
					.show();
		}
	}

	private void initializePopUps() {
		Bundle b = new Bundle();
		b.putString(QuestionPopUpHandler.QUESTION_PASSED_KEY,
				"Sei mai andato in crociera?");
		Message msg = new Message();
		msg.setData(b);
		new QuestionPopUpHandler(this).sendMessageDelayed(msg,
				Constants.QUESTION_INTERVAL);
	}

	private void setListeners() {
		mTimeLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				View selected = (View) arg0.getItemAtPosition(selectedIndex);
				selected.setBackgroundColor(getResources().getColor(
						R.color.red_background_dark));
				selectedIndex = arg2;
				arg1.setBackgroundColor(getResources().getColor(
						R.color.pomegranate));
				requestYear = ((YearView) arg1).getYear();
				FinalFunctionsUtilities.stories.clear();
				mStoriesAdapter.notifyDataSetChanged();
				new GetStoriesTask(ViewStoriesActivity.this, requestYear)
						.execute();
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
				if (FinalFunctionsUtilities.isDeviceConnected(context)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ViewStoriesActivity.this);

					builder.setMessage(R.string.exit_message)
							.setPositiveButton(R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialogInterface,
												int id) {
											dialog = new ProgressDialog(
													ViewStoriesActivity.this);
											dialog.setTitle(getResources()
													.getString(R.string.please));
											dialog.setMessage(getResources()
													.getString(R.string.wait));
											dialog.setCancelable(false);
											dialog.show();

											String email = FinalFunctionsUtilities
													.getSharedPreferences(
															Constants.MAIL_KEY,
															ViewStoriesActivity.this);
											String password = FinalFunctionsUtilities
													.getSharedPreferences(
															Constants.PASSWORD_KEY,
															ViewStoriesActivity.this);

											new LogoutTask(
													ViewStoriesActivity.this)
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
					Toast.makeText(context,
							getResources().getString(R.string.connection_fail),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private class StoriesAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return FinalFunctionsUtilities.stories.size();
		}

		@Override
		public Object getItem(int arg0) {
			return FinalFunctionsUtilities.stories.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			View v = getLayoutInflater().inflate(R.layout.card_story, arg2,
					false);
			Story story = FinalFunctionsUtilities.stories.get(arg0);
			ImageView back = (ImageView) v.findViewById(R.id.cardstory_img);
			TextView title = (TextView) v.findViewById(R.id.cardstory_title);
			TextView desc = (TextView) v.findViewById(R.id.cardstory_desc);
			if (story != null) {
				if (story.getBackground() != null)
					back.setImageBitmap(FinalFunctionsUtilities.stories.get(
							arg0).getBackground());
				title.setText(story.getTitle());
				desc.setText(story.getDesc());
			}
			v.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View clicked) {
					Story story = FinalFunctionsUtilities.stories.get(arg0);
					StoryFragment sf = StoryFragment.newIstance(
							story.getTitle(), story.getDesc(),
							"" + story.getAnno());
					sf.show(getSupportFragmentManager(), "visualized");
				}
			});
			return v;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_story:
			Intent i = new Intent(this, EmptyStoryActivity.class);
			i.putExtra(EmptyStoryActivity.YEAR_PASSED_KEY, requestYear);
			startActivity(i);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		try {
			if (res.getString("success").equals("true")) {
				Toast.makeText(context,
						getResources().getString(R.string.logout_success),
						Toast.LENGTH_LONG).show();
				startActivity(new Intent(ViewStoriesActivity.this,
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
	public void OnStart() {
		setProgressBarIndeterminateVisibility(true);
	}

	@Override
	public void OnProgress() {
		mStoriesAdapter.notifyDataSetChanged();
	}

	@Override
	public void OnFinish(Boolean result) {
		// TODO display a toast in case of error
		setProgressBarIndeterminateVisibility(false);
		if (requestYear == startYear) {
			addBornStory();
		}
		View no_res = findViewById(R.id.no_result_tv);
		if (FinalFunctionsUtilities.stories.isEmpty()) {
			no_res.setVisibility(View.VISIBLE);
			mCards.setVisibility(View.INVISIBLE);
		} else {
			no_res.setVisibility(View.INVISIBLE);
			mCards.setVisibility(View.VISIBLE);
		}
		OnProgress();
	}

	private void addBornStory() {
		String title = getString(R.string.born_title);
		String desc = String.format(getString(R.string.born),
				FinalFunctionsUtilities.getSharedPreferences(
						Constants.LOUGO_DI_NASCITA_PREFERENCES_KEY,
						ViewStoriesActivity.this));
		Bitmap img = BitmapFactory.decodeResource(getResources(),
				R.drawable.baby);
		Story s = new Story(startYear, title, desc);
		s.setBackground(img);
		FinalFunctionsUtilities.stories.addFirst(s);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVE_INDEX, selectedIndex);
	}

}
