package akane.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RandomCat {
	public static String getLink () throws IOException{
		URL randomCat = new URL("http://random.cat/meow");
		URLConnection rc = randomCat.openConnection();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						rc.getInputStream()));
		String catJson = in.readLine();

		Map<String, Object> jsonMap = new Gson().fromJson(
		catJson, new TypeToken<HashMap<String, Object>>() {}.getType()
		);
		
		return jsonMap.get("file").toString();
	}

}