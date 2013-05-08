package it.unitn.vanguard.reminiscence.frags;


import it.unitn.vanguard.reminiscence.CheckBoxAmici;
import it.unitn.vanguard.reminiscence.interfaces.OnTaskFinished;

import org.json.JSONObject;

import android.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BornFragment extends Fragment implements OnTaskFinished {
	
	public static final String BORN_CITY_PASSED_KEY="ussioforaa";
	private TextView mTv;
	private Button btn_aiuto_amico;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_born, container,false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		btn_aiuto_amico = (Button) getView().findViewById(R.id.btn_aiuto_amico);
		btn_aiuto_amico.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent checkbox_amici = new Intent(getActivity(), CheckBoxAmici.class );
				startActivity(checkbox_amici);
				
				
			}
		});
		mTv = (TextView) getView().findViewById(R.id.born_bornTv);
		Bundle b = getArguments();
		if(b!=null)
			mTv.setText(String.format(getString(R.string.born), b.getString(BORN_CITY_PASSED_KEY)));
		else{
			mTv.setText(mTv.getText().toString().substring(0, 5));
		}
	}

	@Override
	public void onTaskFinished(JSONObject res) {
		mTv.setText(mTv.getText().toString().substring(0, 5));	
	}
	
	
	
}
