package akane.listeners;

import akane.Akane;
import akane.command.core.CommandContainer;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	//	public static boolean welcomeMessage = true;
	private static String prefix = ".";

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if ((startsWithPrefix(event)) && !(isFromBot(event))) {
			sendCommand(event);
		} else if ((isFromPrivateChannel(event)) && !(isFromBot(event))){
			sendCommand(event);
		}
	}

	private void sendCommand(MessageReceivedEvent event) {
		Akane.handleCommand(getCommand(event));
	}

	private CommandContainer getCommand(MessageReceivedEvent event) {
		return Akane.parser.parse(event.getMessage().getContent(), event);
	}


	private boolean isFromBot(MessageReceivedEvent event) {
		return event.getAuthor().isBot();
	}

	private boolean startsWithPrefix(MessageReceivedEvent event) {
		return event.getMessage().getContent().startsWith(prefix);
	}

	private boolean isFromPrivateChannel(MessageReceivedEvent event) {
		return event.isFromType(ChannelType.PRIVATE);
	}
}
