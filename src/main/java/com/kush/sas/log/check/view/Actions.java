package com.kush.sas.log.check.view;

import static com.kush.sas.log.check.view.Components.ADDITIONAL_OPTIONS_BUTTON;
import static com.kush.sas.log.check.view.Components.CLEAR_BUTTON;
import static com.kush.sas.log.check.view.Components.CURRENT_FILE_LABEL;
import static com.kush.sas.log.check.view.Components.DATE_PATTERN;
import static com.kush.sas.log.check.view.Components.DATE_PICKER;
import static com.kush.sas.log.check.view.Components.DATE_PICKER_LABEL;
import static com.kush.sas.log.check.view.Components.FILE_CHOOSER;
import static com.kush.sas.log.check.view.Components.FIND_ISSUES_BUTTON;
import static com.kush.sas.log.check.view.Components.FRAME;
import static com.kush.sas.log.check.view.Components.NOTE_MISSING_VALUES_MATTERS;
import static com.kush.sas.log.check.view.Components.OPEN_FILE_WITH_ISSUES_BUTTON;
import static com.kush.sas.log.check.view.Components.PREFIX_FOR_FILE_NAME;
import static com.kush.sas.log.check.view.Components.PREFIX_FOR_FILE_NAME_LABEL;
import static com.kush.sas.log.check.view.Components.SELECT_FILE_OR_DIRECTORY_BUTTON;

import com.kush.sas.log.check.service.SasLogCheck;

import javax.activation.UnsupportedDataTypeException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Actions {
    private static final String MESSAGE = "There are no files to check." + System.lineSeparator() + "Please, select file or directory with appropriate options.";
    private static boolean isHidden = true;

    private final SasLogCheck logCheck = new SasLogCheck();

    private File[] file;

    void setActions() {
        SELECT_FILE_OR_DIRECTORY_BUTTON.addActionListener(event -> selectFileOrDirectoryButtonAction());
        FIND_ISSUES_BUTTON.addActionListener(event -> findIssuesButtonAction());
        OPEN_FILE_WITH_ISSUES_BUTTON.addActionListener(event -> openFileWithIssuesButton());
        CLEAR_BUTTON.addActionListener(event -> clearButton());
        ADDITIONAL_OPTIONS_BUTTON.addActionListener(event -> additionalOptionsButton());

        DATE_PICKER.addActionListener(event -> datePickerAction());
        PREFIX_FOR_FILE_NAME.addActionListener(event -> prefixForFileNameAction());
    }

    private void findIssuesButtonAction() {
        try {
            logCheck.setNameOption(PREFIX_FOR_FILE_NAME.getText());
            logCheck.setDateOption(getValueFromDatePicker());

            logCheck.findIssues(file);
            int issues = logCheck.getAllIssuesCounter();

            if (issues == 0) {
                JOptionPane.showMessageDialog(FRAME, "Your SAS log is clear", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(FRAME, "There are " + issues + " issues", "Warning", JOptionPane.WARNING_MESSAGE);
            }

            OPEN_FILE_WITH_ISSUES_BUTTON.setEnabled(true);
            openFileWithIssuesButton();
        } catch (UnsupportedDataTypeException e) {
            JOptionPane.showMessageDialog(FRAME, MESSAGE, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(FRAME, e.getMessage(), "Sorry, unknown error", JOptionPane.ERROR_MESSAGE);
        }

        refreshFrame();
    }

    private void selectFileOrDirectoryButtonAction() {
        int resultOfAction = FILE_CHOOSER.showDialog(FRAME, "Choose");

        if (resultOfAction == JFileChooser.APPROVE_OPTION) {
            file = FILE_CHOOSER.getSelectedFiles();
            FIND_ISSUES_BUTTON.setEnabled(true);

            CURRENT_FILE_LABEL.setText(getPath(file[0]));
            OPEN_FILE_WITH_ISSUES_BUTTON.setEnabled(false);
        }

        refreshFrame();
    }

    private String getPath(File file) {
        return file.isDirectory() ? file.getAbsolutePath() + "\\" : file.getAbsolutePath().substring(0, file.getAbsolutePath()
                                                                                                            .length() - file.getName()
                                                                                                                            .length());
    }

    private void openFileWithIssuesButton() {
        try {
            Desktop.getDesktop().open(logCheck.getResultFile());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void clearButton() {
        PREFIX_FOR_FILE_NAME.setText("");
        DATE_PICKER.getJFormattedTextField().setText("");
        OPEN_FILE_WITH_ISSUES_BUTTON.setEnabled(false);
        NOTE_MISSING_VALUES_MATTERS.setSelected(false);
        refreshFrame();
    }

    private void additionalOptionsButton() {
        if (isHidden) {
            ADDITIONAL_OPTIONS_BUTTON.setText("Options <<");
            FRAME.add(PREFIX_FOR_FILE_NAME, View.getGridBagConstraint(0, 4, 1));
            FRAME.add(DATE_PICKER, View.getGridBagConstraint(1, 4, 1));
            FRAME.add(NOTE_MISSING_VALUES_MATTERS, View.getGridBagConstraint(2, 4, 1));
            FRAME.add(CLEAR_BUTTON, View.getGridBagConstraint(0, 5, 1));

            PREFIX_FOR_FILE_NAME_LABEL.setText("File name prefix:");
            DATE_PICKER_LABEL.setText("Starting date:");

            isHidden = false;
        } else {
            ADDITIONAL_OPTIONS_BUTTON.setText("Options >>");
            FRAME.remove(PREFIX_FOR_FILE_NAME);
            FRAME.remove(DATE_PICKER);
            FRAME.remove(NOTE_MISSING_VALUES_MATTERS);
            FRAME.remove(CLEAR_BUTTON);

            PREFIX_FOR_FILE_NAME_LABEL.setText("");
            DATE_PICKER_LABEL.setText("");

            isHidden = true;
        }

        refreshFrame();
    }

    private long getValueFromDatePicker() {
        String valueFromDatePicker = DATE_PICKER.getJFormattedTextField().getText();

        if ("".equals(valueFromDatePicker) || valueFromDatePicker == null) {
            return -1;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        try {
            Date date = simpleDateFormat.parse(DATE_PICKER.getJFormattedTextField().getText());
            return date.getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    private void datePickerAction() {
        OPEN_FILE_WITH_ISSUES_BUTTON.setEnabled(false);
    }

    private void prefixForFileNameAction() {
        OPEN_FILE_WITH_ISSUES_BUTTON.setEnabled(false);
    }

    private void refreshFrame() {
        FRAME.revalidate();
        FRAME.repaint();
    }
}
