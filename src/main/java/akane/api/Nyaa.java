package akane.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

public class Nyaa {
	public static HashMap getEntireContent(String query) throws IOException {
		Document fromSearchDoc = search(query);
		if (fromSearchDoc.select("h3").text().equals("No results found"))
			return null;

		return getInformation(fromSearchDoc);
	}

	private static Document search(String query) throws IOException {
		return Jsoup.connect("https://nyaa.si/?f=0&c=0_0&q=" +
		URLEncoder.encode(query, "UTF-8") + "&s=seeders&o=desc").get();
	}

	private static HashMap getInformation(Document doc) {
		HashMap<String, String> information = new HashMap<>();

		Elements titleAndLink = getTitleAndLink(doc);
		Elements torrentLink = doc.select("div.table-responsive td.text-center a");
		Elements magnetLink = doc.select("div.table-responsive td.text-center a:eq(1)");
		Elements fileSize = doc.select("div.table-responsive td:eq(3)");
		Elements dateUploaded = doc.select("div.table-responsive td:eq(4)");
		Elements seeders = doc.select("div.table-responsive td:eq(5)");
		Elements leechers = doc.select("div.table-responsive td:eq(6)");

		information.put("link", titleAndLink.get(0).attr("href"));
		information.put("title", titleAndLink.get(0).attr("title"));
		information.put("torrentLink", torrentLink.get(0).attr("href"));
		information.put("magnetLink", magnetLink.get(0).attr("href"));
		information.put("fileSize", fileSize.get(0).text());
		information.put("uploadDate", dateUploaded.get(0).text());
		information.put("seeders", seeders.get(0).text());
		information.put("leechers", leechers.get(0).text());

		return information;
	}

	private static Elements getTitleAndLink(Document doc){
		if (doc.select("div.table-responsive td[colspan] a").attr("href").contains("comment"))
			return doc.select("div.table-responsive td[colspan] a:eq(1)");
		else
			return doc.select("div.table-responsive td[colspan] a");
	}
}
