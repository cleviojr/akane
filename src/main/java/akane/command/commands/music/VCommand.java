package akane.command.commands.music;

import java.util.concurrent.TimeUnit;

import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT)) {
			notifyServerCommand(event.getChannel());
			return false;
		}
		
		Integer newVolume;
		if(args.length > 0) {
			try {
				newVolume = Integer.valueOf(args[0]);
			} catch (Exception e) {
				event.getTextChannel().sendMessage(":no_entry: Seu volume precisa ser um número.").queue();
				return false;
			}

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(newVolume < 0 || newVolume > 150) {
				event.getTextChannel().sendMessage(":no_entry: Seu volume precisa estar entre 0 e 150.").queue();
				return false;
			}

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!event.getMember().hasPermission(Permission.getPermissions(Permission.ALL_VOICE_PERMISSIONS))) {
				event.getTextChannel().sendMessage(":no_entry: Você precisa ter todas as permissões"
						+ " de voz para alterar o volume.").queue((message) -> message.delete().queueAfter(10, TimeUnit.SECONDS));
				return false;
			}
		}
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());

		if (args.length > 0) {
			Integer newVolume;
			newVolume = Integer.valueOf(args[0]);

			mng.player.setVolume(newVolume);
			event.getTextChannel().sendMessage(":notes: O volume foi mudado para: " + newVolume + "/150.").queue((message) ->
			message.delete().queueAfter(1, TimeUnit.MINUTES));
		} else {
			event.getTextChannel().sendMessage(":notes: O volume atual é: " + mng.player.getVolume() + "/150.").queue((message) ->
			message.delete().queueAfter(10, TimeUnit.SECONDS));
		}
		try {
			event.getMessage().delete().completeAfter(5, TimeUnit.SECONDS);
		} catch (Exception ignored) {
		}

	}

	private void notifyServerCommand(MessageChannel channel) {
		channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
	}
}
