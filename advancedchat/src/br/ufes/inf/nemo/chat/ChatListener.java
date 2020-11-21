package br.ufes.inf.nemo.chat;

public interface ChatListener extends java.rmi.Remote {
    void notify(Message message)
        throws java.rmi.RemoteException;
}
