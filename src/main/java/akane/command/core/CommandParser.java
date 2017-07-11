package akane.command.core;

import java.util.ArrayList;
import java.util.Collections;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandParser {
	public CommandContainer parse(String raw, MessageReceivedEvent event){
		String[] splitContent = raw.split(" ");
		ArrayList<String> splitContentList = new ArrayList<>();

		Collections.addAll(splitContentList, splitContent);

		String commandKey = splitContentList.get(0).toLowerCase();

		String[] args = new String[splitContentList.size() - 1];

		splitContentList.subList(1, splitContentList.size()).toArray(args);

		return new CommandContainer(commandKey, args, event);
	}
}


