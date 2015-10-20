package org.uvdev.rowbot.concept2api.pacemonitor;

import android.util.SparseArray;

public final class ErrorCodes {

    public static final int APMAIN_TASKCREATE_ERR = createErrorCode(1,
            "Operating system task creation error");
    public static final int APMAIN_TASKDELETE_ERR = createErrorCode(2,
            "Operating system teask deletion error");
    public static final int APMAIN_USERKEY_STUCK_ERR = createErrorCode(4,
            "One or more user input keys are asserted during power-up and not released within 2"
                    + " seconds");
    public static final int APMAIN_TASK_INVALID_ERR = createErrorCode(5,
            "One or more operating system tasks that should be active during normal operation are"
                    + " determined to be inactive");
    public static final int APCOMM_INVALIDPW_ERR = createErrorCode(11,
            "Invalid interface authentication password");
    public static final int APLOG_INVALIDUSER_ERR = createErrorCode(21,
            "User number provided by the screen content is out of range");
    public static final int APLOG_USERSTATINFO_STORAGE_ERR = createErrorCode(22,
            "User static information not successfully stored on logcard");
    public static final int APLOG_USERSTATINFO_RETRIEVE_ERR = createErrorCode(23,
            "User static information not successfully retrieved from logcard");
    public static final int APLOG_USERDELETE_ERR = createErrorCode(24,
            "Unsuccessful deletion of user from logcar");
    public static final int APLOG_USERDYNAMINFO_STORAGE_ERR = createErrorCode(25,
            "User dynamic information not successfully stored on logcard");
    public static final int APLOG_USERDYNAMINFO_RETRIEVE_ERR = createErrorCode(26,
            "User dynamic information not successfully retrieved from logcard");
    public static final int APLOG_CUSTOMWORKOUT_STORAGE_ERR = createErrorCode(27,
            "Custom workout information not successfully stored on logcard");
    public static final int APLOG_CUSTOMWORKOUT_RETRIEVE_ERR = createErrorCode(28,
            "Custom workout information not successfully retrieved from logcard");
    public static final int APLOG_CUSTOMWORKOUT_INSUFFMEM_ERR = createErrorCode(29,
            "Insufficient logcard memory exists to store the custom workout information");
    public static final int APLOG_CUSTOMWORKOUT_INVALID_ERR = createErrorCode(30,
            "Specific custom workout information is invalid");
    public static final int APLOG_INVALIDCARDOPERATION_ERR = createErrorCode(31,
            "Screen content performed invalid logcard operation");
    public static final int APLOG_INVALIDCUSTOMWORKOUT_ERR = createErrorCode(33,
            "Custom workout number provided by the screen content is out of range");
    public static final int APLOG_INVALIDWORKOUTIDENT_ERR = createErrorCode(34,
            "Workout type provided by the screen content is out of range");
    public static final int APLOG_INVALIDINPUTPARAM_ERR = createErrorCode(36,
            "Special function input parameter provided by screen content is invalid");
    public static final int APLOG_INVALIDWORKOUTNUM_ERR = createErrorCode(37,
            "Workout number provided by the screen content is out of range");
    public static final int APLOG_CARDNOTPRESENT_ERR = createErrorCode(38,
            "Logcard access unsuccessful because card not present");
    public static final int APLOG_INVALIDINTLOGADDR_ERR = createErrorCode(39,
            "Logcard workout log address provided by the screen content was out of range");
    public static final int APLOG_INVALIDLOGHDRPTR_ERR = createErrorCode(40,
            "Accessing the logcard workout log section was unsuccessful because some of the"
                    + " contents are invalid");
    public static final int APLOG_MAXSPLITSEXCEEDED_ERR = createErrorCode(41,
            "Unable to store the split/interval data in internal log memory because the maximum #"
                    + " of splits has been exceeded");
    public static final int APLOG_NODATAAVAILABLE_ERR = createErrorCode(42,
            "Searching for the requested logcard workout log data has returned no information");
    public static final int APLOG_INVALIDCARDSTRUCTREV_ERR = createErrorCode(43,
            "Logcard card information struction revision is invalid");
    public static final int APLOG_CARDOPERATIONTIMEOUT_ERR = createErrorCode(44,
            "Logcard operations requested by the screen content timed-out waiting for the logcard"
                    + " to become available");
    public static final int APLOG_INVALIDLOGSIZE_ERR = createErrorCode(45,
            "Detected invalid data set size while storing workout results to logcard");
    public static final int APLOG_LOGENTRYVALIDATE_ERR = createErrorCode(46,
            "Failure to validate workout results written to logcard");
    public static final int APLOG_USERDYNAMICVALIDATE_ERR = createErrorCode(47,
            "Failure to validate updated user dynamic data written to logcar");
    public static final int APLOG_CARDINFOVALIDATE_ERR = createErrorCode(48,
            "Failure to validate updated card information data written to logcard");
    public static final int APLOG_CARDACCESS_ERR = createErrorCode(49,
            "Unable to communicate with logcard while its status is present and valid");
    public static final int APPM3_INVALIDWORKOUTNUM_ERR = createErrorCode(60,
            "Workout number provided by screen content or host PC for configuring the PM3 is out of"
                    + " range");
    public static final int APPM3_NOPLOTDATA_ERR = createErrorCode(61,
            "No pace plot data available for collection by the host PC");
    public static final int APPM3_INVALIDMFGINFO_ERR = createErrorCode(62,
            "Manufacturing information structure stored in non-volatile memory does not pass its"
                    + " integrity check");
    public static final int APPM3_INVALIDCALINFO_ERR = createErrorCode(63,
            "Calibration information structure stored in non-volatile memory does not pass its"
                    + " integrity check");
    public static final int APPM3_INVALIDWORKOUTDURATION_ERR = createErrorCode(64,
            "Programmed workout duration is out of range");
    public static final int APPM3_INVALIDSPLITDURATION_ERR = createErrorCode(65,
            "Programmed split duration out of range, causes max splits to be exceeded or exceeds"
                    + " workout duration");
    public static final int APPM3_INVALIDRESTDURATION_ERR = createErrorCode(66,
            "Programmed rest duration out of range");
    public static final int APPM3_INVALIDINTERVALCNT_ERR = createErrorCode(67,
            "Programmed interval count out of range");
    public static final int APPM3_INVALIDWORKOUTTYPE_ERR = createErrorCode(68,
            "Programmed workout type invalid");
    public static final int APHEADER_INVALIDFONTHDR_ERR = createErrorCode(80,
            "Font information header structure stored in Flash memory does not pass its integrity"
                    + " check");
    public static final int APHEADER_INVALIDSCRNHDR_ERR = createErrorCode(81,
            "Screen content information header structure stored in Flash memory does not pass its"
                    + " integrity check");
    public static final int APHEADER_INVALIDLDRHDR_ERR = createErrorCode(82,
            "USB Loader information header structure stored in Flash memory does not pass its"
                    +" integrity check");
    public static final int APHEADER_INVALIDAPPHDR_ERR = createErrorCode(83,
            "Application information header structure stored in Flash memory does not pass its"
                    + " integrity check");
    // TODO Finish adding error codes.

    private static final SparseArray<String> sErrorStrings = new SparseArray<>();
    private static int createErrorCode(int errorCode, String errorString) {
        sErrorStrings.append(errorCode, errorString);
        return errorCode;
    }

    /**
     * Converts an error code to string.
     *
     * @param errorCode The error code to convert.
     * @return The string representation of the error code.
     */
    public static String toString(int errorCode) {
        return sErrorStrings.get(errorCode);
    }
}
