package akane.command.commands.manage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class ClrCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT)) {
			event.getChannel().sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
			return false;
		}

		Integer num;
		if (args.length == 0) {
			num = 0;
		} else {
			try {
				num = Integer.valueOf(args[0]) + 1;
				if (num == 20)
					num = 20;
			} catch (Exception e) {
				try {
					event.getMessage().delete().queueAfter(300, TimeUnit.MILLISECONDS);
				} catch (Exception ignored) {

				}

				event.getTextChannel().sendMessage(":no_entry: Seu argumento precisa ser um número.").queue((message) ->
					message.delete().queueAfter(3, TimeUnit.SECONDS));

				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				return false;
			}
		}

		if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			event.getMessage().delete().queue();
			event.getTextChannel().sendMessage(":no_entry: Você não possui permissão para usar esse comando"
			+ "(permissão necessária: MESSAGE_MANAGE).")
			.queue((message) -> message.delete().queueAfter(10000, TimeUnit.MILLISECONDS));
			return false;
		} else if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE) && num > 1) {
			return true;
		} else if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE) && num <= 1) {
			event.getMessage().delete().queueAfter(200, TimeUnit.MILLISECONDS);
			event.getTextChannel().sendMessage(":no_entry: Por favor me forneça um número >=0.").queue((message) ->
				message.delete().queueAfter(3, TimeUnit.SECONDS));

			return false;
		}

		return false;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		try {
			Integer num = Integer.valueOf(args[0]) + 1;
			if (num > 21)
				num = 21;

			MessageHistory history = new MessageHistory(event.getTextChannel());
			List<Message> msgs;

			msgs = history.retrievePast(num).completeAfter(500, TimeUnit.MILLISECONDS);

			event.getTextChannel().deleteMessages(msgs).queueAfter(1000, TimeUnit.MILLISECONDS);
			event.getTextChannel().sendMessage(":white_check_mark: Deletei " + (num - 1) + " mensagens.").queue((message) ->
				message.delete().queueAfter(3, TimeUnit.SECONDS));
		} catch (PermissionException e) {
			event.getChannel().sendMessage(":no_entry: Eu não possuo permissão pra deletar mensagens nesse servidor.").queue();
		}

	}
}



