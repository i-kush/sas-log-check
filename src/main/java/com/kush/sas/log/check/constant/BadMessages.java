package com.kush.sas.log.check.constant;

import com.kush.sas.log.check.view.Components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BadMessages {

    private static final String OPTIONAL_NOTE = "NOTE: MISSING VALUES WERE GENERATED AS A RESULT OF PERFORMING AN OPERATION ON MISSING VALUES";

    private static final String[] BAD_MESSAGES = new String[]{
            "WARNING:",
            "ERROR:",
            "INFO:",
            "UNINITIALIZED",
            "NOTE: MERGE STATEMENT HAS MORE THAN ONE DATA SET WITH REPEATS OF BY VALUES.",
            "NOTE: NUMERIC VALUES HAVE BEEN CONVERTED TO CHARACTER VALUES AT THE PLACES GIVEN BY",
            "NOTE: CHARACTER VALUES HAVE BEEN CONVERTED TO NUMERIC VALUES AT THE PLACES GIVEN BY",
            "NUMERIC VARIABLES IN THE INPUT DATA SET WILL BE CONVERTED TO CHARACTER IN THE OUTPUT DATA SET",
            "NOTE: DIVISION BY ZERO DETECTED",
            "NOTE: INVALID ARGUMENT TO FUNCTION",
            "NOTE: MATHEMATICAL OPERATIONS COULD NOT BE PERFORMED",
            "NOTE: AT LEAST ONE W.D FORMAT WAS TOO SMALL FOR THE NUMBER TO BE PRINTED",
            "OBSERVATION(S) OUTSIDE THE AXIS RANGE",
            "COULD NOT BE WRITTEN BECAUSE IT HAS THE SAME NAME",
            "NOTE: LOG AXIS CANNOT SUPPORT ZERO OR NEGATIVE VALUES IN THE DATA RANGE",
            "NOTE: EXTRANEOUS INFORMATION ON %END STATEMENT IGNORED"
    };

    public static List<String> getBadMessages() {
        if (Components.NOTE_MISSING_VALUES_MATTERS.isSelected()) {
            List<String> badMessages = new ArrayList<>(Arrays.asList(BAD_MESSAGES));
            badMessages.add(OPTIONAL_NOTE);

            return badMessages;
        }

        return Arrays.asList(BAD_MESSAGES);
    }
}