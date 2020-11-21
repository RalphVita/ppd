package br.ufes.inf.nemo.chatserver;

import br.ufes.inf.nemo.chat.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomImpl implements ChatRoom {
	List<ChatListener> listeners;

	public  ChatRoomImpl(){
		this.listeners = new ArrayList<ChatListener>();
	}

	@Override
	public void registerChatListener(ChatListener listener) throws RemoteException {
		synchronized (listeners){listeners.add(listener);}
	}

	/**
	 * Posta uma mensagem no chat room.
	 *
	 * @param msg@return
	 * @throws RemoteException
	 */
	@Override
	public void postMessage(Message msg) throws RemoteException {
		List<ChatListener> failListeners = new ArrayList<ChatListener>();
		synchronized (listeners){
		for (ChatListener listener: listeners) {
			try {
				listener.notify(msg);
			}catch (java.rmi.RemoteException e){
				System.out.println("Falha ao contactar cliente!");
				failListeners.add(listener);
			}
		}}

		synchronized (listeners){listeners.removeAll(failListeners);}
	}
}
