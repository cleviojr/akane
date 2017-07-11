package akane.listeners;

import akane.Akane;
import akane.utils.LogTime;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class StatusListener extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent event) {
		Akane.statusLog(event);
	}

	@Override
	public void onDisconnect(DisconnectEvent event) {
		System.out.println(LogTime.getTime() + "Fui desconectada.");
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		System.out.println(LogTime.getTime() + "Fui reconectada.");
	}

	public void onException(ExceptionEvent event) {
		System.out.println(LogTime.getTime() + "Ocorreu uma exceção.");
	}
}
