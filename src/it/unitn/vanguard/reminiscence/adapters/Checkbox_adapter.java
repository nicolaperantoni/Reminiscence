package it.unitn.vanguard.reminiscence.adapters;

import it.unitn.vanguard.reminiscence.R;
import it.unitn.vanguard.reminiscence.utils.Friend;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Checkbox_adapter extends ArrayAdapter<Friend> {

	public final Context context;
	public final Friend friend[];

	public Checkbox_adapter(Context context, Friend friend[]) {
		super(context, R.layout.help_friend_item, friend);
		this.context = context;
		this.friend = friend;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.help_friend_item,
				parent, false);
		
		TextView nameTextView = (TextView) rowView
				.findViewById(R.id.item_name);
		TextView surnameTextView = (TextView) rowView
				.findViewById(R.id.item_surname);
		TextView mailTextView = (TextView) rowView
				.findViewById(R.id.item_mail_tv);
		nameTextView.setText(friend[position].getName());
		surnameTextView.setText(friend[position].getSurname());
		mailTextView.setText(friend[position].getEmail());

		return rowView;
	}

}
