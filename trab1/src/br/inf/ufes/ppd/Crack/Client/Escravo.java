package br.inf.ufes.ppd.Crack.Client;

import br.inf.ufes.ppd.Slave;
import br.inf.ufes.ppd.SlaveManager;

import java.rmi.RemoteException;

public class Escravo implements Slave {
    String nome;
    /**
     * Solicita a um escravo que inicie sua parte do ataque.
     *
     * @param ciphertext        mensagem critografada
     * @param knowntext         trecho conhecido da mensagem decriptografada
     * @param initialwordindex  índice inicial do trecho do dicionário
     *                          a ser considerado no sub-ataque.
     * @param finalwordindex    índice final do trecho do dicionário
     *                          a ser considerado no sub-ataque.
     * @param attackNumber      chave que identifica o ataque
     * @param callbackinterface interface do mestre para chamada de
     *                          checkpoint e foundGuess
     * @throws RemoteException
     */
    @Override
    public void startSubAttack(byte[] ciphertext, byte[] knowntext, long initialwordindex, long finalwordindex, int attackNumber, SlaveManager callbackinterface) throws RemoteException {

    }
}
