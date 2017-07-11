package akane.player;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import akane.api.Youtube;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class TrackScheduler extends AudioEventAdapter {
	private AudioPlayer player;
	public final Queue<AudioTrack> queue;
	public final Queue<AudioTrack> radioQueue;
	private AudioTrack lastTrack;
	public TextChannel channel;
	public boolean radio;

	TrackScheduler(AudioPlayer player, TextChannel channel) {
		this.player = player;
		this.queue = new LinkedList<>();
		this.radioQueue = new LinkedList<>();
		this.channel = channel;
		this.radio = false;
		this.lastTrack = null;
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		String trackUrl = track.getInfo().uri;
		if (radio && radioQueue.isEmpty()) {
			if (trackUrl.contains("youtube.")) {
				String nextTrackUrl;
				try {
					GuildMusicManager mng = MusicPlayer.getMusicManager(channel.getGuild(), channel);
					nextTrackUrl = Youtube.getNextSongUrl(trackUrl);
					MusicPlayer.loadAndPlay(mng, channel, nextTrackUrl, false, true);
				} catch (IOException ignored) {
				}
			}
		}

	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		lastTrack = track;
		if (endReason.mayStartNext) {
			trackEndMessage(track, channel);
			next(channel);
		}
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		next(channel);
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		next(channel);
	}

	public void queue(AudioTrack track) {
		if (!(player.startTrack(track, true))) {
			if (radio)
				radioQueue.offer(track);
			else queue.offer(track);
		}
	}

	public void next(TextChannel channel) {
		if (radio) {
			nextSong(radioQueue, channel);
		} else nextSong(queue, channel);
	}

	public void shuffle() {
		Collections.shuffle((List<?>) queue);
	}

	private void trackEndMessage(AudioTrack track, TextChannel channel) {
		channel.sendMessage(":notes: Terminei de tocar: " + track.getInfo().title + "\nLink: <" + track.getInfo().uri + ">.").queue((message) ->
		message.delete().queueAfter(track.getDuration() - 1000, TimeUnit.MILLISECONDS));
	}

	private void messageCurrentSong(AudioTrack track, TextChannel channel) {
		String url = track.getInfo().uri;
		long trackDuration = track.getDuration();
		String message = ":notes: Tocando agora: " + player.getPlayingTrack().getInfo().title + "\nLink: <" + url + ">.";

		channel.sendMessage(message).queue((sentMessage) ->
		sentMessage.delete().queueAfter(trackDuration, TimeUnit.MILLISECONDS));
	}

	private void startTrack() {
		if (radio)
			player.startTrack(radioQueue.poll(), false);
		else player.startTrack(queue.poll(), false);
	}

	private AudioTrack getPlayingTrack() {
		return player.getPlayingTrack();
	}

	private void messageFinishedPlaying(TextChannel channel) {
		channel.sendMessage(":notes: Toquei todas as musicas da fila >.<.").queue((message -> message.delete().queueAfter(1, TimeUnit.MINUTES)));
	}

	private void leaveVoiceChannel(Guild guild) {
		guild.getAudioManager().closeAudioConnection();
	}

	private Guild getGuild(TextChannel channel) {
		return channel.getGuild();
	}

	public void toggleRadio() {
		radio = !radio;
	}

	public void setChannel(TextChannel newChannel) {
		channel = newChannel;
	}

	private void nextSong(Queue queue, TextChannel channel) {
		if (!queue.isEmpty()) {
			startTrack();
			messageCurrentSong(getPlayingTrack(), channel);
		} else {
			messageFinishedPlaying(channel);
			leaveVoiceChannel(getGuild(channel));
		}
	}
}
