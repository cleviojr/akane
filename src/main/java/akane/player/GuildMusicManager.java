package akane.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.entities.TextChannel;

public class GuildMusicManager {
	public final AudioPlayer player;
	public final TrackScheduler scheduler;
	public final AudioPlayerSendHandler sendHandler;

	GuildMusicManager(AudioPlayerManager manager, TextChannel channel) {
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player, channel);
		sendHandler = new AudioPlayerSendHandler(player);
		player.addListener(scheduler);
	}
}
