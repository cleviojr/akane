package akane.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

public class MyAnimeList {
	public static String getMALLink(String query) throws IOException{
		Document doc = Jsoup.connect("https://myanimelist.net/search/all?q=" + URLEncoder.encode(query, "UTF-8")).get();
		Elements results = doc.select("div#content article div.information a");

		return results.attr("href");
	}
}
