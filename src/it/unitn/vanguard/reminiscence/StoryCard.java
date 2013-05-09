package it.unitn.vanguard.reminiscence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class StoryCard extends Card {
	
	public TextView mTitle;
	public TextView mDesc;

	public StoryCard(String title, String desc) {
		super(title,desc);
	}
	
	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_story, null);
		mTitle = (TextView) view.findViewById(R.id.title);
		mDesc = (TextView) view.findViewById(R.id.description);
		return view;
	}
}
