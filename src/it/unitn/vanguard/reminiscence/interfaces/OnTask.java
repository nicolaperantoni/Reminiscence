package it.unitn.vanguard.reminiscence.interfaces;

public interface OnTask {
	public void OnStart();
	public void OnFinish(Boolean result);
	public void OnProgress();
}
