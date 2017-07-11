package akane.listeners;

import akane.utils.LogTime;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildListener extends ListenerAdapter{

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		event.getGuild().getPublicChannel().sendMessage(":white_check_mark: Olá, obrigada por me convidar. Sou a Akane! :heart: \n " +
		"Caso tenha dúvidas sobre meus comandos digite .help").queue();
		System.out.println(LogTime.getTime() + "Entrei no servidor: " + event.getGuild().getName());
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		System.out.println(LogTime.getTime() + "Saí do servidor: " + event.getGuild().getName());
	}

}
