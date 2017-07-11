package akane.command.commands.random;

import java.util.Random;

import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChooseCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (args.length == 1 && !args[0].equals("help")) {
			event.getChannel().sendMessage(":no_entry: Você precisa ter mais de uma coisa pra eu escolher(use: .choose help).").queue();
			return false;
		} else if (args.length <= 0) {
			event.getChannel().sendMessage(":no_entry: Você precisa de pelo menos duas coisas pra eu escolher(use: .choose help).").queue();
			return false;
		}
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		Random choose = new Random();
		int chosen = choose.nextInt(args.length);
		event.getChannel().sendMessage(":white_check_mark: Eu escolho: **" + args[chosen] + ".**").queue();
	}

}
