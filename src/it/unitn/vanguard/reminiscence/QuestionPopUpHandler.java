package it.unitn.vanguard.reminiscence;

import it.unitn.vanguard.reminiscence.utils.Constants;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class QuestionPopUpHandler extends Handler {
	
	public interface  QuestionPopUp{
		public void OnShow(String question);
		public void OnHide();
	}
	
	private class PopUpHideHandler extends Handler{
		private QuestionPopUp listener;	
		public PopUpHideHandler(QuestionPopUp listener) {
			super();
			this.listener = listener;
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			listener.OnHide();
		}
		
	}
	
	public static final String QUESTION_PASSED_KEY="question_msg";
	
	private QuestionPopUp listener;	
		
	public QuestionPopUpHandler(QuestionPopUp listener) {
		super();
		this.listener = listener;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Bundle datas = msg.getData();
		String question = datas.getString(QUESTION_PASSED_KEY);
		listener.OnShow(question);
			new PopUpHideHandler(listener).sendMessageDelayed(new Message(),
					Constants.QUESTION_POPUP_SHOWING_TIME);
			//TODO retrieve the new question from the server
			Message newmsg = new Message();
			newmsg.setData(datas);
			this.sendMessageDelayed(newmsg, Constants.QUESTION_INTERVAL);
	}
	
}
