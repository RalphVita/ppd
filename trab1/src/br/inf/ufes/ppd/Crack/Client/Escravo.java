package br.inf.ufes.ppd.Crack.Client;

import br.inf.ufes.ppd.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Escravo implements Slave {
    String nome;

    List<String> dictionary;

    SlaveManager master;

    UUID id;

    int currentIndex;

    public Escravo(String nome){
        LoadDictionary();
        FindMestre();
        id = java.util.UUID.randomUUID();
        this.nome = nome;
    }

    private void FindMestre(){
        try {
            Registry registry = LocateRegistry.getRegistry();
             this.master = (Master) registry.lookup("mestre");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void BaterPonto(){

        try {
            Slave objref = (Slave) UnicastRemoteObject.exportObject(this,0);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    try {
                        master.addSlave(objref, nome, id);
                    } catch (RemoteException e) {
                        FindMestre();
                        try {
                            master.addSlave(objref, nome, id);
                        } catch (RemoteException remoteException) {
                            remoteException.printStackTrace();
                        }
                    }
                }
            }, 0, 30000);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

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
        new Thread(){
            @Override
            public void run() {
                try {
                    currentIndex = (int) initialwordindex;

                    //Checkpoint
                    new Timer().scheduleAtFixedRate(new TimerTask() {
                        public void run() {
                            try {
                                callbackinterface.checkpoint(id,attackNumber,currentIndex);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 10000, 10000);

                    for (; currentIndex < finalwordindex; currentIndex++) {
                        byte[] gessKey = dictionary.get(currentIndex).getBytes();
                        try{
                        byte[] gesstext = Decrypt.Decript(ciphertext, gessKey);
                        if (new String(gesstext).contains(new String(knowntext))) {

                            System.err.println(dictionary.get(currentIndex));

                            Guess guess = new Guess();
                            guess.setKey(dictionary.get(currentIndex));
                            guess.setMessage(gesstext);
                            callbackinterface.foundGuess(id,attackNumber,currentIndex,guess);
                        }
                        }
                        catch (javax.crypto.BadPaddingException ex) {}
                    }

                    callbackinterface.checkpoint(id,attackNumber,currentIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void LoadDictionary(){
        try {
            dictionary = Files.readAllLines(Paths.get("dictionary.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
