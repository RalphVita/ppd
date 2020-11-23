package br.inf.ufes.ppd.Crack.Server;

import br.inf.ufes.ppd.Crack.Client.Escravo;
import br.inf.ufes.ppd.Crack.EscravoStatus;
import br.inf.ufes.ppd.Crack.RangeAtack;
import br.inf.ufes.ppd.Guess;
import br.inf.ufes.ppd.Master;
import br.inf.ufes.ppd.Slave;
import br.inf.ufes.ppd.SlaveManager;
import org.w3c.dom.ranges.Range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;

public class Mestre implements Master {

    Queue<EscravoStatus> SlaversOcioso;

    Map<UUID, EscravoStatus> mapSlavers;

    //Chaves candidatas por requisição
    Map<Integer, List<Guess> > mapGuess;

    Map<Integer, Queue<RangeAtack>> mapRangeAtack;

    Map<Integer, List<RangeAtack>> rangeAtackAtivos;

    List<String> dictionary;



    private Queue<RangeAtack> GerarRange(){
        Queue<RangeAtack> lstRangeAtack = new LinkedList<>();

        int passo = 1000;
        int max = dictionary.size();
        for(int i = -1; i < max; i+=1000){
            RangeAtack range = new RangeAtack(i+1, (i + passo) > max ? max : i+passo);
            lstRangeAtack.add(range);
        }
        return lstRangeAtack;
    }




    public  Mestre(){
        this.mapSlavers = new HashMap<UUID,EscravoStatus>();
        this.mapGuess = new HashMap<Integer, List<Guess>>();
        this.mapRangeAtack = new HashMap<Integer, Queue<RangeAtack>>();
        this.rangeAtackAtivos = new HashMap<Integer, List<RangeAtack>>();
        this.SlaversOcioso = new LinkedList<>();
        LoadDictionary();
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
        //Inicialisa Maps
        int attackNumber = new Random().nextInt();
        System.out.println("Iniciando ataque: "+attackNumber);
        synchronized (mapGuess){
            mapGuess.put(attackNumber,new ArrayList<Guess>()); }
        synchronized (mapRangeAtack){
            mapRangeAtack.put(attackNumber,GerarRange());}
        synchronized (rangeAtackAtivos){
            rangeAtackAtivos.put(attackNumber,new ArrayList<RangeAtack>());}

        try {
            //Intera sobre a lista de range neste ataque
            while(this.mapRangeAtack.get(attackNumber).size() > 0)
            {
                //Pega um range de ataque na fila
                RangeAtack range = this.mapRangeAtack.get(attackNumber).remove();

                //Lista de Ranges em processamento
                if(!rangeAtackAtivos.get(attackNumber).contains(range))
                    rangeAtackAtivos.get(attackNumber).add(range);

                EscravoStatus escravoStatus = null;
                synchronized (SlaversOcioso){

                    //Espera liberar um escravo
                    if(SlaversOcioso.size() == 0)
                        SlaversOcioso.wait();

                    escravoStatus = SlaversOcioso.remove();
                }
                //Envia um trabalho pra um escravo
                range.Init(escravoStatus,this,ciphertext,knowntext,attackNumber);
                range.start();

                //
                if(this.mapRangeAtack.get(attackNumber).size() == 0)
                    synchronized (mapGuess.get(attackNumber)){
                    mapGuess.get(attackNumber).wait();
                }
                    /*synchronized (mapRangeAtack.get(attackNumber)){
                        mapRangeAtack.get(attackNumber).wait();
                    }*/
                    /*rangeAtackAtivos.get(attackNumber)
                            .stream()
                            .filter(r -> !r.Done())
                            .forEach(r-> {
                                try {
                                     System.out.println("-------Esperando --------"+r.isAlive());
                                    synchronized (r){
                                    r.wait();
                                    }
                                    System.out.println("-------Done --------"+r.isAlive());
                                } catch (InterruptedException e) {
                                    //Remove escravo do mestre, e volta pra fila
                                    try {
                                        this.removeSlave(r.getEscravo().getId());
                                    } catch (RemoteException remoteException) {
                                        remoteException.printStackTrace();
                                    }
                                    this.VoltarPraFilaAtaque(attackNumber, r);
                                }
                            });*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*synchronized (mapGuess.get(attackNumber)) {
            try {
                mapGuess.get(attackNumber).wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        System.out.println("Finalizando ataque: " + attackNumber + " -> Quantidade de chaves possíveis: " + mapGuess.get(attackNumber).size());
        System.out.println(rangeAtackAtivos.get(attackNumber).size());
        return mapGuess.get(attackNumber).stream().toArray(Guess[]::new);

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
        if(!mapSlavers.containsKey(slavekey)){
            EscravoStatus escravo = new EscravoStatus(s, slaveName,slavekey);
            synchronized (mapSlavers){
                mapSlavers.put(slavekey, escravo);
            }
            synchronized (SlaversOcioso){
                SlaversOcioso.add(escravo);
                SlaversOcioso.notifyAll();
            }
        }
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
            System.out.println("Removendo escravo: "+mapSlavers.get(slaveKey).getNome());
            synchronized (mapSlavers){
                mapSlavers.remove(slaveKey);
        }
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
        synchronized (mapGuess){
            mapGuess.get(attackNumber).add(currentguess);
        }
        //Checkpoint
        checkpoint(slaveKey,attackNumber,currentindex);
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
        System.err.println("Checkpoint => Escravo: " + mapSlavers.get(slaveKey).getNome() + " -> Index: " + currentindex + " -> "+ dictionary.get(currentindex < dictionary.size () ? (int)currentindex : (int)(currentindex-1))+" Ataque: "+attackNumber);

        //Faz checking
        mapSlavers.get(slaveKey).Checking();

        //Atualiza currentindex
        rangeAtackAtivos.get(attackNumber).stream()
                .filter(r -> r.IndexInRange((int)currentindex))
                .findFirst().ifPresent(r -> {
                    r.setCurrentIndex((int)currentindex);
                    if(r.Done()){
                        synchronized (SlaversOcioso){
                        SlaversOcioso.add(r.getEscravo());
                        SlaversOcioso.notifyAll();
                    }
                    if(FinalizouTudo(attackNumber))
                        synchronized (mapGuess.get(attackNumber)){
                            mapGuess.get(attackNumber).notifyAll();
                        }
                    }
                });

    }

    public List<Guess> getMapGuess(int attackNumber) {
        return mapGuess.get(attackNumber);
    }

    public boolean FinalizouTudo(int attackNumber){
        return  rangeAtackAtivos.get(attackNumber)
                .stream()
                .filter(r -> !r.Done()).count() == 0;
    }

    public void VoltarPraFilaAtaque(int attackNumber, RangeAtack range){
        synchronized (mapRangeAtack) {
            mapRangeAtack.get(attackNumber).add(range);
        }
    }

    private void LoadDictionary(){
        try {
            dictionary = Files.readAllLines(Paths.get("dictionary.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
