package akane.api;

import org.jsoup.select.Elements;

public class YoutubeResult {
	private Elements pageDoc;
	private String resultUrl;
	public boolean isPlaylist;

	YoutubeResult(String url, Elements pageDoc) {
		this.resultUrl = url;
		this.pageDoc = pageDoc;
	}

	public String getUrl() {
		return resultUrl;
	}

	public boolean isPlaylist() {

		return false;
	}
}
