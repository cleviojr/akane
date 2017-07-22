package akane.command.commands.music;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import akane.command.core.CommandInterface;
import akane.listeners.PlListener;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import akane.api.Youtube;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class QCommand implements CommandInterface {
	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();

		if (!event.isFromType(ChannelType.TEXT)) {
			messageIsServerCommand(channel);
			return false;
		}

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

		if (!event.getGuild().getAudioManager().isConnected()) {
			joinVoiceChannel(event, event.getGuild(), mng);
		}

		if (mng.player.isPaused()) {
			mng.player.setPaused(false);
		}

		if (mng.scheduler.channel == null)
			mng.scheduler.channel = userChannel;

		if (mng.scheduler.radioQueue.size() > 0 && mng.scheduler.radio)
			mng.scheduler.radioQueue.clear();

		if (args.length == 1) {
			try {
				URL url = new URL(args[0]);
				if (url.getHost().equalsIgnoreCase("www.youtube.com") || url.getHost()
								.equalsIgnoreCase("youtu.be") || url.getHost()
								.equalsIgnoreCase("www.soundcloud.com") || url.getHost()
								.equalsIgnoreCase("youtube.com") || url.getHost()
								.equalsIgnoreCase("soundcloud.com")) {
					if ((args[0].contains("&list") || args[0].contains("playlist?list")) || (args[0].contains("soundcloud") && args[0].contains("/sets/"))) {
						MusicPlayer.loadAndPlay(mng, userChannel, args[0], true, false);
					} else {
						MusicPlayer.loadAndPlay(mng, userChannel, args[0], false, false);
					}
				} else {
					event.getChannel().sendMessage(":no_entry: Aceito apenas links do Youtube ou Soundcloud.").queue(s ->
									s.delete().queueAfter(5, TimeUnit.SECONDS));
				}
			} catch (MalformedURLException y) {
				youtubeQueue(args, event, mng);
			}
		} else {
			youtubeQueue(args, event, mng);
		}

		try {
			event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
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

	private void youtubeQueue(String[] args, MessageReceivedEvent event, GuildMusicManager mng) {
		Youtube youtube = new Youtube(null, String.join(" ", args));
		try {
			URL url = youtube.getFirstResult().getUrl();

			if (url.getQuery().contains("list=") || url.getQuery().contains("playlist?list")) {
        event
        .getChannel()
        .sendMessage(":notes: `Uma playlist foi detectada, deseja adicionar a" +
                          " playlist inteira(s ou n)?`\n`Você tem 5 segundos!`"      )
        .queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));

				event
        .getJDA()
        .addEventListener(new PlListener(event.getGuild(), event.getTextChannel(),
                                         url.toString(), event.getAuthor())     );
			} else {
				MusicPlayer.loadAndPlay(mng, event.getTextChannel(),
                                url.toString(), false,
                                false                   );
			}

		} catch (IOException i) {
			messageIOException(event.getChannel());
		}
	}
}