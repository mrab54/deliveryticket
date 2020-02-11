package com.bellin.erp.supplychain.deliveryticket.domain.file;

import com.bellin.erp.supplychain.deliveryticket.exception.UFFileLineFieldMissingException;
import com.bellin.erp.supplychain.deliveryticket.exception.UFFileLineInvalidException;
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
public class UFFile {
    private static final Logger logger = LoggerFactory.getLogger(UFFile.class);
    private static final Charset ENCODING = StandardCharsets.UTF_8;
    private List<UFFileLine> ufFileLines = new ArrayList<UFFileLine>();
    private Map<String, Map<String, Object>> config;


    public UFFile(Map<String, Map<String, Object>> config) {
        // TODO - build out Config class so this isn't just a nested map
        this.config = config;
    }

    private List<CSVRecord> getCSVRecords(String inputFilePath) throws IOException {
        List<CSVRecord> csvRecords;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath), UFFile.ENCODING)) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withTrim()
                    .withDelimiter('|'));

            UFFile.logger.debug("Parsing CSV file...");
            csvRecords = csvParser.getRecords();
        }

        return csvRecords;
    }

    private UFFileLine createUFFileLine(CSVRecord csvRecord, int index) throws UFFileLineFieldMissingException {
        Map<String, String> lineMap = csvRecord.toMap();

        UFFileLine ufFileLine = new UFFileLine(index, config);
        ufFileLine.read(lineMap);

        return ufFileLine;
    }

    public void read(String inputFilePath) throws IOException, UFFileLineInvalidException, UFFileLineFieldMissingException {
        List<CSVRecord> csvRecords = getCSVRecords(inputFilePath);

        for (int i = 0; i < csvRecords.size(); i++) {
            UFFileLine ufFileLine = createUFFileLine(csvRecords.get(i), i);

            if (ufFileLine.isValid()) {
                ufFileLines.add(ufFileLine);
            }
            else {
                throw new UFFileLineInvalidException("Invalid UFFileLine at line index " + i);
            }
        }
    }

    public List<UFFileLine> getUFFileLines() {
        return ufFileLines;
    }
}
