package akane.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.valueOf;

public class MyWaifuList {
	public static EmbedBuilder getRandomWaifuInfo() throws IOException {
		Map jsonMap = getJson();
		Map<String, String> infoMap = new HashMap<>();
		EmbedBuilder embedBuilder = new EmbedBuilder();

		//indentar
		infoMap.put("name", jsonMap.get("name").toString());
		infoMap.put("image", jsonMap.get("display_picture").toString());
		infoMap.put("link", "https://mywaifulist.moe/waifu/" + infoMap.get("name").replaceAll(" ", "-").replaceAll(",", "-"));
		infoMap.put("description", jsonMap.get("description").toString());
		infoMap.put("likes", String.valueOf(jsonMap.get("likes").toString()));
		infoMap.put("trash", String.valueOf(jsonMap.get("trash").toString()));
		String[] animeTitle = jsonMap.get("series").toString().split(","); //procurar como faz pra dar parse
		infoMap.put("anime", animeTitle[1].substring(6));
		if (infoMap.get("description").length() > 144) {
			infoMap.replace("description", infoMap.get("description").substring(0, 144) + "...\n[Leia mais](" + infoMap.get("link") + ").");
		}

		return Float.valueOf(infoMap.get("likes")) + Float.valueOf(infoMap.get("trash")) > 30 ? //if thats true, then it is ok.
		embedBuilder.setColor(Color.magenta)
		.setTitle(infoMap.get("name") + " - **" + infoMap.get("anime") + "**.", infoMap.get("link"))
		.setDescription("Classificação(likes/trash): " + infoMap.get("likes") + "/" + infoMap.get("trash") + ".\n\n" + "Descrição(MyWaifuList): " +
		infoMap.get("description"))
		.setImage(infoMap.get("image"))
		: getRandomWaifuInfo(); //if thats false, then the function is re-called.
	}

	private static Map getJson() throws IOException {
		Document doc = Jsoup.connect("https://mywaifulist.moe/random").get();
		Elements results = doc.select("div div#app waifucore");
		String waifuJson = results.attr("waifu-json");

		return new Gson().fromJson(
		waifuJson, new TypeToken<HashMap<String, Object>>() {
		}.getType());
	}

}
