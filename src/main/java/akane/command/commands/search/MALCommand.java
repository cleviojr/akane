package akane.command.commands.search;

import akane.api.MyAnimeList;
import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;

public class MALCommand implements CommandInterface {
	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (args.length == 0) {
			messageNoArgs(event.getChannel());
			return false;
		}
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		try {
			event.getChannel().sendMessage(MyAnimeList.getMALLink(String.join(" ", args))).queue();
		} catch (IOException e) {
			event.getChannel().sendMessage(":no_entry: Não foi possível realizar sua pesquisa.").queue();
		}
	}

	private void messageNoArgs(MessageChannel channel) {
		channel.sendMessage(":no_entry: Você precisa me falar o anime que deseja pesquisar no MAL, hmpf! >//<.").queue();
	}
}
