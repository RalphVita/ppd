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

	/**
	 * Retorna o número atribuído à última mensagem recebida no chat room. 
	 * Retorna 0 quando não houver mensagens, 1 quando houver uma mensagem recebida, 
	 * e assim por diante.
	 * 
	 * @return número atribuído à última mensagem recebida
	 * @throws java.rmi.RemoteException
	 */
	int lastMessageNumber() 
			throws java.rmi.RemoteException;

	/**
	 * Retorna a lista de mensagens desde a mensagem especificada.
	 * 
	 * O cliente deve chamar esta operação periodicamente.
	 * 
	 * @param fromMessage número da primeira mensagem a ser retornada (iniciando em 0)
	 * @return lista de mensagens desde a mensagem de número fromMessage (pode ser vazia)
	 * @throws java.rmi.RemoteException
	 */
	java.util.List<Message> getMessages(int fromMessage)
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

