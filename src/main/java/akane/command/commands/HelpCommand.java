package akane.command.commands;


import java.awt.*;

import akane.command.core.CommandInterface;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand implements CommandInterface {

	@Override
	public boolean isSafe(String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void main(String[] args, MessageReceivedEvent event) {
		EmbedBuilder helpMessage = new EmbedBuilder();
		helpMessage.setColor(Color.magenta).setTitle("Lista de comandos")
		.addField("Musica", ".play [link ou nome]\n.skip\n.shuffle\n.volume\n.sair\n.fila\n.limparfila *\n.channel *\n.radio\n.pause\n.loop", true)
		.addField("Random", ".cat\n.choose [op1] [op2] [opN]\n.dado [vezes] [faces]\n.waifu", true)
		.addField("Pesquisa", ".mal [nome]\n.nyaa [nome]", true)
		.addField("Admin",".clr [n de mensagens]\n.volume [novo volume]*",true)
		.addField("etc", ".ping", false)
		.addField("Me convide! ❤", ".invite", false)
		.setDescription("Para informação detalhada de cada comando: .comando help\nComandos com * precisam ser executados por pessoas com permissões de voz(todas).");

		if (event.isFromType(ChannelType.TEXT)) {
			event.getMessage().addReaction("❤").queue();

			event.getMember().getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(helpMessage.build()).queue());
			event.getTextChannel().sendMessage(":white_check_mark: " + event.getAuthor().getAsMention() +
			", te mandei a lista de comandos no privado! :wink:.").queue();

		} else if (event.isFromType(ChannelType.PRIVATE)) {
			event.getMessage().addReaction("❤").queue();

			event.getChannel().sendMessage(helpMessage.build()).queue();
		}

	}
}
