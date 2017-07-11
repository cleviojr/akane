package akane.listeners;

import akane.player.MusicPlayer;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.xml.soap.Text;

public class PlListener extends ListenerAdapter {
	private Guild guild;
	private TextChannel channel;
	private String url;

	public PlListener(Guild guild, TextChannel channel, String url) {
		this.guild = guild;
		this.channel = channel;
		this.url = url;
	}

	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			MusicPlayer.loadAndPlay(MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel()), event.getTextChannel(), url, true, false);
			event.getJDA().removeEventListener(this);
		}
	}
}
