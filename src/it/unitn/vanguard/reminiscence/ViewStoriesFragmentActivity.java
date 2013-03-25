package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.frags.EmptyStoryFragment;
import eu.giovannidefrancesco.DroidTimeline.view.TimeLineView;
import eu.giovannidefrancesco.DroidTimeline.widget.HorizontalListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ViewStoriesFragmentActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_viewstories);
		
		mViewPager = (ViewPager) findViewById(R.id.viewstories_pager);
		mViewPager.setAdapter(new StoriesAdapter(getSupportFragmentManager()));
		
	}
	
	private class StoriesAdapter extends FragmentPagerAdapter{

		private int mCount;

		public StoriesAdapter(FragmentManager fm) {
			super(fm);
			mCount=5;
		}

		@Override
		public Fragment getItem(int arg0) {
			EmptyStoryFragment  tmp=  new EmptyStoryFragment();
			Bundle b = new Bundle();
			b.putInt(EmptyStoryFragment.YEAR_PASSED_KEY, arg0);
			tmp.setArguments(b);
			return tmp;
		}

		@Override
		public int getCount() {
			return mCount;
		}
		
		
	}
	
}
