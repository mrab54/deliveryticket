package com.bellin.erp.supplychain.deliveryticket;


import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqLine;
import org.apache.commons.csv.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Reader;


/**
 * Hello world!
 *
 */
public class App 
{
    final static String[] HEADERS = { "author", "title" };
    final static String INPUT_FILE_NAME = ".\\res\\input\\0000015809.csv";
    final static String OUTPUT_FILE_NAME = ".\\res\\output\\0000015809.txt";
    final static Charset ENCODING = StandardCharsets.UTF_8;

    // [company, reqNumber, lineNumber, item, desc
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App app = new App();

        try {
            app.apacheCSV("abc");
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public void apacheCSV(String fileName) throws IOException {
        ArrayList<ReqLine> reqLines = new ArrayList<>();

        try (
                Reader reader = Files.newBufferedReader(Paths.get(INPUT_FILE_NAME), ENCODING);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withTrim()
                        .withDelimiter('|'));
        ) {
            for (CSVRecord csvRecord : csvParser) {
                reqLines.add(new ReqLine(csvRecord));
                /*
                Map<String, String> reqLine = csvRecord.toMap();

                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("---------------");

                for (String key : reqLine.keySet()) {
                    System.out.println(key + ": " + reqLine.get(key));
                }

                System.out.println("---------------\n\n");
                */


                // Accessing values by Header names
                /*
                String one = csvRecord.get("One");
                String two = csvRecord.get("Two");
                String three = csvRecord.get("three");
                String four = csvRecord.get("four");
                String five = csvRecord.get("five");

                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("---------------");
                System.out.println("one : " + one);
                System.out.println("two : " + two);
                System.out.println("three : " + three);
                System.out.println("four : " + four);
                System.out.println("five : " + five);
                System.out.println("---------------\n\n");
                */
            }
        }


        /*
        List<String> reqLines = Files.readAllLines(Paths.get(INPUT_FILE_NAME), ENCODING);

        for (String line : reqLines) {
            System.out.println(line);
        }
        */




        /*
        Reader in = new FileReader(fileName);

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withDelimiter('|')
                .withFirstRecordAsHeader()
                .parse(in);
        for(CSVRecord record : records) {
            String author = record.get("author");
            String title = record.get("title");
            System.out.println(author);
            System.out.println(title);
        }
        */

    }
}
