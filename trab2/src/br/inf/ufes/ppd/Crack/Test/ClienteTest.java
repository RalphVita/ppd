package br.inf.ufes.ppd.Crack.Test;

import br.inf.ufes.ppd.Attacker;
import br.inf.ufes.ppd.Crack.Config;
import br.inf.ufes.ppd.Guess;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class ClienteTest {
    private static class TextPropert{
        public byte[] ciphertext;
        public byte[] knowntext;

        private TextPropert(byte[] ciphertext,byte[] knowntext) {
            this.ciphertext = ciphertext;
            this.knowntext = knowntext;
        }
    }

    private  static  class TestInfo{
        int size;
        double timeResponse;
        int p;
        int m;
        public TestInfo(int size,double timeResponse,int p, int m){
            this.size = size;
            this.timeResponse = timeResponse;
            this.p = p;
            this.m = m;
        }
        public String toCsvLine(){
            return m+";"+p+";"+size+";"+timeResponse;
        }

        public void appendCSV(){
            //Salva resultado do teste
            try{
                FileWriter writer = new FileWriter("testinfo.csv",true);
                writer.write(this.toCsvLine() + System.lineSeparator());
                writer.close();
            }catch (Exception ex){
                System.out.println("Erro ao salvar arquivo.");
            }
        }
    }

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        List<Integer> lstSizes = new ArrayList<Integer>(){{
            add(1_000);
            add(10_000);
            add(25_000);
            add(50_000);
        }};
        List<TestInfo> lstTestInfo = new ArrayList<>();

        //Granulalidade
        System.out.println("Granulalidade M: ");
        int m = s.nextInt();

        //Espera setar quantidade de maquinas
        System.out.println("N de maquinas: ");
        int p = s.nextInt();

        //Faz a avaliação por tamanho e quantidade de maquinas
        while(p != 0){
            for (int size:lstSizes) {
                TextPropert tp = GerarCipherText(size);
                long inicio = System.nanoTime();
                Conectar(tp);
                long fim = System.nanoTime();
                TestInfo tf = new TestInfo(size,(double) ((fim-inicio)/1_000_000),p,m);
                lstTestInfo.add(tf);
                tf.appendCSV();
            }
            System.out.println("N de maquinas: ");
            p = s.nextInt();
        }

        /*//Salva resultado do teste
        try{
        FileWriter writer = new FileWriter("testinfo.csv");
        for(TestInfo tf: lstTestInfo) {
            writer.write(tf.toCsvLine() + System.lineSeparator());
        }
        writer.close();
        }catch (Exception ex){
            System.out.println("Erro ao salvar arquivo.");
        }*/


    }
    private static void Conectar(TextPropert tp){
        try {
            Registry registry = LocateRegistry.getRegistry(Config.getProp("server.hostname"));
            Attacker master = (Attacker) registry.lookup(Config.getProp("master.name"));

            Guess[] guesses = master.attack(tp.ciphertext,tp.knowntext);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


    private  static  void SaveGuess(Guess guess){
        try {
            System.out.println(guess.getKey());
            FileOutputStream out = new FileOutputStream(guess.getKey()+".msg");

            out.write(guess.getMessage());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  static TextPropert  GerarCipherText(int maxByte){
        byte[] text = new byte[maxByte];
        new Random().nextBytes(text);
        byte[] knowntext = Arrays.copyOfRange(text,0,10);

        try {
            byte[] key = "none".getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");

            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] ciphertext = cipher.doFinal(text);
            return new TextPropert(ciphertext,knowntext);

        } catch (Exception e) {
            // don't try this at home
            e.printStackTrace();
        }
        return  null;
    }
}
