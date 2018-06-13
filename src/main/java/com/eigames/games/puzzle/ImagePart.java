package com.eigames.games.puzzle;

public class ImagePart {
	private String url;
	private int partId;

	public ImagePart(String url, int partId) {
		this.url = url;
		this.partId = partId;
	}

	public String getUrl() {
		return url;
	}

	public int getPartId() {
		return partId;
	}

	public boolean isEmpty() {
		return partId == -1;
	}
}
