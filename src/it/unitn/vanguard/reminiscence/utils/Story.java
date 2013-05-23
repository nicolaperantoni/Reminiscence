package it.unitn.vanguard.reminiscence.utils;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Story {
	private int anno;
	private String id;
	private String title;
	private String desc;
	private String numImages;
	private Bitmap background;
	public ArrayList<String> urls;

	public Story(int anno, String title, String desc, String id) {
		super();
		this.anno = anno;
		this.title = title;
		this.desc = desc;
		this.id = id;
	}

	public Bitmap getBackground() {
		return background;
	}

	public void setBackground(Bitmap background) {
		this.background = background;
	}
	
	public String getNumImages() {
		return numImages;
	}

	public void setNumImages(String numImages) {
		this.numImages = numImages;
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
	
	public String getId() {
		return id;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Story) {
			Story s = (Story) o;
			return s.getId().equals(this.getId());
		}
		return false;
	}

	@Override
	public String toString() {
		String str = "";
		str += "Id: " + this.id + "\n";
		str += "Title: " + this.title + "\n";
		str += "Desc: " + this.desc + "\n";
		str += "Year: " + this.anno + "\n";
		str += "Background: " + this.background.toString() + "\n";
		return str;
	}
}
