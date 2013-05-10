package it.unitn.vanguard.reminiscence.utils;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Story {
	private int anno;
	private String title;
	private String desc;
	private Bitmap background;
	public ArrayList<String> urls;
	
	public Story(int anno,String title, String desc) {
		super();
		this.anno=anno;
		this.title = title;
		this.desc = desc;
	}

	public Bitmap getBackground() {
		return background;
	}


	public void setBackground(Bitmap background) {
		this.background = background;
	}


	public int getAnno() {
		return anno;
	}

	public String getTitle() {
		return title;
	}

	public String getDesc() {
		return desc;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}
	
	
}