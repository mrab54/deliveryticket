package com.bellin.erp.supplychain.deliveryticket;


import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.report.ReportWriter;
import org.apache.commons.csv.*;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
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
    //final static String INPUT_FILE_NAME = "D:\\ipaoutput\\SHIPMENTRELEASE\\whsdata-0000015809.csv";
    final static String INPUT_FILE_NAME = "C:\\Users\\mrab\\dev\\code\\java\\deliveryticket\\whsdata-0000015809.csv";
    final static String OUTPUT_FILE_NAME = ".\\res\\output\\0000015809.txt";

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App app = new App();

        try {
            app.runit(args[0]);
            //app.runit(INPUT_FILE_NAME);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void runit(String filePath) {
        ReqFile reqFile = new ReqFile();
        reqFile.read(filePath);

        String outputFilename = "";
        Path inFilePath = Paths.get(filePath);
        Path inFileName = inFilePath.getFileName();
        Path inFileParentPath = inFilePath.getParent();

        String shipmentNumber = StringUtils.remove(inFileName.toString(), ".csv");
        shipmentNumber = StringUtils.remove(shipmentNumber, "whsdata-");

        String outFilePath = inFileParentPath.toString() + "\\whsrpt-" + shipmentNumber + ".txt";

        ReportWriter rw = new ReportWriter();
        rw.writeDeliveryTicket(reqFile, outFilePath);


        System.err.println("this is an error");
        System.out.println(filePath);
        System.exit(0);
    }


}
