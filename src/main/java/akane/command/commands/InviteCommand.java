package akane.command.commands;

import akane.command.core.CommandInterface;
import akane.utils.Credentials;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InviteCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		String inviteMessage = ":white_check_mark: Você que manda! Link do convite: <" + Credentials.INVITE_LINK_WITHPERMISSIONS + ">." +
		"\nSerá um prazer estar em seu servidor ❤.";
		String confirmMessage = ":white_check_mark: " + event.getAuthor().getAsMention() +
		", mandei o convite no privado! :wink:";
		String failedToSend = ":no_entry: Não posso te enviar mensagens de texto no privado, permita que pessoas do mesmo servidor te mandem mensagens no privado!";

		//case: user asked from invite inside a server.
		if (event.isFromType(ChannelType.TEXT)) {
			event.getMember().getUser().openPrivateChannel().queue(
			(channel) -> channel.sendMessage(inviteMessage).queue(
			//Tries to open a private channel and prevents exception.
			(success) -> {
				event.getTextChannel().sendMessage(confirmMessage).queue();
				event.getMessage().addReaction("❤").queue();
			},
			(fail) -> event.getChannel().sendMessage(failedToSend).queue()));
		} else if (event.isFromType(ChannelType.PRIVATE)) { //case: user asked from invite from private channel.
			event.getMessage().addReaction("❤").queue();
			event.getChannel().sendMessage(inviteMessage).queue();
		}

	}

}
