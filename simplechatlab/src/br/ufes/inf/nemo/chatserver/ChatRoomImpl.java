package br.ufes.inf.nemo.chatserver;

import br.ufes.inf.nemo.chat.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomImpl implements ChatRoom {
	List<Message> lstMessage;
	int count = 0;
	/**
	 * Retorna o número atribuído à última mensagem recebida no chat room.
	 * Retorna 0 quando não houver mensagens, 1 quando houver uma mensagem recebida,
	 * e assim por diante.
	 *
	 * @return número atribuído à última mensagem recebida
	 * @throws RemoteException
	 */
	@Override
	public int lastMessageNumber() throws RemoteException {
		return count;
	}

	/**
	 * Retorna a lista de mensagens desde a mensagem especificada.
	 * <p>
	 * O cliente deve chamar esta operação periodicamente.
	 *
	 * @param fromMessage número da primeira mensagem a ser retornada (iniciando em 0)
	 * @return lista de mensagens desde a mensagem de número fromMessage (pode ser vazia)
	 * @throws RemoteException
	 */
	@Override
	public List<Message> getMessages(int fromMessage) throws RemoteException {
		//System.out.println(fromMessage+ ": " + lstMessage.size());
		if(count > 0  && fromMessage < count)
			return new ArrayList<Message>(lstMessage.subList(fromMessage,count));
		else
			return  new ArrayList<Message>();
	}

	/**
	 * Posta uma mensagem no chat room.
	 *
	 * @param msg@return
	 * @throws RemoteException
	 */
	@Override
	public void postMessage(Message msg) throws RemoteException {
		lstMessage.add(msg);
		count++;
	}
	/**
	 * TODO
	 * 
	 * Exercício (1/2)
	 * 
	 * Implementar a interface ChatRoom na class ChatRoomImpl.
	 * Este é o código que executa a "lógica da aplicação", ou seja, 
	 * a classe que vai ser instanciada para criar o objeto remoto.
	 * 
	 */
}
