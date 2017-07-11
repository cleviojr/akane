package akane.command.commands.random;

import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;


public class RCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		Integer times, diceSides;

		if (args.length < 2) {
			event.getChannel().sendMessage(":no_entry: Preciso que coloque os argumentos na ordem: [numero de vezes] [numero de faces].").queue();
			return false;
		}

		try {
			times = Integer.valueOf(args[0]);
			diceSides = Integer.valueOf(args[1]);
		} catch (Exception e) {
			event.getChannel().sendMessage(":no_entry: Todos seus argumentos precisam ser números.").queue();
			return false;
		}

		if (times <= 0 || diceSides <= 0) {
			event.getChannel().sendMessage(":no_entry: Todos os argumentos devem ser maiores que 0.").queue();
			return false;
		}

		if(times > 20 || diceSides > 200) {
			event.getChannel().sendMessage(":no_entry: Só posso rodar o dado simultaneamente 20 vezes e não rodo mais do que 200 lados.").queue();
			return false;
		}

		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		Integer times, diceSides;
		int sum = 0;
		int randRes;
		StringBuilder message = new StringBuilder();
		Random rand = new Random();

		times = Integer.valueOf(args[0]);
		diceSides = Integer.valueOf(args[1]);

		message.append(":game_die: Rodei ").append(times).append(" vezes o d").append(diceSides).append(":\n");
		for (int i = 0; i < times; i++) {
			randRes = rand.nextInt(diceSides) + 1;
			message.append("Resultado número ").append(i + 1).append(": ").append(randRes).append(".\n");
			sum += randRes;
		}
		message.append("\nSoma: ").append(sum).append(".");

		event.getChannel().sendMessage(message.toString()).queue();
	}
}
