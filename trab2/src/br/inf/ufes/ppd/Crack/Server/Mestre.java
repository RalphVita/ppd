package br.inf.ufes.ppd.Crack.Server;

import br.inf.ufes.ppd.Crack.*;
import br.inf.ufes.ppd.Crack.Client.Escravo;
import br.inf.ufes.ppd.Guess;
import br.inf.ufes.ppd.Master;
import br.inf.ufes.ppd.Slave;
import br.inf.ufes.ppd.SlaveManager;
import com.google.protobuf.ByteString;
import com.sun.messaging.ConnectionConfiguration;
import org.w3c.dom.ranges.Range;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.*;
import javax.jms.Queue;

import com.sun.messaging.ConnectionConfiguration;

public class Mestre implements Master,MessageListener {

    public  Mestre(){
        this.mapGuess = new HashMap<Integer, List<Guess>>();
        this.mapCountSubAttackDone = new HashMap<Integer, AtomicInteger >();
        LoadDictionary();
    }

    //Chaves candidatas por requisição
    static Map<Integer, List<Guess> > mapGuess;

    static Map<Integer, AtomicInteger> mapCountSubAttackDone;

    static Integer serverID;

    List<String> dictionary;

    JMSContext context;
    JMSProducer producer;

    javax.jms.Queue queue;
    Queue queueGuessesQueue;

    public  void GetQueue(){
        try{
            //Gera um ID pra esse servidor
            serverID = new Random().nextInt();

            Logger.getLogger("").setLevel(Level.SEVERE);

            String host = Config.getProp("glassfish.hostname");

            System.out.println("obtaining connection factory...");
            com.sun.messaging.ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
            connectionFactory.setProperty(ConnectionConfiguration.imqAddressList,host+":7676");
            System.out.println("obtained connection factory.");

            System.out.println("obtaining queue SubAttacksQueue...");
            queue = new com.sun.messaging.Queue("SubAttacksQueue");
            System.out.println("obtained queue SubAttacksQueue.");

            context = connectionFactory.createContext();
            producer = context.createProducer();


            System.out.println("obtaining queue GuessesQueue...");
            queueGuessesQueue = new com.sun.messaging.Queue("GuessesQueue");
            System.out.println("obtained queue GuessesQueue.");


            JMSConsumer consumer = context.createConsumer(queueGuessesQueue, "serverID = "+serverID);

            MessageListener listener = new Mestre();
            consumer.setMessageListener(listener);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void GerarRange(byte[] ciphertext, byte[] knowntext,int attackNumber){
            int passo = Integer.parseInt(Config.getProp("m"));
            int max = dictionary.size();
            int count = 0;
            //Caso seja pra rodar tudo numa maquina sequencialamente
            if (!Boolean.parseBoolean(Config.getProp("run.parallel"))){
                count++;
                synchronized (mapCountSubAttackDone){
                    mapCountSubAttackDone.put(attackNumber,new AtomicInteger(count));}
                AddSubAttackQueue(ciphertext,knowntext,0,max,attackNumber);

            }
            else{//Gera faixas para serem rodadas paralelamente
                for (int i = -1; i < max; i += (passo)){
                    count++;
                }
                synchronized (mapCountSubAttackDone){
                    mapCountSubAttackDone.put(attackNumber,new AtomicInteger(count));}
                for (int i = -1; i < max; i += (passo)){
                    AddSubAttackQueue(ciphertext,knowntext,i + 1, (i + passo) > max ? max - 1 : i + passo,attackNumber);
                }
            }

    }

    private void AddSubAttackQueue(byte[] ciphertext, byte[] knowntext,long initialwordindex, long finalwordindex, int attackNumber){
        try {
            SubAttackOuterClass.SubAttack s = SubAttackOuterClass.SubAttack.newBuilder()
                .setCiphertext(ByteString.copyFrom(ciphertext))
                .setKnowntext(ByteString.copyFrom(knowntext))
                .setInitialwordindex((int)initialwordindex)
                .setFinalwordindex((int)finalwordindex)
                .setAttackNumbe(attackNumber)
                .setServerID(serverID)
                .build();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            s.writeTo(baos);

            producer.send(queue,baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        //Gera um ID pro ataque
        int attackNumber = new Random().nextInt();

        System.out.println("Iniciando ataque: "+attackNumber);


        synchronized (mapGuess){
            mapGuess.put(attackNumber,new ArrayList<Guess>()); }

        GerarRange(ciphertext,knowntext,attackNumber);


        try{
            synchronized (mapCountSubAttackDone.get(attackNumber)){
                mapCountSubAttackDone.get(attackNumber).wait();
            }
        } catch (Exception e) {
            System.err.println("Mestre wait mapCountSubAttackDone exception: " + e.toString());
            e.printStackTrace();
        }


        System.out.println("Finalizando ataque: " + attackNumber + " -> Quantidade de chaves possíveis: " + mapGuess.get(attackNumber).size());
        return mapGuess.get(attackNumber).stream().toArray(Guess[]::new);

    }



    /**
     * Indica para o mestre que o escravo achou uma chave candidata.
     *
     * @param attackNumber chave que identifica o ataque
     * @param currentindex índice da chave candidata no dicionário
     * @param currentguess chute que inclui chave candidata e mensagem
     *                     decriptografada com a chave candidata
     */
    public void foundGuess(int attackNumber, long currentindex, Guess currentguess){
        synchronized (mapGuess){
            mapGuess.get(attackNumber).add(currentguess);
        }
        //Checkpoint
        //checkpoint(slaveKey,attackNumber,currentindex);
    }



    public List<Guess> getMapGuess(int attackNumber) {
        return mapGuess.get(attackNumber);
    }


    private void LoadDictionary(){
        try {
            dictionary = Files.readAllLines(Paths.get("dictionary.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessage(Message message) {
        if(message instanceof BytesMessage) {


            try{
                BytesMessage m = (BytesMessage) message;

                byte[] payload = new byte[(int) m.getBodyLength()];
                m.readBytes(payload);

                GuessOuterClass.Guess g = GuessOuterClass.Guess.parseFrom(payload);//((TextMessage)message).getText().getBytes());

                if(g.hasDone() && g.getDone()){
                    synchronized (mapCountSubAttackDone.get(g.getAttackNumber())) {
                        mapCountSubAttackDone.get(g.getAttackNumber()).decrementAndGet();
                        if(mapCountSubAttackDone.get(g.getAttackNumber()).get() == 0)
                            mapCountSubAttackDone.get(g.getAttackNumber()).notifyAll();
                    }
                }
                else{
                    System.err.println("Chave candidata => Escravo: " + g.getNameSlave() + " -> Index: " + g.getCurrentindex() + " -> "+ g.getKey()+" Ataque: "+g.getAttackNumber() );
                    Guess guess = new Guess();
                    guess.setKey(g.getKey());
                    guess.setMessage(g.getMessage().toByteArray());
                    foundGuess(g.getAttackNumber(),g.getCurrentindex(), guess);
                }

            } catch (Exception e) {
                System.err.println("Mestre onMessage exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
}
