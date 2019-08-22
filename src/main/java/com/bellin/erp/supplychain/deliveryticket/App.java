package com.bellin.erp.supplychain.deliveryticket;


import com.bellin.erp.supplychain.deliveryticket.config.Config;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.report.ReportWriter;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


/**
 * Hello world!
 *
 */
public class App 
{
    //final static String INPUT_FILE_NAME = "D:\\ipaoutput\\SHIPMENTRELEASE\\whsdata-0000015809.csv";
    final static String INPUT_FILE_NAME = "C:\\Users\\mrab\\dev\\code\\java\\deliveryticket\\whsdata-300GS-24168.csv";
    //final static String INPUT_FILE_NAME = "D:\\ipaoutput\\PickTicket\\whsdata-300GS-24168.csv";
    final static String OUTPUT_FILE_NAME = ".\\res\\output\\0000015809.txt";

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App app = new App();

        try {
            //app.runit(args[0]);
            app.runit(INPUT_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void runit(String inputFilePath) {
        Map<String, Map<String, Object>> filelineFieldConfig = Config.getFileLineFieldConfig();

        ReqFile reqFile = new ReqFile(filelineFieldConfig);
        reqFile.read(inputFilePath);

        // TODO - fix outputFilename, just replace whsdata with whsrpt and csv with txt
        String outputFilePath = inputFilePath.replace("whsdata", "whsrpt").replace("csv", "txt");
        ReportWriter rw = new ReportWriter();
        rw.writeDeliveryTicket(reqFile, outputFilePath);


        System.err.println("this is an error");
        System.out.println(inputFilePath);
        System.exit(0);
    }


}
