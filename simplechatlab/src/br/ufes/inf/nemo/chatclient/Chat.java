package br.ufes.inf.nemo.chatclient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import br.ufes.inf.nemo.chat.ChatRoom;
import br.ufes.inf.nemo.chat.Message;

public class Chat {
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

			int lastMessage = room.lastMessageNumber();

			(new ReaderThread(room,lastMessage)).start();
			
			Scanner s = new Scanner(System.in);

			System.out.println("Sender: ");
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

}

class ReaderThread extends Thread
{
	private ChatRoom room;
	private int lastRead;
	public ReaderThread(ChatRoom room, int lastRead)
	{
		this.room=room;
		this.lastRead=lastRead;
	}
	public void run()
	{
		while(true)
		{
			try {
				for (Message m : room.getMessages(lastRead))
				{
					System.out.println(m.getSender() + ": " + m.getContent());
					lastRead++;
				}
				sleep(1000);
			} catch (java.rmi.RemoteException e)
			{
				System.out.println("Problemas no servidor!");
				e.printStackTrace();
			}
			catch (InterruptedException e) {
			}
		}

	}

}