package se.agvard.switcheroo;

public class RequestResult {

    // TODO Extract to resource file
    public static final String MALFORMED_URL = "Url or port number is malformed";

    // TODO Extract to resource file
    public static final String IO_OPEN = "Failed to open connection";

    /* Error text */
    private String mErrorText;

    /**
     * Create a successful request result
     */
    public RequestResult() {
        mErrorText = null;
    }

    /**
     * Create a failed request result
     */
    public RequestResult(String errorText) {
        if (errorText == null) {
            throw new NullPointerException();
        }

        mErrorText = errorText;
    }

    /**
     * Check if request was successful
     */
    public boolean success() {
        return mErrorText == null;
    }

    /**
     * Get text describing the error that occurred, or null if request was
     * successful.
     */
    public String getErrorText() {
        return mErrorText;
    }
}
