package br.inf.ufes.ppd.Crack;

import br.inf.ufes.ppd.Slave;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.UUID;

public class EscravoStatus {
    private final Slave slave;
    private final String nome;
    private final UUID id;

    private long lastCheck;

    private boolean processando;

    public EscravoStatus(Slave slave, String nome, UUID id) {
        this.slave = slave;
        this.nome = nome;
        this.lastCheck = System.nanoTime();
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
        return (lastCheck-System.nanoTime()) <= 020_000_000_000;
    }

    public double getDiffTimeMiliSeconds() {
        return (System.nanoTime()-lastCheck)/1_000_000;
    }

    public void Checking(){
        this.lastCheck = System.nanoTime();
    }
}