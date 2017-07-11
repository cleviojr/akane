package akane.command.commands.etc;

import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class PingCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setColor(Color.magenta).build();
		embedBuilder.setTitle("Pong").build();
		event.getChannel().sendMessage(embedBuilder.build()).queue();
	}

}
