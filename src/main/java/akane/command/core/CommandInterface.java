package akane.command.core;

import akane.Akane;
import akane.utils.HelpIndex;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface CommandInterface {
	void main(String[] args, MessageReceivedEvent event);
	boolean isSafe(String[] args, MessageReceivedEvent event);
	
	default boolean isHelp(String[] args) {
		return !(args.length == 0) && args[0].equalsIgnoreCase("help");
	}

	default void status(CommandContainer cmd, boolean success, MessageReceivedEvent event) {
		Akane.CommandLog(cmd, success, event);
	}

	default void getHelp(CommandContainer cmd, MessageReceivedEvent event) {
		event.getChannel().sendMessage(HelpIndex.getHelp(cmd)).queue();
	}

}
