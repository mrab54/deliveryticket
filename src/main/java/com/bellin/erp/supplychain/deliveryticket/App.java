package com.bellin.erp.supplychain.deliveryticket;

import com.bellin.erp.supplychain.deliveryticket.config.Config;
import com.bellin.erp.supplychain.deliveryticket.domain.file.ReqFile;
import com.bellin.erp.supplychain.deliveryticket.domain.file.UFFile;
import com.bellin.erp.supplychain.deliveryticket.exception.ReqFileException;
import com.bellin.erp.supplychain.deliveryticket.exception.UFFileException;
import com.bellin.erp.supplychain.deliveryticket.report.ReportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;


@SuppressWarnings("ALL")
public class App
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String configFileName = "filelinefield.yaml";
    private static final String ufConfigFileName = "uffilelinefield.yaml";
    private static final String INPUT_FILE_NAME = "D:\\ipaoutput\\DeliveryTicket\\whsdata-300CS-51858.csv";
    private static final String UF_INPUT_FILE_NAME = "D:\\ipaoutput\\DeliveryTicket\\ufdata-300CS-51858.csv";

    public static void main( String[] args )
    {
        App app = new App();

        try {
            //String inputFileName = args[0];
            //String ufInputFileName = args[1];
            String inputFileName = App.INPUT_FILE_NAME;
            String ufInputFileName = App.UF_INPUT_FILE_NAME;
            App.logger.info("Processing: " + inputFileName);
            app.runit(inputFileName, ufInputFileName);
            App.logger.info("End: " + inputFileName);
        } catch (Exception e) {
            App.logger.error("Unhandled error", e);
            System.exit(1);
        }
    }


    private void runit(String inputFilePath, String ufInputFilePath) {
        Map<String, Map<String, Object>> filelineFieldConfig = null;
        Map<String, Map<String, Object>> ufFilelineFieldConfig = null;

        try {
            filelineFieldConfig = Config.getFileLineFieldConfig(App.configFileName);
        } catch (IOException e) {
            App.logger.error("Error reading config file {}", App.configFileName);
        }

        try {
            ufFilelineFieldConfig = Config.getFileLineFieldConfig(App.ufConfigFileName);
        } catch (IOException e) {
            App.logger.error("Error reading config file {}", App.ufConfigFileName);
        }

        ReqFile reqFile = new ReqFile(filelineFieldConfig);
        UFFile ufFile = new UFFile(ufFilelineFieldConfig);

        try {
            reqFile.read(inputFilePath);
        } catch (ReqFileException | IOException e) {
            App.logger.error("Error reading ReqFile", e);
            System.exit(1);
        }

        try {
            ufFile.read(ufInputFilePath);
        } catch (UFFileException | IOException e) {
            App.logger.error("Error reading UFFile", e);
            System.exit(1);
        }

        String outputFilePath = inputFilePath.replace("whsdata", "whsrpt").replace("csv", "txt");

        ReportWriter rw = new ReportWriter();
        try {
            rw.writeDeliveryTicket(reqFile, ufFile, outputFilePath);
        } catch (IOException e) {
            App.logger.error("Error writing delivery ticket", e);
            System.exit(1);
        }
    }
}
