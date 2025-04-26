package com.kush.sas.log.check.view;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Properties;

public class Components {
    public static final String DATE_PATTERN = "dd MMM yyyy";
    public static final JCheckBox NOTE_MISSING_VALUES_MATTERS = new JCheckBox("Check missing");
    static final JButton SELECT_FILE_OR_DIRECTORY_BUTTON = new JButton("Select");
    static final JFrame FRAME = new JFrame("Finding SAS log issues");
    static final JLabel CURRENT_FILE_LABEL = new JLabel();
    static final JLabel PREFIX_FOR_FILE_NAME_LABEL = new JLabel();
    static final JLabel DATE_PICKER_LABEL = new JLabel();
    static final JLabel EMPTY_LABEL_1 = new JLabel();
    static final JSeparator SEPARATOR = new JSeparator();
    static final JFileChooser FILE_CHOOSER = new JFileChooser();
    static final JButton OPEN_FILE_WITH_ISSUES_BUTTON = new JButton("Open result");
    static final JButton ADDITIONAL_OPTIONS_BUTTON = new JButton("Options >>");
    static final JButton CLEAR_BUTTON = new JButton("Clear");
    static final JTextField PREFIX_FOR_FILE_NAME = new JTextField(5);
    static final JDatePickerImpl DATE_PICKER;
    static final JButton FIND_ISSUES_BUTTON = new JButton("Find issues");
    private static final int WIDTH = (int) SELECT_FILE_OR_DIRECTORY_BUTTON.getPreferredSize().getWidth();
    private static final int HEIGHT = (int) SELECT_FILE_OR_DIRECTORY_BUTTON.getPreferredSize().getHeight();
    private static final Font FONT = new Font(SELECT_FILE_OR_DIRECTORY_BUTTON.getFont()
                                                                             .getName(), Font.PLAIN, SELECT_FILE_OR_DIRECTORY_BUTTON
                                                      .getFont().getSize());

    static {
        EMPTY_LABEL_1.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        PREFIX_FOR_FILE_NAME_LABEL.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        DATE_PICKER_LABEL.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    static {
        FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FILE_CHOOSER.setPreferredSize(new Dimension(500, 600));
        FILE_CHOOSER.setMultiSelectionEnabled(true);
        FILE_CHOOSER.setFileFilter(new FileNameExtensionFilter("Log", "log", "LOG"));
    }

    static {
        NOTE_MISSING_VALUES_MATTERS.setPreferredSize(new Dimension(ADDITIONAL_OPTIONS_BUTTON.getWidth(), HEIGHT));
    }

    static {
        PREFIX_FOR_FILE_NAME.setFont(FONT);
        PREFIX_FOR_FILE_NAME.setPreferredSize(new Dimension(WIDTH, HEIGHT + 1));
    }

    static {
        UtilDateModel model = new UtilDateModel();

        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        DATE_PICKER = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JTextField textField = DATE_PICKER.getJFormattedTextField();
        textField.setFont(FONT);
        textField.setHorizontalAlignment(JTextField.LEFT);
    }

    static {
        FIND_ISSUES_BUTTON.setPreferredSize(new Dimension((int) DATE_PICKER.getPreferredSize().getWidth(), HEIGHT));
    }
}
