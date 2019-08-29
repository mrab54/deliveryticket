package com.bellin.erp.supplychain.deliveryticket;


import com.bellin.erp.supplychain.deliveryticket.config.Config;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.exception.ReqFileException;
import com.bellin.erp.supplychain.deliveryticket.report.ReportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;


/**
 * Hello world!
 *
 */
@SuppressWarnings("ALL")
public class App
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String configFileName = "filelinefield.yaml";
    //final static String INPUT_FILE_NAME = "D:\\ipaoutput\\SHIPMENTRELEASE\\whsdata-0000015809.csv";
    //final static String INPUT_FILE_NAME = "C:\\Users\\mrab\\dev\\code\\java\\deliveryticket\\whsdata-300GS-24168.csv";
    //final static String INPUT_FILE_NAME = "D:\\ipaoutput\\PickTicket\\whsdata-300GS-24168.csv";
    //final static String INPUT_FILE_NAME = "D:\\ipaoutput\\PickTicket\\whsdata-300CS-41122.csv";
    //final static String INPUT_FILE_NAME = "D:\\ipaoutput\\PickTicket\\whsdata-300CS-41110.csv";
    private static final String INPUT_FILE_NAME = "C:\\Users\\MDRABA\\Documents\\dev\\code\\java\\deliveryticket\\deliveryticket\\src\\main\\resources\\whsdata-300CS-41110.csv";

    public static void main( String[] args )
    {
        App.logger.info("Start");
        String test = "1234";
        boolean bMatches = test.matches("\\d+");
        App app = new App();

        try {
            //String inputFileName = args[0];
            String inputFileName = App.INPUT_FILE_NAME;
            app.runit(inputFileName);
        } catch (Exception e) {
            App.logger.error("Unhandled error", e);
        }
        App.logger.info("End");
    }


    private void runit(String inputFilePath) {
        Map<String, Map<String, Object>> filelineFieldConfig = null;

        try {
            filelineFieldConfig = Config.getFileLineFieldConfig(App.configFileName);
        } catch (IOException e) {
            App.logger.error("Error reading config file {}", App.configFileName);
        }

        ReqFile reqFile = new ReqFile(filelineFieldConfig);
        try {
            reqFile.read(inputFilePath);
        } catch (ReqFileException | IOException e) {
            App.logger.error("Error reading ReqFile", e);
            System.exit(1);
        }

        String outputFilePath = inputFilePath.replace("whsdata", "whsrpt").replace("csv", "txt");

        ReportWriter rw = new ReportWriter();
        rw.writeDeliveryTicket(reqFile, outputFilePath);
    }
}
