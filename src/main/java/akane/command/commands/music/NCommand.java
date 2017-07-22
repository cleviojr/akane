package akane.command.commands.music;

import akane.Akane;
import akane.command.core.CommandContainer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class NCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT)) {
			notifyServerCommand(event.getChannel());
			return false;
		}

		Guild guild = event.getGuild();
		GuildMusicManager mng = MusicPlayer.getMusicManager(guild, event.getTextChannel());
		AudioPlayer player = mng.player;
		AudioTrack track = player.getPlayingTrack();

		if (track == null) {
			event.getTextChannel().sendMessage(":no_entry: Não estou tocando nada.").queue();
			return false;
		}
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		GuildMusicManager mng = MusicPlayer.getMusicManager(guild, event.getTextChannel());

		if (mng.scheduler.loop) {
			Akane.handleCommand(new CommandContainer(".loop", args, event));
		}

		mng.scheduler.next(event.getTextChannel());

		try {
			event.getMessage().delete().completeAfter(5, TimeUnit.SECONDS);
		} catch (Exception ignored) {
		}

	}

	private void notifyServerCommand(MessageChannel channel) {
		channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
	}

}
