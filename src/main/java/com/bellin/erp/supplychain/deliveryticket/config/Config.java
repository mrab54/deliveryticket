package com.bellin.erp.supplychain.deliveryticket.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class Config {

    public static Map<String, Map<String, Object>> getFileLineFieldConfig() {
        Yaml yaml = new Yaml();

        Class currentClass = new Object( ){}.getClass().getEnclosingClass();
        InputStream inputStream = currentClass
                .getClassLoader()
                .getResourceAsStream("filelinefield.yaml");
        Map<String, Map<String, Object>> fileLineFieldConfig = yaml.load(inputStream);

        return fileLineFieldConfig;
    }


}
