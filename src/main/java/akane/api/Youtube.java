package akane.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;


public class Youtube {

	 private String query;
	 private String url;

	public Youtube(String url, String query) {
		this.url = url;
		this.query = query;
	}

	public Youtube() {
		this.url = null;
		this.url = null;
	}

	public YoutubeResult getFirstResult()  throws IOException {
		Document doc = Jsoup.connect("https://www.youtube.com/results?search_query=" + URLEncoder.encode(query, "UTF-8")).get();
		Elements results = doc.select("div#results div.yt-lockup div.yt-lockup-content a");

		return new YoutubeResult("https://youtube.com" + results.first().attr("href"), results);
	}



	public String getNextSongUrl() throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements results = doc.select("div.content-wrapper a");

		return "https://youtube.com" + results.first().attr("href");
	}

	public void setUrl(String url) {
		this.url = url;
	}

}