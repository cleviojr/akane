package akane.command.commands.music;

import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChCommand implements CommandInterface {
	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());

		mng.scheduler.setChannel(event.getTextChannel());
		messageFinished(event.getTextChannel());
	}

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT)) {
			messageIsServerCommand(event.getChannel());
			return false;
		}

		if (event.getMember().hasPermission(Permission.getPermissions(Permission.ALL_VOICE_PERMISSIONS)))
			return true;
		else
			messageNoPermission(event.getTextChannel());
		return false;
	}

	private void messageFinished(TextChannel channel) {
		String message = ":notes: Agora o canal de atualizações sobre músicas é: " + channel.getAsMention() + ".";
		channel.sendMessage(message).queue();
	}

	private void messageNoPermission(TextChannel channel) {
		channel.sendMessage(":no_entry: Você não possui permissão para usar este comando.").queue();
	}

	private void messageIsServerCommand(MessageChannel channel) {
		channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
	}
}

