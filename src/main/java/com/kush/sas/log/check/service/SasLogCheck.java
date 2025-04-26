package com.kush.sas.log.check.service;

import static com.kush.sas.log.check.view.Components.DATE_PATTERN;

import com.kush.sas.log.check.constant.BadMessages;
import com.kush.sas.log.check.constant.Message;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.activation.UnsupportedDataTypeException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SasLogCheck {
    private static final String FILE_WITH_ISSUES_NAME = "Result of Log check.xls";

    private final List<Message> dirty = new ArrayList<>();
    private final List<Message> clear = new ArrayList<>();

    private int allIssuesCounter;
    private int fileCounter;
    private int fileWithIssuesCounter;
    private int rowNumber;
    private int columnsAmount;

    private File resultFile;
    private String pathToResultFile;

    private Workbook excelWorkbook;
    private Sheet excelSheet;

    private String nameOption;
    private long dateOption = -1;

    public int getAllIssuesCounter() {
        return allIssuesCounter;
    }

    public File getResultFile() {
        return resultFile;
    }

    public void setNameOption(String nameOption) {
        this.nameOption = nameOption;
    }

    public void setDateOption(long dateOption) {
        this.dateOption = dateOption;
    }

    public void findIssues(File[] files) throws IOException {
        resetResources();
        File file = files[0];

        pathToResultFile = file.isDirectory() ? file.getAbsolutePath() + "\\" : file.getAbsolutePath().substring(0, file.getAbsolutePath()
                                                                                                                        .length() - file
                .getName().length());
        resultFile = new File(pathToResultFile + FILE_WITH_ISSUES_NAME);

        parseTheseFiles(files);
        saveResult();
    }

    private void resetResources() {
        allIssuesCounter = 0;
        fileCounter = 0;
        fileWithIssuesCounter = 0;
        dirty.clear();
        clear.clear();
    }

    private void parseTheseFiles(File[] files) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) {
                for (File innerFile : file.listFiles()) {
                    if (innerFile.isFile()) {
                        parseFile(innerFile);
                    }
                }
            } else {
                parseFile(file);
            }
        }
    }

    private void parseFile(File file) throws IOException {
        String simpleFileName = file.getName();
        String[] fileNameParts = simpleFileName.split("\\.");

        if (!fileNameParts[fileNameParts.length - 1].equalsIgnoreCase("log")) {
            return;
        }

        if (nameOption != null && !"".equals(nameOption) && !simpleFileName.substring(0, nameOption.length())
                                                                           .equalsIgnoreCase(nameOption)) {
            return;
        }

        long timeOfCreationInMillis = file.lastModified();

        if (dateOption != -1 && timeOfCreationInMillis < dateOption) {
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String valueFromLog;
        fileCounter++;

        int currentIssuesCounter = 0;
        while ((valueFromLog = reader.readLine()) != null) {
            for (String string : BadMessages.getBadMessages()) {
                if (valueFromLog.toUpperCase().contains(string)) {
                    dirty.add(new Message(simpleFileName, timeOfCreationInMillis, valueFromLog));
                    allIssuesCounter++;
                    currentIssuesCounter++;
                }
            }
        }

        if (currentIssuesCounter == 0) {
            clear.add(new Message(simpleFileName, timeOfCreationInMillis));
        } else {
            fileWithIssuesCounter++;
        }

        reader.close();
    }

    private String getFormattedDate(long dateInMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN + ", HH:mm:ss", Locale.ENGLISH);
        return simpleDateFormat.format(new Date(dateInMillis));
    }

    private void saveResult() throws IOException {
        if (fileCounter == 0) {
            throw new UnsupportedDataTypeException();
        }

        excelWorkbook = new HSSFWorkbook();
        excelSheet = excelWorkbook.createSheet("Result");

        setMetaInfoAndTitles();

        writeListInFile(clear, "Passed");
        writeListInFile(dirty, "Failed");

        for (int i = 0; i < columnsAmount - 2; i++) {
            excelSheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(resultFile)) {
            excelWorkbook.write(fileOutputStream);
        }

        excelWorkbook.close();
    }

    private void setMetaInfoAndTitles() {
        String[] metaInfo = {"Date of the logs check: " + getFormattedDate(System.currentTimeMillis()),
                "Path: " + pathToResultFile,
                "Checked logs: " + fileCounter,
                "Clear logs: " + (fileCounter - fileWithIssuesCounter),
                "Logs with issues: " + fileWithIssuesCounter};

        rowNumber = metaInfo.length;

        for (int i = 0; i < metaInfo.length; i++) {
            excelSheet.createRow(i).createCell(0).setCellValue(metaInfo[i]);
            excelSheet.addMergedRegion(new CellRangeAddress(i, i, 0, 5));
        }

        Row row = excelSheet.createRow(++rowNumber);
        String[] titles = {"Program name", "Date/Time of run", "Status", "Reason, if failed"};
        columnsAmount = titles.length;

        excelSheet.setAutoFilter(new CellRangeAddress(rowNumber, rowNumber, 0, columnsAmount - 1));

        Font font = excelWorkbook.createFont();
        font.setBold(true);
        CellStyle cellStyle = excelWorkbook.createCellStyle();
        cellStyle.setFont(font);

        Cell cell;
        for (int i = 0; i < columnsAmount; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(titles[i]);

            if (i == columnsAmount - 2) {
                excelSheet.setColumnWidth(i, 2_300);
            }
            if (i == columnsAmount - 1) {
                excelSheet.setColumnWidth(i, 4_600);
            }
        }
    }

    private void writeListInFile(List<Message> messages, String status) {
        DataFormat format = excelWorkbook.createDataFormat();
        CellStyle dateStyle = excelWorkbook.createCellStyle();
        dateStyle.setDataFormat(format.getFormat(DATE_PATTERN + ", HH:mm:ss"));

        for (Message message : messages) {
            Row row = excelSheet.createRow(++rowNumber);

            row.createCell(0).setCellValue(message.getSimpleFileName());
            createDateCell(row, message.getDateOfCreation(), dateStyle);
            row.createCell(2).setCellValue(status);
            row.createCell(3).setCellValue(message.getValueFromLog());
        }
    }

    private void createDateCell(Row row, Date date, CellStyle dateStyle) {
        Cell dateCell = row.createCell(1);
        dateCell.setCellStyle(dateStyle);
        dateCell.setCellValue(date);
    }
}