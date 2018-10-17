package com.bellin.erp.supplychain.deliveryticket.domain.file;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class ReqFile {
    final static Charset ENCODING = StandardCharsets.UTF_8;

    //log
    //confObj
    private ArrayList<ReqFileLine> reqFileLines = new ArrayList<ReqFileLine>();

    //public ReqFile(confObj){}
    public ReqFile(){

    }

    public void read(String filePath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(filePath), ENCODING);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withTrim()
                        .withDelimiter('|'));
        ) {
            int index = 0;

            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> lineMap = csvRecord.toMap();

                //ReqFileLine reqFileLine = new ReqFileLine(index, confObj);
                ReqFileLine reqFileLine = new ReqFileLine(index);
                reqFileLine.read(lineMap);

                /*
                if (reqFileLine.isValid()) {
                    this.reqFileLines.add(reqFileLine);
                }
                else {
                    //TODO
                    System.out.println();
                }
                */
                this.reqFileLines.add(reqFileLine);

                index++;
            }
        } catch (IOException e) {
            //TODO: err handle
            System.err.println(e);
        }
    }
}
