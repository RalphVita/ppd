package br.ufes.inf.nemo.chatserver;

import br.ufes.inf.nemo.chat.ChatRoom;
import br.ufes.inf.nemo.chat.Message;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	/**
	 * 
	 * No laboratório deve-se executar o ChatServer com opção
	 * java.rmi.server.hostname setada, da seguinte forma:
	 * 
	 * java -Djava.rmi.server.hostname=IP br.ufes.inf.nemo.chatserver.ChatServer
	 * 
	 * onde IP é o endereço IP da máquina servidora
	 * 
	 */
	public static void main(String[] args) {
		try {
			ChatRoomImpl obj = new ChatRoomImpl();
			obj.lstMessage = new ArrayList<Message>();
			ChatRoom objref = (ChatRoom) UnicastRemoteObject.exportObject(obj,0);
			// Bind the remote object in the registry
			Registry registry = LocateRegistry.getRegistry("localhost"); // opcional: host
			registry.rebind("ChatRoom", objref);
			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());  e.printStackTrace();
		}
	}
}
