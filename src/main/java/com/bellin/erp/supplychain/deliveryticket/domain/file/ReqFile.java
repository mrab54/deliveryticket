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
import java.util.List;
import java.util.Map;

public class ReqFile {
    final static Charset ENCODING = StandardCharsets.UTF_8;

    //log
    //confObj
    private ArrayList<ReqFileLine> reqFileLines = new ArrayList<ReqFileLine>();

    //public ReqFile(confObj){}
    public ReqFile(){

    }

    private List<CSVRecord> getCSVRecords(String filePath) {
        List<CSVRecord> csvRecords = null;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(filePath), ENCODING);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withTrim()
                        .withDelimiter('|'));
        ) {
            // Reading all records at once into memory
            csvRecords = csvParser.getRecords();
        } catch (IOException e) {
            //TODO: err handle
            System.err.println(e);
        }
        return csvRecords;
    }

    private ReqFileLine createReqFileLine(CSVRecord csvRecord, int index) {
        Map<String, String> lineMap = csvRecord.toMap();

        ReqFileLine reqFileLine = new ReqFileLine(index);
        reqFileLine.read(lineMap);

        return reqFileLine;
    }

    public void read(String filePath) {

        List<CSVRecord> csvRecords = this.getCSVRecords(filePath);

        for (int i = 0; i < csvRecords.size(); i++) {

            ReqFileLine reqFileLine = this.createReqFileLine(csvRecords.get(i), i);

            if (reqFileLine.isValid()) {
                this.reqFileLines.add(reqFileLine);
            }
            else {
                // TODO - log this
            }
        }
    }
}
