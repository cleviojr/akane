package akane.command.commands.music;

import akane.Akane;
import akane.command.core.CommandContainer;
import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LoopCommand implements CommandInterface {
    @Override
    public void main(String[] args, MessageReceivedEvent event) {
        //sets loop to true
        GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());
        mng.scheduler.toggleLoop();
        if (mng.scheduler.loop) {
          if (mng.scheduler.radio)
            Akane.handleCommand(new CommandContainer(".radio", args, event));

          event.getTextChannel().sendMessage(":notes: Ativei o modo Loop!").queue();
        } else {
          event.getTextChannel().sendMessage(":notes: Desativei o modo Loop!").queue();
        }
    }

    @Override
    public boolean isSafe(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();

        if (!event.isFromType(ChannelType.TEXT)) {
            messageIsServerCommand(channel);
            return false;
        }

        return true;
    }

    private void messageIsServerCommand(MessageChannel channel) {
        channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
    }

}