package akane.command.commands.search;

import akane.api.Nyaa;
import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class NyaaCommand implements CommandInterface {
	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		return !(args.length == 0);
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		HashMap content;
		try {
			content = Nyaa.getEntireContent(String.join(" ", args));
			if (content == null)
				notifyNoResult(event.getChannel());
			else
				sendEmbedMessage(event.getChannel(), getEmbedMessage(content));
		} catch (IOException e) {
			notifySearchFail(event.getChannel());
		}
	}

	private void notifySearchFail(MessageChannel channel) {
		channel.sendMessage(":no_entry: Não foi possível realizar sua pesquisa").queue();
	}

	private void notifyNoResult(MessageChannel channel) {
		channel.sendMessage(":no_entry: Não encontrei nenhum torrent no Nyaa com o nome fornecido.").queue();
	}

	private EmbedBuilder getEmbedMessage(HashMap content) {
		EmbedBuilder returnMessage = new EmbedBuilder();

		returnMessage
		.setColor(Color.magenta)
		.setTitle(content.get("title").toString(), "https://nyaa.si" + content.get("link").toString())
		.setDescription("Tamanho: " + "`" + content.get("fileSize") + "`" +
		"\nData de upload: " + "`" + content.get("uploadDate") + "`" +
		"\nSeeders/Leechers: " + "`" + content.get("seeders") + "/" + content.get("leechers") + "`" +
		"\nDownload: " + "[Torrent](https://nyaa.si" + content.get("torrentLink") + ")" + "/" + "[Magnet](" + content.get("magnetLink") + ")");

		return returnMessage;
	}

	private void sendEmbedMessage(MessageChannel channel, EmbedBuilder returnMessage) {
		channel.sendMessage(returnMessage.build()).queue();
	}
}

