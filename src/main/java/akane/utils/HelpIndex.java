package akane.utils;

import akane.Akane;
import akane.command.core.CommandContainer;

public class HelpIndex {
	private static String help[] = new String[Akane.commands.size() - 1];
	
	public static String getHelp(CommandContainer cmd) {
		for (int i = 0; i < help.length; i++) {
			help[i] = listHelp(i);
		}

		String name = cmd.invoke;
		for (String aHelp : help) {
			if (aHelp.contains(name)) {
				return aHelp;
			}
		}
		return null;
	}

	private static String listHelp(int i) {
		help[0] = "Use: .help (mostra a lista de comandos).";
		help[1] = "Use: .choose [argumento 1] [argumento 2] ... [argumento n]"
				+ " (devolve um dos argumentos aleatoriamente).";
		help[2] = "Use: .dado [numero de vezes] [numero de faces] (roda um dado de x vezes com y faces).";
		help[3] = "Use: .clr [numero de mensagens]"
				+ " (apaga o tanto de mensagens que voce quiser anteriores a sua)"
				+ " (requer permissão).";
		help[4] = "Use: .play [link youtube/soundcloud/twitch ou nome(pesquisa youtube apenas)].\n"
				+ "(toca uma musica ou playlist, entra no canal de voz automaticamente o bot não estiver ainda).";
		help[5] = "Use: .skip"
				+ " (pula pra proxima musica)>";
		help[6] = "Use: .fila (mostra a fila de musicas na fila).";
		help[7] = "Use: .shuffle (reordena a fila de forma aleatória).";
		help[8] = "Use: .limparfila (limpa a fila)(requer permissão).";
		help[9] = "Use: .sair (sai do canal de voz).";
		help[10] = "Use: .volume [volume de 0 a 150] (define o volume do bot)(requer permissão)." +
				"\n" + "caso queira apenas checar o volume, .v (devolve o volume atual)(não requer permissão).";
		help[11] = "Use: .invite (te mando o link pra me convidar no privado).";
		help[12] = "Use: .cat (random.cat).";
		help[13] = "Use: .mal [Insira sua pesquisa aqui] (Pesquisa um anime no MAL).";
		help[14] = "Use: .waifu(https://mywaifulist.moe/random).";
		help[15] = "Use: .ping (pong).";
		help[16] = "Use: .nyaa [sua pesquisa aqui] (Pesquisa um torrent no Nyaa e pega o com mais leechers, entao fornece todas as informações sobre o mesmo e seus links de download).";
		help[17] = "Use: .channel (define o canal atual como o canal para receber notificações de música).";
		help[18] = "Use: .radio (habilita ou desabilita o modo de playlist automatico do youtube).";
		help[19] = "Use: .pause (pausa a música ou retoma se estiver pausada)";
//		help[19] = "Use: .avatar [nome da pessoa do servidor] (mostra a imagem do avatar da pessoa).";
		return help[i];
	}

}
