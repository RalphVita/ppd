package br.inf.ufes.ppd.Crack.Client;

import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.*;

import com.sun.messaging.ConnectionConfiguration;

public class EscravoClient {
    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];

        try {

            Logger.getLogger("").setLevel(Level.INFO);


            Scanner s = new Scanner(System.in);

            System.out.println("Seu nome: ");
            String nome = s.nextLine();

            Escravo obj = new Escravo(nome);
            obj.BaterPonto();


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
