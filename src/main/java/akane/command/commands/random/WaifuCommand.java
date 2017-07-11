package akane.command.commands.random;

import akane.api.MyWaifuList;
import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;

public class WaifuCommand implements CommandInterface {
	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		try {
			event.getChannel().sendMessage(MyWaifuList.getRandomWaifuInfo().build()).queue();
		} catch (IOException e) {
			event.getChannel().sendMessage(":no_entry: Não foi possivel executar o comando.").queue();
		}
	}
}
