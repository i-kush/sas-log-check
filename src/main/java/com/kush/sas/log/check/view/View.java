package com.kush.sas.log.check.view;

import static com.kush.sas.log.check.view.Components.ADDITIONAL_OPTIONS_BUTTON;
import static com.kush.sas.log.check.view.Components.CURRENT_FILE_LABEL;
import static com.kush.sas.log.check.view.Components.DATE_PICKER_LABEL;
import static com.kush.sas.log.check.view.Components.EMPTY_LABEL_1;
import static com.kush.sas.log.check.view.Components.FIND_ISSUES_BUTTON;
import static com.kush.sas.log.check.view.Components.FRAME;
import static com.kush.sas.log.check.view.Components.OPEN_FILE_WITH_ISSUES_BUTTON;
import static com.kush.sas.log.check.view.Components.PREFIX_FOR_FILE_NAME;
import static com.kush.sas.log.check.view.Components.PREFIX_FOR_FILE_NAME_LABEL;
import static com.kush.sas.log.check.view.Components.SELECT_FILE_OR_DIRECTORY_BUTTON;
import static com.kush.sas.log.check.view.Components.SEPARATOR;

import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class View {
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 250;

    public View() {
        setFrameOptions();
        setViewOptions();
    }

    static GridBagConstraints getGridBagConstraint(int x, int y, int gridWidth) {
        return new GridBagConstraints(x, y, gridWidth, 1, 1, 1, GridBagConstraints.NORTH,
                                      GridBagConstraints.HORIZONTAL, new Insets(4, 2, 4, 2), 0, 0);
    }

    private void setFrameOptions() {
        FRAME.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        FRAME.setLayout(new GridBagLayout());
        FRAME.setLocationRelativeTo(null);
        FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FRAME.setResizable(false);
    }

    private void setViewOptions() {
        FRAME.add(SELECT_FILE_OR_DIRECTORY_BUTTON, getGridBagConstraint(0, 0, 1));

        FRAME.add(FIND_ISSUES_BUTTON, getGridBagConstraint(1, 0, 1));
        FIND_ISSUES_BUTTON.setEnabled(false);

        FRAME.add(OPEN_FILE_WITH_ISSUES_BUTTON, getGridBagConstraint(2, 0, 1));
        OPEN_FILE_WITH_ISSUES_BUTTON.setEnabled(false);

        FRAME.add(CURRENT_FILE_LABEL, getGridBagConstraint(0, 1, 3));
        FRAME.add(SEPARATOR, getGridBagConstraint(0, 2, 3));

        FRAME.add(PREFIX_FOR_FILE_NAME_LABEL, getGridBagConstraint(0, 3, 1));
        FRAME.add(DATE_PICKER_LABEL, getGridBagConstraint(1, 3, 1));

        FRAME.add(EMPTY_LABEL_1, getGridBagConstraint(0, 4, 3));
        FRAME.add(ADDITIONAL_OPTIONS_BUTTON, getGridBagConstraint(2, 5, 1));

        new Actions().setActions();
        setTextOptions();

        FRAME.setVisible(true);
    }

    private void setTextOptions() {
        PREFIX_FOR_FILE_NAME.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() < 10) {
                    super.insertString(offs, str, a);
                }
            }
        });
    }
}