package akane.command.commands.music;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import akane.command.core.CommandInterface;
import akane.player.GuildMusicManager;
import akane.player.MusicPlayer;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LqCommand implements CommandInterface {

    @Override
    public boolean isSafe(String[] args, MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) {
            notifyServerCommand(event.getChannel());
            return false;
        }

        return true;
    }

    @Override
    public void main(String[] args, MessageReceivedEvent event) {
        GuildMusicManager mng = MusicPlayer.getMusicManager(event.getGuild(), event.getTextChannel());
        Queue<AudioTrack> queue = mng.scheduler.radio ? mng.scheduler.radioQueue : mng.scheduler.queue;
        StringBuilder sb = new StringBuilder();

        if (queue.isEmpty()) {
            event.getTextChannel().sendMessage(":notes: A fila está vazia.").queue();
            return;
        }

        int trackCount = 1;
        long queueLength = 0;
        double minLength;
        sb.append(":notes: Fila Atual(tamanho: ")
                .append(queue.size()).append(") mostrando as 10 primeiras:").append(System.lineSeparator())
                .append(System.lineSeparator());

        for (AudioTrack s : queue) {
            queueLength += s.getInfo().length;
            if (trackCount < 11) {
                sb.append(trackCount).append(" - ").append(s.getInfo().title).append(System.lineSeparator())
                        .append("link: ").append("<").append(s.getInfo().uri).append(">").append(System.lineSeparator());
                trackCount++;
            }
        }

        minLength = (double) (queueLength / 1000) / 60;
        sb.append(System.lineSeparator()).append("Tempo total de musica(em minutos): ").append(String.format("%.2f", minLength)).append(".");
        event.getTextChannel().sendMessage(sb.toString()).queue((message) -> message.delete().queueAfter(5, TimeUnit.MINUTES));

        try {
            event.getMessage().delete().completeAfter(5, TimeUnit.SECONDS);
        } catch (Exception ignored) {
        }

    }

    private void notifyServerCommand(MessageChannel channel) {
        channel.sendMessage(":no_entry: Só uso esse comando em servidores e não no privado.").queue();
    }
}
