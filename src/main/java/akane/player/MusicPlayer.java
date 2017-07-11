package akane.player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import akane.utils.Credentials;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;


public class MusicPlayer {
	private static final int DEFAULT_VOLUME = 20;
	private static final int queueLimit = Credentials.QUEUE_LIMIT;
	private static AudioPlayerManager musicPlayer;
	private static Map<String, GuildMusicManager> musicManagers;

	public MusicPlayer() {
		MusicPlayer.musicPlayer = new DefaultAudioPlayerManager();
		musicPlayer.registerSourceManager(new YoutubeAudioSourceManager());
		musicPlayer.registerSourceManager(new SoundCloudAudioSourceManager());
		musicPlayer.registerSourceManager(new BandcampAudioSourceManager());
		musicPlayer.registerSourceManager(new VimeoAudioSourceManager());
		musicPlayer.registerSourceManager(new TwitchStreamAudioSourceManager());
		musicPlayer.registerSourceManager(new HttpAudioSourceManager());
		musicPlayer.registerSourceManager(new LocalAudioSourceManager());
		musicManagers = new HashMap<>();
	}

	public static GuildMusicManager getMusicManager(Guild guild, TextChannel channel) {
		String guildId = guild.getId();
		GuildMusicManager mng = musicManagers.get(guildId);

			if (mng == null) {
				mng = new GuildMusicManager(musicPlayer, channel);
				mng.player.setVolume(DEFAULT_VOLUME);
				musicManagers.put(guildId, mng);
			}

		return mng;
	}

	public static void loadAndPlay(GuildMusicManager mng, TextChannel userChannel, String url, boolean addPlaylist, boolean isRadio) {

		final String trackUrl;
		if (url.startsWith("<") && url.endsWith(">")) {
			trackUrl = url.substring(1, url.length() - 1);
		} else {
			trackUrl = url;
		}

		musicPlayer.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				if (mng.scheduler.queue.size() == queueLimit) {
					messageQueueFull(userChannel);
				} else if (!isRadio) {
					if (mng.scheduler.queue.isEmpty() && mng.player.getPlayingTrack() == null) {
						userChannel.sendMessage(":notes: Comecei a tocar: "
						+ track.getInfo().title + "." +
						"\n" + "Link: " + "<" + trackUrl + ">" + ".").queue((message) ->
						message.delete().queueAfter(track.getDuration(), TimeUnit.MILLISECONDS));
					} else {
						userChannel.sendMessage(":notes: Adicionado a fila: "
						+ track.getInfo().title + "." +
						"\n" + "Link: " + "<" + trackUrl + ">" + ".").queue((message) ->
						message.delete().queueAfter(1, TimeUnit.MINUTES));
					}
					mng.scheduler.queue(track);
				} else {
					mng.scheduler.queue(track);
				}
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {

				synchronized (playlist) {
					AudioTrack firstTrack = playlist.getSelectedTrack();

					if (firstTrack == null) {
						firstTrack = playlist.getTracks().get(0);
					}

					if (mng.scheduler.queue.size() == queueLimit) {
						messageQueueFull(userChannel);
						return;
					} else if (mng.scheduler.queue.size() + playlist.getTracks().size() >= queueLimit) {
						purgePlaylist(mng, playlist, userChannel);
					}

					if (addPlaylist) {
						int songsAdded = playlist.getTracks().size();
						if (mng.scheduler.queue.isEmpty() && mng.player.getPlayingTrack() == null) {
							userChannel.sendMessage(":notes: Comecei a tocar: " +
							playlist.getTracks().get(0).getInfo().title + ".\nLink: <" +
							trackUrl + ">.").queue((message) ->
							message.delete().queueAfter(playlist.getTracks().get(0).getDuration(), TimeUnit.MILLISECONDS));
							songsAdded--;
						}

						playlist.getTracks().forEach(mng.scheduler::queue);
						userChannel.sendMessage(":notes: Adicionei: " + (songsAdded) + " músicas à fila, "
						+ "da playlist: " + playlist.getName() + ".").queue((message)
						-> message.delete().queueAfter(5, TimeUnit.MINUTES));
					} else {
						mng.scheduler.queue(firstTrack);
					}
				}
			}

			@Override
			public void noMatches() {
				userChannel.sendMessage(":no_entry: link invalido: " + trackUrl + ".").queue((message) ->
				message.delete().queueAfter(3, TimeUnit.SECONDS));
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				userChannel.sendMessage(":no_entry: Não foi possivel tocar sua música(verifique se o link é valido).").queue((message) ->
				message.delete().queueAfter(3, TimeUnit.SECONDS));
			}

		});
	}

	private static void indexPlaylist(AudioPlaylist playlist, int[] index) {
		int playlistSize = playlist.getTracks().size();
		for (int i = 0; i < playlistSize; i++) {
			index[i] = i;
		}
	}

	private static void purgePlaylist(GuildMusicManager mng, AudioPlaylist playlist, MessageChannel channel) {
		int index[] = new int[playlist.getTracks().size()];
		int queueSize = mng.scheduler.queue.size();
		int removeCount = 0;

		indexPlaylist(playlist, index);

		if (mng.player.getPlayingTrack() == null) {
			for (int i = playlist.getTracks().size() - 1; playlist.getTracks().size() != (queueLimit + 1) - (queueSize); i--) {
				playlist.getTracks().remove(index[i]);
				removeCount++;
			}
		} else {
			for (int i = playlist.getTracks().size() - 1; playlist.getTracks().size() != (queueLimit) - (queueSize); i--) {
				playlist.getTracks().remove(index[i]);
				removeCount++;
			}
		}
		channel.sendMessage(":fast_forward: Sua playlist excederia o máximo de músicas,"
		+ " por isso removi as " + removeCount
		+ " últimas músicas dela.").queue((message ->
		message.delete().queueAfter(5, TimeUnit.SECONDS)));
	}

	private static void messageQueueFull(TextChannel userChannel) {
		String message = ":no_entry: Não posso adicionar sua playlist, pois sua fila já está com o máximo de músicas(100)";

		userChannel.sendMessage(message + ".")
		.queue((newMessage) -> newMessage.delete().queueAfter(3, TimeUnit.SECONDS));
	}

}

