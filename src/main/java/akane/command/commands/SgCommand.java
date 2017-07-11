package akane.command.commands;

import akane.command.core.CommandInterface;
import akane.utils.Credentials;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class SgCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		return event.getAuthor().getId().equals(Credentials.OWNER_ID);
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();

		for (String s : args) {
			sb.append(s).append(" ");
		}

		event.getJDA().getPresence().setGame(Game.of(sb.toString()));
		event.getChannel().sendMessage(":white_check_mark: Novo jogo: " + sb.toString() + ".").queue();

	}

}
