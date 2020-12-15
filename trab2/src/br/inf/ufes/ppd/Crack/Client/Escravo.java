package br.inf.ufes.ppd.Crack.Client;

import br.inf.ufes.ppd.*;
import br.inf.ufes.ppd.Crack.Config;
import br.inf.ufes.ppd.Crack.GuessOuterClass;
import br.inf.ufes.ppd.Crack.SubAttackOuterClass;
import com.google.protobuf.ByteString;
import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.jmq.jmsclient.BytesMessageImpl;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.jms.*;
import java.io.ByteArrayOutputStream;
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

public class Escravo{
    String nome;

    List<String> dictionary;

    UUID id;

    JMSContext context;
    JMSConsumer consumer;
    JMSProducer producer;
    Queue queue;
    Queue queueGuessesQueue;

    public Escravo(String nome){
        LoadDictionary();
        FindQueue();
        id = java.util.UUID.randomUUID();
        this.nome = nome;
    }

    private void FindQueue(){
        try {
            Registry registry = LocateRegistry.getRegistry(Config.getProp("glassfish.hostname"));

            String host = Config.getProp("glassfish.hostname");

            System.out.println("obtaining connection factory...");
            com.sun.messaging.ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
            connectionFactory.setProperty(ConnectionConfiguration.imqAddressList, host);
            // esta configuração pode ser importante para evitar que o middleware "puxe" (faça o "prefetch")
            // de várias mensagens
            connectionFactory.setProperty(ConnectionConfiguration.imqConsumerFlowLimitPrefetch,"false");

            System.out.println("obtained connection factory.");

            System.out.println("obtaining queue SubAttacksQueue...");
            queue = new com.sun.messaging.Queue("SubAttacksQueue");
            System.out.println("obtained queue SubAttacksQueue.");

            System.out.println("obtaining queue GuessesQueue...");
            queueGuessesQueue = new com.sun.messaging.Queue("GuessesQueue");
            System.out.println("obtained queue GuessesQueue.");

            context = connectionFactory.createContext();

            producer = context.createProducer();

            consumer = context.createConsumer(queue);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void Consomir(){

        try {
            while (true)
            {

                Message message = consumer.receive();

                if(message instanceof BytesMessage) {
                    BytesMessage m = (BytesMessage) message;

                try{

                    byte[] payload = new byte[(int) m.getBodyLength()];
                    m.readBytes(payload);
                    SubAttackOuterClass.SubAttack s = SubAttackOuterClass.SubAttack.parseFrom(payload); //((TextMessage)m).getText().getBytes());

                    System.out.print("\nreceived message: ");
                    System.out.println(s.getInitialwordindex());

                    startSubAttack(s.getCiphertext().toByteArray(),s.getKnowntext().toByteArray(),s.getInitialwordindex(),s.getFinalwordindex(),s.getAttackNumbe());

                } catch (Exception e) {
                    System.err.println("Client exception: " + e.toString());
                    e.printStackTrace();
                }
                }
            }
        } catch (Exception e) {
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
     * @throws RemoteException
     */
    public void startSubAttack(byte[] ciphertext, byte[] knowntext, long initialwordindex, long finalwordindex, int attackNumber)  {
                try {
                    int currentIndex = (int) initialwordindex;

                    for (; currentIndex <= finalwordindex; currentIndex++) {
                        byte[] gessKey = dictionary.get(currentIndex).getBytes();
                        try{
                            byte[] gesstext = Decrypt.Decript(ciphertext, gessKey);
                            if (new String(gesstext).contains(new String(knowntext)))
                                AddGuessQueue(attackNumber,currentIndex,dictionary.get(currentIndex),gesstext);
                        }
                        catch (javax.crypto.BadPaddingException ex) {}
                    }
                    //Passou do máximo
                    currentIndex--;
                    FinalizaAtaque(attackNumber);

                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    private void AddGuessQueue(int attackNumber, int currentindex, String key, byte[] message){
        System.err.println("Inicio AddGuessQueue");
        try {
            GuessOuterClass.Guess g = GuessOuterClass.Guess.newBuilder()
                    .setAttackNumber(attackNumber)
                    .setCurrentindex(currentindex)
                    .setKey(key)
                    .setMessage(ByteString.copyFrom(message))
                    .build();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            g.writeTo(baos);

            producer.send(queueGuessesQueue,baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Fim AddGuessQueue");
    }

    private void FinalizaAtaque(int attackNumber){
        System.err.println("Finaliza ataque ");
        try {
            GuessOuterClass.Guess g = GuessOuterClass.Guess.newBuilder()
                    .setDone(true)
                    .setAttackNumber(attackNumber)
                    .build();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            g.writeTo(baos);
            //TextMessage message = context.createTextMessage();
            //message.setText(s.toString());
            producer.send(queueGuessesQueue,baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Fim Finaliza ataque ");
    }


    private void LoadDictionary(){
        try {
            dictionary = Files.readAllLines(Paths.get("dictionary.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
