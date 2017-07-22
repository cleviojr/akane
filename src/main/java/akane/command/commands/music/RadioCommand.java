package akane.command.commands.music;

import akane.Akane;
import akane.api.Youtube;
import akane.command.core.CommandContainer;
import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;

public class RadioCommand implements CommandInterface {
	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());
		TextChannel channel = event.getTextChannel();

		mng.scheduler.toggleRadio();
		messageFinished(channel, mng.scheduler.radio);

		if (mng.scheduler.loop) {
		  Akane.handleCommand(new CommandContainer(".loop", args, event));
    }

		if (mng.scheduler.radio) {
			if (mng.player.getPlayingTrack() != null) {
				Youtube youtube = new Youtube();
				youtube.setUrl(mng.player.getPlayingTrack().getInfo().uri);
				try {
					String nextTrackUrl = youtube.getRelated().findFirst().get().getUrl().toString();
					MusicPlayer.loadAndPlay(mng, channel, nextTrackUrl, false, true);
				} catch (IOException ignored) {
				}
			}
		} else {
			mng.scheduler.radioQueue.clear();
		}

		if (mng.scheduler.loop) {
			Akane.handleCommand(new CommandContainer(".loop", args, event));
		}

	}

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		return true;
	}

	private void messageFinished(TextChannel channel, boolean radio) {
		if (radio)
			channel.sendMessage(":notes: O modo rádio está ligado!").queue();
		else channel.sendMessage(":notes: O modo rádio está desligado!").queue();
	}
}
