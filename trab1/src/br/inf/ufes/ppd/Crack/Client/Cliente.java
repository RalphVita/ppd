package br.inf.ufes.ppd.Crack.Client;



import br.inf.ufes.ppd.Attacker;
import br.inf.ufes.ppd.Encrypt;
import br.inf.ufes.ppd.Guess;
import com.sun.xml.internal.ws.commons.xmlutil.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;

public class Cliente {

    static byte[] ciphertext;
    static byte[] knowntext;

    public static void main(String[] args) {

        try {
            String filename = (args.length < 1) ? null : args[0];
            ciphertext = Files.readAllBytes(Paths.get(args[0]));
            knowntext = (args.length < 1) ? null : args[1].getBytes();
        }catch (java.io.IOException ex){
            GerarText(args);

        }



        try {
            Registry registry = LocateRegistry.getRegistry();
            Attacker master = (Attacker) registry.lookup("mestre");

            Guess[] guess = master.attack(ciphertext,knowntext);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private  static void  GerarText(String[] args){
        int maxByte = (args.length < 1) ? 1000 : Integer.parseInt(args[2]);
        byte[] text = new byte[maxByte];
        new Random().nextBytes(text);
        knowntext = Arrays.copyOfRange(ciphertext,0,10);

        try {
            byte[] key = "none".getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");

            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            ciphertext = cipher.doFinal(text);

        } catch (Exception e) {
            // don't try this at home
            e.printStackTrace();
        }
    }
}
