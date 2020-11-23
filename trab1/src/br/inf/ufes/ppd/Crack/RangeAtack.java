package br.inf.ufes.ppd.Crack;


import br.inf.ufes.ppd.Crack.Server.Mestre;
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
    Mestre master;

    EscravoStatus escravo;
    Timer monitorar;

    public  RangeAtack(int start, int end){
        this.currentIndex = this.start = start;
        this.end = end;
    }

    public void  Init(EscravoStatus escravo, Mestre master, byte[] ciphertext, byte[] knowntext, int attackNumber){
        this.escravo = escravo;
        this.escravo.setProcessando(true);
        this.ciphertext = ciphertext;
        this.knowntext = knowntext;
        this.master = master;
        this.attackNumber = attackNumber;

        monitorar = new Timer();
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
            escravo.Checking();
            escravo.getSlave().startSubAttack(ciphertext,knowntext,start,end,attackNumber,(SlaveManager)master);
            
        } catch (RemoteException e) {
            try {
                this.master.removeSlave(escravo.getId());
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
        }
        finally {
            monitorar.cancel();
        }
    }

    public boolean IndexInRange(int index){
        return index >= start && index <= end;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    void Interromper(){
        this.interrupt();
    }

    private void  Monitorar(){
        monitorar.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(!escravo.HasActive())
                    Interromper();
            }
        }, 20000, 20000);

    }


}
