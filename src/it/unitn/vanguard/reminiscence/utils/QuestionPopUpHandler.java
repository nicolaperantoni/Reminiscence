package it.unitn.vanguard.reminiscence.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class QuestionPopUpHandler extends Handler {
	
	public interface  QuestionPopUp{
		public void OnShow(String question);
		public void OnHide();
	}
	
	private class HideHandler extends Handler{
		private QuestionPopUp listener;	
		public HideHandler(QuestionPopUp listener) {
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
			new HideHandler(listener).sendMessageDelayed(new Message(), 10000);
			//TODO retrieve the new question from the server
			Message newmsg = new Message();
			newmsg.setData(datas);
			this.sendMessageDelayed(newmsg, 50000);
	}
	
}
