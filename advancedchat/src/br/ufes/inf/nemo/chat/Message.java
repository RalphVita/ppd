package br.ufes.inf.nemo.chat;

import java.io.Serializable;

/**
 * Mensagem a ser enviada pelo ChatRoom.
 * 
 * Plain-Old Java Object (POJO) com dois atributos String e 
 * getters e setters. Marcada para Serializable para ser
 * enviada por cópia na chamada remota.
 * 
 * @author jpalmeida
 *
 */
public class Message implements Serializable {
	private String content;
	private String sender;
	public Message(String content, String sender) {
		super();
		this.content = content;
		this.sender = sender;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
}
