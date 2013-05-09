package it.unitn.vanguard.reminiscence.interfaces;

import com.fima.cardsui.objects.Card;

public interface OnGetStoryTask {
	public void OnStart();
	public void OnFinish(Boolean result);
	public void OnProgress(Card card);
}
