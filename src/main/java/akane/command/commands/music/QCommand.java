package akane.command.commands.music;

import java.io.IOException;
import java.util.EventListener;
import java.util.concurrent.TimeUnit;

import akane.command.core.CommandInterface;
import akane.listeners.PlListener;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import akane.api.Youtube;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class QCommand implements CommandInterface {
	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();

		if (!event.isFromType(ChannelType.TEXT)) {
			messageIsServerCommand(channel);
			return false;
		}

		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());

		if (args.length == 0) {
			messageNoArgs(channel);
			return false;
		}

		if (!event.getMember().getVoiceState().inVoiceChannel()) {
			messageOutsideChannel(channel);
			return false;
		}
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());
		TextChannel userChannel = event.getTextChannel();

		 if (!event.getGuild().getAudioManager().isConnected()){
			joinVoiceChannel(event, event.getGuild(), mng);
		}

		if (mng.player.isPaused()) {
		 	mng.player.setPaused(false);
		}

		if (mng.scheduler.channel == null)
			mng.scheduler.channel = userChannel;

		if (mng.scheduler.radioQueue.size() > 0 && mng.scheduler.radio)
			mng.scheduler.radioQueue.clear();

		if (args[0].contains("http://") || args[0].contains("https://")) {
			if (args[0].contains("&list") || args[0].contains("playlist?list") || (args[0].contains("soundcloud") && args[0].contains("/sets/"))) {
				MusicPlayer.loadAndPlay(mng, userChannel, args[0], true, false);
			} else {
				MusicPlayer.loadAndPlay(mng, userChannel, args[0], false, false);
			}
		} else {
			Youtube youtube = new Youtube(null, String.join(" ", args));
			try {
				String url = youtube.getFirstResult().getUrl();

				if (url.contains("&list") || url.contains("playlist?list")) {
					event.getJDA().addEventListener(new PlListener(event.getGuild(), event.getTextChannel(), url));
					event.getChannel().sendMessage("Deseja adicionar a playlist: ?").queue();
				} else {
					MusicPlayer.loadAndPlay(mng, userChannel, url, false, false);
				}

			} catch (IOException e) {
				messageIOException(event.getChannel());
			}
		}

		try {
			event.getMessage().delete().completeAfter(5, TimeUnit.SECONDS);
		} catch (Exception ignored) {
		}

	}

	private void joinVoiceChannel(MessageReceivedEvent event, Guild guild, GuildMusicManager mng) {
		guild.getAudioManager().setSendingHandler(mng.sendHandler);
		guild.getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
	}

	private void messageOutsideChannel(MessageChannel channel) {
		channel.sendMessage(":no_entry: Voce não esta em um canal de voz"
		+ "(ou eu não possuo acesso ao seu canal de voz).").queue();
	}

	private void messageIOException(MessageChannel channel) {
		channel.sendMessage(":no_entry: Não foi possível achar sua música.").queue();
	}

	private void messageNoArgs(MessageChannel channel) {
		channel.sendMessage(":no_entry: Você precisa colocar o link ou nome.").queue();
	}

	private void messageIsServerCommand(MessageChannel channel) {
		channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
	}
}
