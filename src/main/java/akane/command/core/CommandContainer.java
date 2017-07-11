package akane.command.core;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class CommandContainer {
	public final String invoke;
	public final String[] args;
	public final MessageReceivedEvent event;

	CommandContainer(String invoke, String[] args, MessageReceivedEvent e){
		this.invoke = invoke;
		this.args = args;
		this.event = e;
	}
	
}
