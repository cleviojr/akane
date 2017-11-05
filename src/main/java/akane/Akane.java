package akane;


import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;
import akane.command.commands.*;
import akane.command.commands.etc.PingCommand;
import akane.command.commands.manage.*;
import akane.command.commands.music.*;
import akane.command.commands.random.*;
import akane.command.commands.search.*;
import akane.command.core.CommandContainer;
import akane.command.core.CommandInterface;
import akane.command.core.CommandParser;
import akane.listeners.CommandListener;
import akane.listeners.GuildListener;
import akane.listeners.StatusListener;
import akane.player.MusicPlayer;
import akane.utils.Credentials;
import akane.utils.LogTime;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class  Akane{
	public static JDA jda;
	public static final CommandParser parser = new CommandParser();
	public static HashMap<String, CommandInterface> commands = new HashMap<>();
	public static MusicPlayer player = new MusicPlayer();

	public static void main(String[] args) {
		//builder attributes
		JDABuilder builder;
		builder = new JDABuilder(AccountType.BOT);
		builder.setToken(Credentials.BOT_TOKEN);
		builder.setAutoReconnect(true);
		builder.addEventListener(new CommandListener());
		builder.addEventListener(new StatusListener());
		builder.addEventListener(new GuildListener());
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setGame(Game.of(".help | .invite ❤"));

		//login attempt
		try {
			jda = builder.buildBlocking();
		} catch (LoginException | IllegalArgumentException | RateLimitedException | InterruptedException e) {
			e.printStackTrace();
		}

		/*
		commands hashmap creation
		ping is commented because it is not useful at all
		*/
		commands.put(".help", new HelpCommand());
		commands.put(".ping", new PingCommand());
		commands.put(".choose", new ChooseCommand());
		commands.put(".play", new QCommand());
		commands.put(".clr", new ClrCommand());
		commands.put(".skip", new NCommand());
		commands.put(".fila", new LqCommand());
		commands.put(".shuffle", new QsCommand());
		commands.put(".limparfila", new QclrCommand());
		commands.put(".sg", new SgCommand());
		commands.put(".sair", new LCommand());
		commands.put(".volume", new VCommand());
		commands.put(".invite", new InviteCommand());
		commands.put(".cat", new CatCommand());
		commands.put(".dado", new RCommand());
		commands.put(".mal", new MALCommand());
		commands.put(".waifu", new WaifuCommand());
		commands.put(".nyaa", new NyaaCommand());
		commands.put(".channel", new ChCommand());
		commands.put(".radio", new RadioCommand());
		commands.put(".pause", new PauseCommand());
		commands.put(".loop", new LoopCommand());
	}

	public static void handleCommand(CommandContainer cmd) {
			if (commands.containsKey(cmd.invoke)) {
				boolean help = commands.get(cmd.invoke).isHelp(cmd.args);
				boolean safe;
				if (!help) {
					safe = commands.get(cmd.invoke).isSafe(cmd.args, cmd.event);
					if (safe) {
						commands.get(cmd.invoke).main(cmd.args, cmd.event);
						commands.get(cmd.invoke).status(cmd, true, cmd.event);
					} else {
						commands.get(cmd.invoke).status(cmd, false, cmd.event);
					}
				} else {
					commands.get(cmd.invoke).getHelp(cmd, cmd.event);
					commands.get(cmd.invoke).status(cmd, true, cmd.event);
				}
			} else if (cmd.event.isFromType(ChannelType.PRIVATE)) {
				cmd.event.getChannel().sendMessage("`Digite .help para obter a lista de comandos.`").queue();
			}
		}

	public static void CommandLog(CommandContainer cmd, boolean success, MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT)) {
			String user = event.getAuthor().getName();
			String message = event.getMessage().getRawContent();

			if (commands.get(cmd.invoke).isHelp(cmd.args)) {
				String print = LogTime.getTime() + "No comando (" + cmd.invoke + ") foi solicitada ajuda." +
				"\n" + "autor: " + user + "\n" + "servidor: privado" +
				"\n" + "linha do comando: " + message;

				System.out.println(print);
			} else if (success) {
				String print = LogTime.getTime() + "O comando (" + cmd.invoke + ") foi executado." +
				"\n" + "autor: " + user + "\n" + "servidor: privado" +
				"\n" + "linha do comando: " + message;

				System.out.println(print);


			} else {
				String print = LogTime.getTime() + "O comando (" + cmd.invoke + ") nao foi executado." +
				"\n" + "autor: " + user + "\n" + "servidor: privado" +
				"\n" + "linha do comando: " + message;

				System.out.println(print);
			}
		} else if (event.isFromType(ChannelType.TEXT)) {
			String user = event.getMember().getEffectiveName();
			String server = event.getGuild().getName();
			String message = event.getMessage().getRawContent();
			String channel = event.getMessage().getChannel().getName();

			if (commands.get(cmd.invoke).isHelp(cmd.args)) {
				String print = LogTime.getTime() + "No comando (" + cmd.invoke + ") foi solicitada ajuda." +
				"\n" + "autor: " + user + "\n" + "servidor: " + server +
				"\n" + "canal: " + channel + "\n" + "linha do comando: " + message;

				System.out.println(print);
			} else if (success) {
				String print = LogTime.getTime() + "O comando (" + cmd.invoke + ") foi executado." +
				"\n" + "autor: " + user + "\n" + "servidor: " + server +
				"\n" + "canal: " + channel + "\n" + "linha do comando: " + message;

				System.out.println(print);


			} else {
				String print = LogTime.getTime() + "O comando (" + cmd.invoke + ") nao foi executado." +
				"\n" + "autor: " + user + "\n" + "servidor: " + server +
				"\n" + "canal: " + channel + "\n" + "linha do comando: " + message;

				System.out.println(print);
			}
		}
	}

	public static void statusLog(ReadyEvent event) {
		StringBuilder guildList = new StringBuilder();
		List<Guild> list = event.getJDA().getGuilds();
		//Manda mensagem no console
		for (Guild g : list) {
			guildList.append("(").append(g.getName()).append(")");
		}

		System.out.println(LogTime.getTime() + "O bot esta rodando nos servidores(total de: " + list.size() + " servidores): ");
		System.out.println(guildList.toString());
	}

}
