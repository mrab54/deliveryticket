package com.bellin.erp.supplychain.deliveryticket.domain.file;

import com.bellin.erp.supplychain.deliveryticket.exception.ReqFileLineFieldMissingException;
import com.bellin.erp.supplychain.deliveryticket.exception.ReqFileLineInvalidException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class ReqFile {
    private static final Logger logger = LoggerFactory.getLogger(ReqFile.class);
    private static final Charset ENCODING = StandardCharsets.UTF_8;
    private List<ReqFileLine> reqFileLines = new ArrayList<ReqFileLine>();
    private Map<String, Map<String, Object>> config;


    public ReqFile(Map<String, Map<String, Object>> config) {
        // TODO - build out Config class so this isn't just a nested map
        this.config = config;
    }

    private List<CSVRecord> getCSVRecords(String inputFilePath) throws IOException {
        List<CSVRecord> csvRecords;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath), ReqFile.ENCODING)) {
            // TODO - CSVParser is AutoClosable and needs to be in try with resources
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withTrim()
                    .withDelimiter('|'));

            ReqFile.logger.debug("Parsing CSV file...");
            csvRecords = csvParser.getRecords();
        }

        return csvRecords;
    }

    private ReqFileLine createReqFileLine(CSVRecord csvRecord, int index) throws ReqFileLineFieldMissingException {
        Map<String, String> lineMap = csvRecord.toMap();

        ReqFileLine reqFileLine = new ReqFileLine(index, config);
        reqFileLine.read(lineMap);

        return reqFileLine;
    }

    public void read(String inputFilePath) throws IOException, ReqFileLineInvalidException, ReqFileLineFieldMissingException {
        List<CSVRecord> csvRecords = getCSVRecords(inputFilePath);

        for (int i = 0; i < csvRecords.size(); i++) {
            ReqFileLine reqFileLine = createReqFileLine(csvRecords.get(i), i);

            if (reqFileLine.isValid()) {
                reqFileLines.add(reqFileLine);
            }
            else {
                throw new ReqFileLineInvalidException("Invalid ReqFileLine at line index " + i);
            }
        }
    }

    public List<ReqFileLine> getReqFileLines() {
        return reqFileLines;
    }
}
