package akane.command.commands.music;

import java.util.concurrent.TimeUnit;

import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LCommand implements CommandInterface {
	private String channelName;

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT)) {
			notifyServerCommand(event.getChannel());
			return false;
		}

		if (!event.getGuild().getAudioManager().isConnected()) {
			event.getTextChannel().sendMessage(":no_entry: O bot não está em um canal de voz.").queue();
			return false;
		}
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		event.getTextChannel().sendMessage("Procurando o canal que estou...").queue((message) -> {
			channelName = message.getMember().getVoiceState().getChannel().getName();
			message.delete().queueAfter(500, TimeUnit.MILLISECONDS);
		});

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		event.getGuild().getAudioManager().closeAudioConnection();

		event.getTextChannel().sendMessage(":notes: Saí do canal de voz: #" + channelName + ".").queue();

		try {
			event.getMessage().delete().completeAfter(5, TimeUnit.SECONDS);
		} catch (Exception ignored) {
		}

	}

	private void notifyServerCommand(MessageChannel channel) {
		channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
	}
}
