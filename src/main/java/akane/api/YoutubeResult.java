package akane.api;

import java.net.MalformedURLException;
import java.net.URL;

public class YoutubeResult {
	private URL resultUrl;

	YoutubeResult(String url) {
		try {
			this.resultUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public URL getUrl() {
		return resultUrl;
	}

}
