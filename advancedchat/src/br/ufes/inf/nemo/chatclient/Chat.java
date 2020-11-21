package br.ufes.inf.nemo.chatclient;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import br.ufes.inf.nemo.chat.ChatListener;
import br.ufes.inf.nemo.chat.ChatRoom;
import br.ufes.inf.nemo.chat.Message;

public class Chat implements ChatListener {
	/**
	 * Um cliente para o ChatRoom capaz de enviar e receber
	 * mensagens. Este cliente checa periodicamente por novas
	 * mensagens, e as imprime no console quando forem recebidas.
	 */

	public static void main(String[] args) {

		String host = (args.length < 1) ? null : args[0];

		try {
			Registry registry = LocateRegistry.getRegistry(host);
			ChatRoom room = (ChatRoom) registry.lookup("ChatRoom");

			Chat obj = new Chat();
			ChatListener objref = (ChatListener) UnicastRemoteObject.exportObject(obj,0);

			room.registerChatListener(objref);
			
			Scanner s = new Scanner(System.in);

			System.out.println("Seu nome: ");
			String sender = s.nextLine();

			while (true)
			{
				String content = s.nextLine();		    
				room.postMessage(new Message(content, sender));
			}
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void notify(Message message) throws RemoteException {
		System.out.println(message.getSender() + ": " + message.getContent());
	}
}