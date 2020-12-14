package br.inf.ufes.ppd.Crack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public  class Config {

    static Properties  props;

    private static void Load(){
        props = new Properties();
        FileInputStream file = null;
        try {
            file = new FileInputStream(
                    "./properties/config.properties");

        props.load(file);
        } catch (Exception e) {
            System.out.println("Arquivo de configuração não encontrado.");
        }

    }
    public static String getProp(String key){
        if(props == null)
            Load();
        return props.getProperty(key);
    }
}
