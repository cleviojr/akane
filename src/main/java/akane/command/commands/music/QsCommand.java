package akane.command.commands.music;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class QsCommand implements CommandInterface{

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT)) {
			notifyServerCommand(event.getChannel());
			return false;
		}
		
		Guild guild = event.getGuild();
		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());
		Queue<AudioTrack> queue = mng.scheduler.queue;

		if (queue.isEmpty()) {
			event.getTextChannel().sendMessage(":no_entry: A fila está vazia.").queue();
			return false;
		}

		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());

		mng.scheduler.shuffle();
		event.getChannel().sendMessage(":notes: A fila foi reordenada de forma aleatória.").queue((message) ->
		message.delete().queueAfter(5, TimeUnit.SECONDS));

		try {
			event.getMessage().delete().completeAfter(5, TimeUnit.SECONDS);
		} catch (Exception ignored) {
		}
	}

	private void notifyServerCommand(MessageChannel channel) {
		channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
	}

}
