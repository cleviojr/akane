package akane.command.commands.music;

import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PauseCommand implements CommandInterface{
	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());

		mng.player.setPaused(!mng.player.isPaused());
		event.getTextChannel().sendMessage(mng.player.isPaused() ? ":notes: Pausei." : ":notes: Retomei.").queue();
	}

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());
		if (mng.player.getPlayingTrack() == null) {
			event.getTextChannel().sendMessage(":no_entry: Não estou tocando nada.").queue();
			return false;
		}

		return true;
	}
}
