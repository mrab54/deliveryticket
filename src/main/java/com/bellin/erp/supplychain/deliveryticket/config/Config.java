package com.bellin.erp.supplychain.deliveryticket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@SuppressWarnings("ALL")
public final class Config {

    public static Map<String, Map<String, Object>> getFileLineFieldConfig(String inputFileName) throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Map<String, Object>> fileLineFieldConfig;
        @SuppressWarnings("EmptyClass") Class currentClass = new Object( ){}.getClass().getEnclosingClass();

        try (InputStream inputStream = currentClass.getClassLoader().getResourceAsStream(inputFileName)) {
            fileLineFieldConfig = yaml.load(inputStream);
        }

        return fileLineFieldConfig;
    }


}
