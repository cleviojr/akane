package akane.command.commands.random;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import akane.api.RandomCat;
import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CatCommand implements CommandInterface{

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		try {
			event.getChannel().sendMessage(RandomCat.getLink()).queue();
		} catch (IOException e) {
			event.getChannel().sendMessage(":no_entry: O comando falhou.").queue((message) -> message.delete().queueAfter(5, TimeUnit.SECONDS));
		}
	}

}

