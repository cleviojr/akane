package akane.command.commands.music;

import java.util.concurrent.TimeUnit;

import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class QclrCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT)) {
			notifyServerCommand(event.getChannel());
			return false;
		}

		Guild guild = event.getGuild();
		GuildMusicManager mng = MusicPlayer.getMusicManager(guild, event.getTextChannel());

		if (mng.scheduler.queue.isEmpty()) {
			event.getTextChannel().sendMessage(":no_entry: A fila está vazia.").queue();
			return false;
		} else if (!event.getMember().hasPermission(Permission.getPermissions(Permission.ALL_VOICE_PERMISSIONS))) {
			event.getTextChannel().sendMessage(":no_entry: Você precisa ter todas as permissões de voz para usar este comando.")
			.queue((message) -> message.delete().queueAfter(10, TimeUnit.SECONDS));
			return false;
		}

		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		GuildMusicManager mng = MusicPlayer.getMusicManager(guild, event.getTextChannel());

		mng.scheduler.queue.clear();
		event.getTextChannel().sendMessage(":notes: Limpei a fila!").queue((message) ->
		message.delete().queueAfter(10, TimeUnit.SECONDS));

		try {
			event.getMessage().delete().completeAfter(5, TimeUnit.SECONDS);
		} catch (Exception ignored) {
		}

	}

	private void notifyServerCommand(MessageChannel channel) {
		channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
	}
}
