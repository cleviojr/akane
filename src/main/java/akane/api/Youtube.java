package akane.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


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

		return new YoutubeResult("https://www.youtube.com" + results.first().attr("href"));
	}

	public Stream<YoutubeResult> getRelated() throws IOException {
		Document doc          = Jsoup.connect(url).get();
		Elements items        = doc.select("ul.video-list li.video-list-item");
		Stream<Element> links = items.stream().map(e -> e.select("a").first());

		return links.map(a -> new YoutubeResult("https://www.youtube.com" + a.attr("href")));
	}

//	public YoutubeResult getResult(int index) throws IOException{
//		Document doc = Jsoup.connect(url).get();
//		Elements results = doc.select("div#results div.yt-lockup:eq(" + index + ") div.yt-lockup-content a");
//
//		return new YoutubeResult(new URL("https://youtube.com" + results.attr("href")), results);
//	}

	public void setUrl(String url) {
		this.url = url;
	}

	public URL getUrl() throws MalformedURLException { return new URL(url); }

}