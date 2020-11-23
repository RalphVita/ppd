package br.inf.ufes.ppd.Crack;

import br.inf.ufes.ppd.Slave;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.UUID;

public class EscravoStatus {
    private final Slave slave;
    private final String nome;
    private final UUID id;

    private Timestamp lastCheck;

    private boolean processando;

    public EscravoStatus(Slave slave, String nome, UUID id) {
        this.slave = slave;
        this.nome = nome;
        this.lastCheck = new Timestamp(System.currentTimeMillis());
        processando = false;
        this.id = id;
    }

    public void setProcessando(boolean processando) {
        this.processando = processando;
    }

    public boolean IsProcessando() {
        return this.processando;
    }

    public Slave getSlave() {
        return slave;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public boolean HasActive(){
        return ((lastCheck.getTime()-System.currentTimeMillis())) <= 20000;
    }

    public void Checking(){
        this.lastCheck.setTime(System.currentTimeMillis());
    }
}