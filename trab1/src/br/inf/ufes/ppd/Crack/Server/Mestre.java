package br.inf.ufes.ppd.Crack.Server;

import br.inf.ufes.ppd.Guess;
import br.inf.ufes.ppd.Master;
import br.inf.ufes.ppd.Slave;

import java.rmi.RemoteException;
import java.util.*;

public class Mestre implements Master {
    public class Tuple<X, Y> {
        public final X x;
        public final Y y;
        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    Map<UUID, Tuple<Slave, String> > mapSlavers;


    public  Mestre(){
        this.mapSlavers = new HashMap<UUID,Tuple<Slave, String>>();
    }
    /**
     * Operação oferecida pelo mestre para iniciar um ataque.
     *
     * @param ciphertext mensagem critografada
     * @param knowntext  trecho conhecido da mensagem decriptografada
     * @return vetor de chutes: chaves candidatas e mensagem
     * decriptografada com chaves candidatas
     */
    @Override
    public Guess[] attack(byte[] ciphertext, byte[] knowntext) throws RemoteException {
        return new Guess[0];
    }

    /**
     * Registra escravo no mestre. Deve ser chamada a cada 30s por um escravo
     * para se re-registrar.
     * <p>
     * Note que o escravo deve gerar uma chave ao se registrar pela primeira vez
     * apenas usando java.util.UUID.randomUUID()
     *
     * @param s         referência para o escravo
     * @param slaveName nome descritivo para o escravo
     * @param slavekey
     * @throws RemoteException
     */
    @Override
    public void addSlave(Slave s, String slaveName, UUID slavekey) throws RemoteException {
        if(!mapSlavers.containsKey(slavekey))
            mapSlavers.put(slavekey, new Tuple<>(s, slaveName));
    }

    /**
     * Desegistra escravo no mestre.
     *
     * @param slaveKey chave que identifica o escravo
     * @throws RemoteException
     */
    @Override
    public void removeSlave(UUID slaveKey) throws RemoteException {
        if(!mapSlavers.containsKey(slaveKey))
            mapSlavers.remove(slaveKey);
    }

    /**
     * Indica para o mestre que o escravo achou uma chave candidata.
     *
     * @param slaveKey     chave que identifica o escravo
     * @param attackNumber chave que identifica o ataque
     * @param currentindex índice da chave candidata no dicionário
     * @param currentguess chute que inclui chave candidata e mensagem
     *                     decriptografada com a chave candidata
     * @throws RemoteException
     */
    @Override
    public void foundGuess(UUID slaveKey, int attackNumber, long currentindex, Guess currentguess) throws RemoteException {

    }

    /**
     * Chamado por cada escravo a cada 10s durante ataque para indicar progresso
     * no ataque, e ao final do ataque.
     *
     * @param slaveKey     chave que identifica o escravo
     * @param attackNumber chave que identifica o ataque
     * @param currentindex índice da chave já verificada
     * @throws RemoteException
     */
    @Override
    public void checkpoint(UUID slaveKey, int attackNumber, long currentindex) throws RemoteException {

    }
}
