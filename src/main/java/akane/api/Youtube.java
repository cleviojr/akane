package akane.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;


public class Youtube {

	public static String getFirstResult(String query)  throws IOException {
		Document doc = Jsoup.connect("https://www.youtube.com/results?search_query=" + URLEncoder.encode(query, "UTF-8")).get();
		Elements results = doc.select("div#results div.yt-lockup div.yt-lockup-content a");

		return "https://youtube.com" + results.first().attr("href");
	}

	public static String getNextSongUrl(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements results = doc.select("div.content-wrapper a");

		return "https://youtube.com" + results.first().attr("href");
	}

}
