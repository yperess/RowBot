package org.uvdev.backend.responses;

public class BaseResponse {

    public static final int STATUS_OK = 0;
    public static final int STATUS_INTERNAL_ERROR = 1;

    private int status;

    public BaseResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
