package br.ufes.inf.nemo.chat;

/**
 * Interface de uma  sala de bate papo que será acessada remotamente.
 * 
 * Clientes postam mensagens (do tipo Message) através de postMessage, e
 * devem, periodicamente, chamar lastMessageNumber e getMessages para obter
 * as mensagens (estratégia denominada "polling").
 * 
 * Esta interface deve ser implementada por um objeto a ser "exportado" 
 * como objeto remoto.
 *  
 * @author jpalmeida
 *
 */
public interface ChatRoom extends java.rmi.Remote	 {

	void registerChatListener(ChatListener listener)
			throws java.rmi.RemoteException;
	
	/**
	 * Posta uma mensagem no chat room.
	 * 
	 * @param message
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	void postMessage(Message msg)
			throws java.rmi.RemoteException;
	
}

