package akane.listeners;

import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlListener extends ListenerAdapter {
	private Guild guild;
	private TextChannel channel;
	private String url;
	private User author;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public PlListener(Guild guild, TextChannel channel, String url, User author) {
		this.guild = guild;
		this.channel = channel;
		this.url = url;
		this.author = author;
	}

	public void onMessageReceived(MessageReceivedEvent event) {
    GuildMusicManager mng = MusicPlayer.getMusicManager(guild, channel);

    try {
      scheduler.schedule(() -> RemoveListener(event),5, TimeUnit.SECONDS);
    } catch (Exception ignored) {}

		if (!event.getAuthor().isBot() && event.getAuthor().equals(author) && event.getMessage().getContent().contains("s")) {
      MusicPlayer.loadAndPlay(mng, event.getTextChannel(), url, true, false);
			event.getJDA().removeEventListener(this);
		} else if (!event.getAuthor().isBot() && event.getAuthor().equals(author) && event.getMessage().getContent().contains("n")) {
      channel.sendMessage(":notes: Ok!").queue();
      event.getJDA().removeEventListener(this);
    }

	}

  private void RemoveListener(MessageReceivedEvent event) {
    event.getJDA().removeEventListener(this);
    scheduler.shutdown();
  }
}
