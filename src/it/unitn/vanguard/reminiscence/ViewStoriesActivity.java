package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.QuestionPopUpHandler.QuestionPopUp;
import it.unitn.vanguard.reminiscence.asynctasks.DeleteStoryTask;
import it.unitn.vanguard.reminiscence.asynctasks.GetPrivateStoriesTask;
import it.unitn.vanguard.reminiscence.asynctasks.GetPublicStoriesTask;
import it.unitn.vanguard.reminiscence.asynctasks.GetStoryCoverTask;
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

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	public static final int ADD_STORY_CODE = 100;

	private Context context;

	// private ViewPager mViewPager;
	private GridView mCards;
	private TimeLineView mTimeLine;
	private ProgressDialog dialog;
	private ActionBar actionBar;

	private TextView mQuestionTv;
	private ImageView mCloseQuestionImgV;
	private StoriesAdapter mStoriesAdapter;
	private int selectedIndex;

	// private YearView selected;
	private int startYear;
	private int requestYear;

	private YearView lastSelected;

	private Bundle bundle;
	private String[] questions;
	private TextView mNo_res_tv;

	@Override
	public void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(arg0);
		context = ViewStoriesActivity.this;
		bundle = arg0;

		String language = FinalFunctionsUtilities.getSharedPreferences(
				Constants.LANGUAGE_KEY, context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_viewstories);

		mCards = (GridView) findViewById(R.id.viewstroies_cards_gw);
		mTimeLine = (TimeLineView) findViewById(R.id.viewstories_tlv);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getApplicationContext(), R.array.stories_dropdown,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		questions = getResources().getStringArray(R.array.questions);

		mStoriesAdapter = new StoriesAdapter();
		mCards.setAdapter(mStoriesAdapter);

		// E' per avere lo 0 alla fine degli anni.(per avere l'intera decade,
		// praticamente)
		String year = FinalFunctionsUtilities.getSharedPreferences(
				Constants.YEAR_KEY, this);
		year = year.substring(0, year.length() - 1);

		// HOTFIX
		try {
			requestYear = Integer.parseInt(year + '0');
		} catch (Exception ex) {
			requestYear = 1920;
		}

		startYear = requestYear;
		mTimeLine.setStartYear(startYear);

		if (bundle != null) {
			selectedIndex = bundle.getInt(SAVE_INDEX);
			FinalFunctionsUtilities.stories.clear();
			mStoriesAdapter.notifyDataSetChanged();
		} else {
			selectedIndex = 0;
		}

		// Cambio il colore dell'anno corrente
		YearView selected = (YearView) mTimeLine.getAdapter().getView(
				selectedIndex, null, mTimeLine);
		selected.setBackgroundColor(getResources()
				.getColor(R.color.pomegranate));

		requestYear = selected.getYear();
		lastSelected = selected;

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(adapter,
				new ActionBar.OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {

						if (FinalFunctionsUtilities.isDeviceConnected(context)) {
							FinalFunctionsUtilities.stories.clear();

							YearView selected = (YearView) mTimeLine
									.getAdapter().getView(selectedIndex, null,
											mTimeLine);
							selected.setBackgroundColor(getResources()
									.getColor(R.color.pomegranate));

							switch (itemPosition) {
							case 0: {
								switchActiveStories(Constants.PRIVATE_STORIES);
								break;
							}
							case 1: {
								switchActiveStories(Constants.PUBLIC_STORIES);
								break;
							}
							}
						} else {
							Toast.makeText(context, R.string.connection_fail,
									Toast.LENGTH_LONG).show();
						}
						return true;
					}
				});
		setListeners();
		initializePopUps();
	}

	private void initializePopUps() {
		Bundle b = new Bundle();
		int index = (int) (Math.random() * questions.length);
		b.putString(QuestionPopUpHandler.QUESTION_PASSED_KEY, questions[index]);
		Message msg = new Message();
		msg.setData(b);
		new QuestionPopUpHandler(this).sendMessageDelayed(msg,
				Constants.QUESTION_INTERVAL);
	}

	private void switchActiveStories(String willActive) {

		// Richiedo il tipo di storie selezionato solo se quelle
		// attuali erano diverse..
		if (willActive.equals(Constants.PRIVATE_STORIES)) {
			new GetPrivateStoriesTask(ViewStoriesActivity.this, requestYear)
					.execute();
		} else if (willActive.equals(Constants.PUBLIC_STORIES)) {
			new GetPublicStoriesTask(ViewStoriesActivity.this, requestYear)
					.execute();
		}

		// Aggiorno il tipo di storie visualizzate
		FinalFunctionsUtilities.setSharedPreferences(Constants.ACTIVE_STORIES,
				willActive, context);
	}

	private void setListeners() {

		mTimeLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				lastSelected.setBackgroundColor(getResources().getColor(
						R.color.red_background_dark));
				lastSelected = (YearView) arg1;

				((YearView) arg1).setBackgroundColor(getResources().getColor(
						R.color.pomegranate));

				// aggiorno gli indici
				selectedIndex = arg2;
				requestYear = ((YearView) arg1).getYear();
				// tolgo le vecchie e chiedo le nuove
				FinalFunctionsUtilities.stories.clear();
				mStoriesAdapter.notifyDataSetChanged();

				if (FinalFunctionsUtilities.isDeviceConnected(context)) {
					// A seconda del tipo di storie selezionate chiedo al server
					// le storie pubbliche o private
					String activeStories = FinalFunctionsUtilities
							.getSharedPreferences(Constants.ACTIVE_STORIES,
									context);
					if (activeStories.equals(Constants.PRIVATE_STORIES)) {
						try {
							new GetPrivateStoriesTask(ViewStoriesActivity.this,
									requestYear).execute();
						} catch (Exception e) {
							Log.e(ViewStoriesActivity.class.getName(),
									e.toString());
							e.printStackTrace();
						}
					} else {
						try {
							new GetPublicStoriesTask(ViewStoriesActivity.this,
									requestYear).execute();
						} catch (Exception e) {
							Log.e(ViewStoriesActivity.class.getName(),
									e.toString());
							e.printStackTrace();
						}
					}
				} else {
					Toast.makeText(context,
							getResources().getString(R.string.connection_fail),
							Toast.LENGTH_LONG).show();
				}
			}
		});

		mQuestionTv = (TextView) findViewById(R.id.viewtories_addstory_hint);
		mQuestionTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewStoriesActivity.this,
						EmptyStoryActivity.class);
				intent.putExtra(EmptyStoryActivity.YEAR_PASSED_KEY, requestYear);
				startActivityForResult(intent, ADD_STORY_CODE);
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

				AlertDialog.Builder builder = new AlertDialog.Builder(context);

				builder.setMessage(R.string.exit_message)
						.setPositiveButton(R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialogInterface,
											int id) {

										if (FinalFunctionsUtilities
												.isDeviceConnected(context)) {
											dialog = new ProgressDialog(context);
											dialog.setTitle(getResources()
													.getString(R.string.please));
											dialog.setMessage(getResources()
													.getString(R.string.wait));
											dialog.setCancelable(false);
											dialog.show();

											String email = FinalFunctionsUtilities
													.getSharedPreferences(
															Constants.MAIL_KEY,
															context);
											String password = FinalFunctionsUtilities
													.getSharedPreferences(
															Constants.PASSWORD_KEY,
															context);

											try {
												new LogoutTask(
														ViewStoriesActivity.this)
														.execute(email,
																password);
											} catch (Exception e) {
												Log.e(ViewStoriesActivity.class
														.getName(), e
														.toString());
												e.printStackTrace();
											}
										} else {
											Toast.makeText(
													context,
													getResources()
															.getString(
																	R.string.connection_fail),
													Toast.LENGTH_LONG).show();
										}
									}
								})
						.setNegativeButton(R.string.no,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
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
			}
		});
		mNo_res_tv = (TextView) findViewById(R.id.no_result_tv);
		mNo_res_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (actionBar.getSelectedNavigationIndex() == 0) {
					Intent intent = new Intent(ViewStoriesActivity.this,
							EmptyStoryActivity.class);
					intent.putExtra(EmptyStoryActivity.YEAR_PASSED_KEY,
							requestYear);
					startActivityForResult(intent, ADD_STORY_CODE);
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
			View v;
			ImageView back=null;
			if (actionBar.getSelectedNavigationIndex() == 1) {
				v = getLayoutInflater().inflate(R.layout.card_story_pub, arg2,
						false);
			} else {
				v = getLayoutInflater().inflate(R.layout.card_story, arg2,
						false);
				back = (ImageView) v.findViewById(R.id.card_story_img);
			}

			// lancio il task per farmi ritornare il jason con le immagini

			String token = FinalFunctionsUtilities.getSharedPreferences(
					Constants.TOKEN_KEY, context);

			TextView title = (TextView) v.findViewById(R.id.cardstory_title);
			TextView desc = (TextView) v.findViewById(R.id.cardstory_desc);
			TextView year = (TextView) v.findViewById(R.id.yearStoryCard);

			if (FinalFunctionsUtilities.stories.size() != 0) {
				Story story = FinalFunctionsUtilities.stories.get(arg0);
				// if (FinalFunctionsUtilities.isDeviceConnected(context)) {
				// new GetStoryCoverTask(ViewStoriesActivity.this, context)
				// .execute(token, story.getId());
				// }

				if (story != null) {
					title.setText(story.getTitle());
					desc.setText(story.getDesc());
					year.setText(String.valueOf(story.getAnno()));
					if (story.getBackground() != null) {
						if (back != null)
							back.setImageBitmap(story.getBackground());
					}
				}
			}

			v.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View clicked) {
					if (FinalFunctionsUtilities.stories.size() != 0) {
						Story story = FinalFunctionsUtilities.stories.get(arg0);
						StoryFragment sf = StoryFragment.newInstance(story);
						sf.show(getFragmentManager(), "visualized");
					}
				}
			});

			v.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View longClicked) {

					final int pos = arg0;

					if (FinalFunctionsUtilities.stories.size() != 0
							&& !FinalFunctionsUtilities.stories.get(arg0)
									.getId().equals("-1")) {

						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);

						String delStory = "";
						delStory += getResources().getString(
								R.string.deleteStoryPopupMessage1);
						delStory += "\n\n\""
								+ FinalFunctionsUtilities.stories.get(pos)
										.getTitle() + "\"\n\n";
						delStory += getResources().getString(
								R.string.deleteStoryPopupMessage2);

						builder.setMessage(delStory)
								.setPositiveButton(R.string.yes,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialogInterface,
													int id) {

												if (FinalFunctionsUtilities
														.isDeviceConnected(context)) {
													dialog = new ProgressDialog(
															context);
													dialog.setTitle(getResources()
															.getString(
																	R.string.please));
													dialog.setMessage(getResources()
															.getString(
																	R.string.wait));
													dialog.setCancelable(false);
													dialog.show();
													deleteStory(arg0);
												} else {
													Toast.makeText(
															context,
															getResources()
																	.getString(
																			R.string.connection_fail),
															Toast.LENGTH_LONG)
															.show();
												}
											}
										})
								.setNegativeButton(R.string.no,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
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
					}
					return true;
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
			Intent intent = new Intent(this, EmptyStoryActivity.class);
			intent.putExtra(EmptyStoryActivity.YEAR_PASSED_KEY, requestYear);
			startActivityForResult(intent, ADD_STORY_CODE);
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
				if (res.getString("Operation").equals("Logout")) {
					Toast.makeText(context,
							getResources().getString(R.string.logout_success),
							Toast.LENGTH_LONG).show();

					FinalFunctionsUtilities.clearSharedPreferences(context);
					startActivity(new Intent(context, LoginActivity.class));
					this.finish();
				} else if (res.get("Operation").equals("delStory")) {
					Toast.makeText(
							context,
							getResources()
									.getString(R.string.deleteStorySucces),
							Toast.LENGTH_LONG).show();

					mStoriesAdapter.notifyDataSetChanged();
					if (FinalFunctionsUtilities.stories.isEmpty()) {
						mCards.setVisibility(View.INVISIBLE);
						int index = (int) (Math.random() * questions.length);
						mNo_res_tv.setText(questions[index]);

						// test
						if (selectedIndex == 1980)
							mNo_res_tv.setText("When did you graduate?");
						mNo_res_tv.setVisibility(View.VISIBLE);
					} else {
						mNo_res_tv.setVisibility(View.GONE);
						mCards.setVisibility(View.VISIBLE);
					}
				} else if (res.getString("Operation").equals("GetStoryCover")
						&& !res.getString("numImages").equals("0")) {

					// Inserisco la cover della storia..
					String id = res.getString("story_id");
					Story s = null;
					for (int i = 0; i < FinalFunctionsUtilities.stories.size(); i++) {
						if (id.equals(FinalFunctionsUtilities.stories.get(i)
								.getId())) {
							s = FinalFunctionsUtilities.stories.get(i);
						}
					}
					if (s != null) {
						try {

							// Converto l'immagine da Base64 a Bitmap e la
							// inserisco nell' imageView della StoryCard
							// (COVER)..
							byte[] decodedString = Base64.decode(
									res.getString("cover"), Base64.DEFAULT);
							Bitmap bitmap = BitmapFactory.decodeByteArray(
									decodedString, 0, decodedString.length);

							s.setBackground(bitmap);
							s.setNumImages(1 + "");
							Log.e("imagg", res.getString("cover"));
							mStoriesAdapter.notifyDataSetChanged();

						} catch (Exception e) {
							Log.e(ViewStoriesActivity.class.getName(),
									e.toString());
							e.printStackTrace();
							Toast.makeText(context, "Cover non caricata",
									Toast.LENGTH_LONG).show();
						} catch (OutOfMemoryError e) {
							Log.e(ViewStoriesActivity.class.getName(),
									e.toString());
							e.printStackTrace();
							Toast.makeText(context, "Cover non caricata",
									Toast.LENGTH_LONG).show();
						}
					}
				}
			} else {

				String toastText = "";

				if (res.getString("Operation").equals("Logout")) {
					toastText = getResources()
							.getString(R.string.logout_failed);
				} else if (res.get("Operation").equals("DelStory")) {
					toastText = getResources().getString(
							R.string.deleteStoryFail);
				} else if (res.getString("Operation").equals("GetStoryCover")
						&& !res.getString("numImages").equals("0")) {
					toastText = "Error";
				}
				Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
			}
		} catch (JSONException jsonEx) {
			Log.e(ViewStoriesActivity.class.getName(), jsonEx.toString());
			jsonEx.printStackTrace();
		} catch (Exception e) {
			Log.e(ViewStoriesActivity.class.getName(), e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void OnShow(String question) {
		mQuestionTv.setText(question);
		togglePopup(true);
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

		setProgressBarIndeterminateVisibility(false);
		// Se sono nell'anno di nascita e sto visualizzando le storie private
		// mostro la BornStory come prima storia..
		if ((requestYear == startYear)
				&& (FinalFunctionsUtilities.getSharedPreferences(
						Constants.ACTIVE_STORIES, context)
						.equals(Constants.PRIVATE_STORIES))) {
			addBornStory();
		}

		if (FinalFunctionsUtilities.stories.isEmpty()) {
			mCards.setVisibility(View.INVISIBLE);
			int index = (int) (Math.random() * questions.length);
			if (actionBar.getSelectedNavigationIndex() == 0)
				mNo_res_tv.setText(questions[index]);
			else
				mNo_res_tv.setText(getString(R.string.no_stories));
			// test
			if (selectedIndex == 1980)
				mNo_res_tv.setText("When did you graduate?");
			mNo_res_tv.setVisibility(View.VISIBLE);

		} else {
			mNo_res_tv.setVisibility(View.GONE);
			mCards.setVisibility(View.VISIBLE);
		}

		// Per ciascuna storia lancio un task per ottenere la relativa cover..
		String token = FinalFunctionsUtilities.getSharedPreferences(
				Constants.TOKEN_KEY, context);
		if (FinalFunctionsUtilities.isDeviceConnected(this)) {
			for (int i = 0; i < FinalFunctionsUtilities.stories.size(); i++) {
				try {
					new GetStoryCoverTask(ViewStoriesActivity.this,
							ViewStoriesActivity.this)
							.execute(token, FinalFunctionsUtilities.stories
									.get(i).getId() + "");
				} catch (Exception e) {
					Log.e(ViewStoriesActivity.class.getName(), e.toString());
					e.printStackTrace();
				}
			}
		}
		mStoriesAdapter.notifyDataSetChanged();
		OnProgress();
	}

	private void addBornStory() {
		String title = getString(R.string.born_title);
		String desc = String.format(getString(R.string.born),
				FinalFunctionsUtilities.getSharedPreferences(
						Constants.LOUGO_DI_NASCITA_PREFERENCES_KEY, context));
		Bitmap img = BitmapFactory.decodeResource(getResources(),
				R.drawable.baby);
		Story s = new Story(startYear, title, desc, "-1");
		s.setBackground(img);
		s.setNumImages(1 + "");
		FinalFunctionsUtilities.stories.addFirst(s);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVE_INDEX, selectedIndex);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_STORY_CODE) {
			if (resultCode == RESULT_OK) {
				if (data.hasExtra(EmptyStoryActivity.YEAR_PASSED_KEY)) {

					try {
						int year = Integer
								.parseInt(data
										.getStringExtra(EmptyStoryActivity.YEAR_PASSED_KEY));

						if (year >= requestYear && year <= requestYear + 10) {
							Story s = new Story(
									year,
									data.getStringExtra(EmptyStoryActivity.TITLE_PASSED_KEY),
									data.getStringExtra(EmptyStoryActivity.DESC_PASSED_KEY),
									data.getStringExtra(EmptyStoryActivity.ID_PASSED_KEY));

							FinalFunctionsUtilities.stories.add(s);
							mStoriesAdapter.notifyDataSetChanged();
							mNo_res_tv.setVisibility(View.GONE);
							mCards.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						Log.e(ViewStoriesActivity.class.getName(), e.toString());
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void storyDeleted(int position) {
		try {
			FinalFunctionsUtilities.stories.remove(position);
		} catch (Exception e) {
			Log.e(ViewStoriesActivity.class.getName(), e.toString());
			e.printStackTrace();
		}
	}

	private void deleteStory(int position) {
		Story story = FinalFunctionsUtilities.stories.get(position);
		if (FinalFunctionsUtilities.isDeviceConnected(context)) {
			if (dialog == null) {
				dialog = new ProgressDialog(context);
				dialog.setTitle(getResources().getString(R.string.please));
				dialog.setMessage(getResources().getString(R.string.wait));
				dialog.setCancelable(false);
				dialog.show();
			}
			try {
				new DeleteStoryTask(ViewStoriesActivity.this, position)
						.execute(story.getId());
			} catch (Exception e) {
				Log.e(ViewStoriesActivity.class.getName(), e.toString());
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context,
					getResources().getString(R.string.connection_fail),
					Toast.LENGTH_LONG).show();
		}
	}
}
