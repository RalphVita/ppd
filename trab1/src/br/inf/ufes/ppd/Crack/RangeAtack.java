package br.inf.ufes.ppd.Crack;


import br.inf.ufes.ppd.SlaveManager;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class RangeAtack extends Thread{
    int start;
    int end;
    int currentIndex;

    byte[] ciphertext;
    byte[] knowntext;
    int attackNumber;
    SlaveManager master;

    EscravoStatus escravo;

    public  RangeAtack(int start, int end){
        this.currentIndex = this.start = start;
        this.end = end;
    }

    public void  Init(EscravoStatus escravo, SlaveManager master, byte[] ciphertext, byte[] knowntext, int attackNumber){
        this.escravo = escravo;
        this.escravo.setProcessando(true);
        this.ciphertext = ciphertext;
        this.knowntext = knowntext;
        this.master = master;
        this.attackNumber = attackNumber;
    }

    public boolean Done(){
        return  currentIndex == end;
    }

    public void setEscravo(EscravoStatus escravo) {
        this.escravo = escravo;
    }

    public EscravoStatus getEscravo() {
        return escravo;
    }

    public void run()
    {
        try {
            Monitorar();
            escravo.getSlave().startSubAttack(ciphertext,knowntext,start,end,attackNumber,master);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean IndexInRange(int index){
        return index >= start && index <= end;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    void Finalizar(){
        this.interrupt();
    }

    private void  Monitorar(){

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(escravo.HasActive())
                    Finalizar();
            }
        }, 20000, 20000);

    }


}
