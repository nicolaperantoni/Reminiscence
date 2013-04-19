package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.frags.EmptyStoryFragment;
import it.unitn.vanguard.reminiscence.frags.StoryFragment;
import it.unitn.vanguard.reminiscence.utils.FinalFunctionsUtilities;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import eu.giovannidefrancesco.DroidTimeline.view.TimeLineView;
import eu.giovannidefrancesco.DroidTimeline.view.YearView;

public class ViewStoriesFragmentActivity extends FragmentActivity {

	private Context context;
	
	private ViewPager mViewPager;
	private TimeLineView mTimeLine;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = getApplicationContext();
		String language = FinalFunctionsUtilities.getSharedPreferences("language", context);
		FinalFunctionsUtilities.switchLanguage(new Locale(language), context);
		setContentView(R.layout.activity_viewstories);

		mViewPager = (ViewPager) findViewById(R.id.viewstories_pager);
		mTimeLine = (TimeLineView) findViewById(R.id.viewstories_tlv);

		FragmentManager fm = getSupportFragmentManager();
		final StoriesAdapter st = new StoriesAdapter(fm);
		st.setYear(mTimeLine.getStartYear());
		mViewPager.setAdapter(st);

		mTimeLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(
						ViewStoriesFragmentActivity.this,
						"Funzione non ancora disponibile!",
						Toast.LENGTH_SHORT).show();
				st.setYear(((YearView) arg1).getYear());
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Siamo spiacenti!");
		builder.setMessage("Questa schermata non e' ancora pronta,"
				+ "ma puoi comunque visualizzarla!");
		builder.setCancelable(true);
		builder.setNeutralButton("Capito!",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		builder.create().show();
	}

	private class StoriesAdapter extends FragmentPagerAdapter {

		private int mCount;
		private int mYear;

		public StoriesAdapter(FragmentManager fm) {
			super(fm);
			// TODO ServerRequest!
			mCount = 10;
		}

		public void setYear(int year) {
			this.mYear=year;
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment f = new EmptyStoryFragment();
			Bundle b = new Bundle();

			if (arg0 % 2 == 0) {
				f = new EmptyStoryFragment();
				b.putInt(EmptyStoryFragment.YEAR_PASSED_KEY, mYear+arg0);
			} else {
				f = new StoryFragment();
				b.putString(StoryFragment.TITLE_PASSED_KEY, getResources()
						.getString(R.string.dummy_story_title));
				b.putString(StoryFragment.DESCRIPTION_PASSED_KEY,
						getResources().getString(R.string.dummy_story_desc));
			}
			f.setArguments(b);
			return f;
		}

		@Override
		public int getCount() {
			return mCount;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		String language = FinalFunctionsUtilities.getSharedPreferences("language", getApplicationContext());
		Locale locale = new Locale(language);

		if(locale.toString().equals(Locale.ITALIAN.getLanguage()) || locale.toString().equals(locale.ITALY.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.it);
		}
		else if(locale.toString().equals(Locale.ENGLISH.getLanguage())) {
			menu.getItem(0).setIcon(R.drawable.en);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Locale locale = null;
		switch (item.getItemId()) {
		    case R.id.action_language_it: { locale = Locale.ITALY; break; }
		    case R.id.action_language_en: { locale = Locale.ENGLISH; break; }
	    }
		
		if(locale != null && FinalFunctionsUtilities.switchLanguage(locale, context)) {
		    // Refresh activity
		    finish();
		    startActivity(getIntent());
	    }
	    return true;
	}
}
