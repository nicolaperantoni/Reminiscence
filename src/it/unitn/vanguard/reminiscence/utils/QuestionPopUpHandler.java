package it.unitn.vanguard.reminiscence.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class QuestionPopUpHandler extends Handler {
	
	public interface  QuestionPopUp{
		public void OnShow(String question);
		public void OnHide();
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
		try {
			Thread.sleep(10000);
			listener.OnHide();
			//TODO retrieve the new question from the server
			this.sendMessageDelayed(msg, 10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
