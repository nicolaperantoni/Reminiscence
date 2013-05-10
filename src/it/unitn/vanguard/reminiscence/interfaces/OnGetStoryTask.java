package it.unitn.vanguard.reminiscence.interfaces;


public interface OnGetStoryTask {
	public void OnStart();
	public void OnFinish(Boolean result);
	public void OnProgress();
}
