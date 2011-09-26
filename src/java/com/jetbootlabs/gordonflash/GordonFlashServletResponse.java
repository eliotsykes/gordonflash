package com.jetbootlabs.gordonflash;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * This provides a pre servlet spec 3.0 way of getting to the status code
 */
public class GordonFlashServletResponse extends HttpServletResponseWrapper {

    private int statusCode = SC_OK;

    public GordonFlashServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(int statusCode) throws IOException {
        this.statusCode = statusCode;
        super.sendError(statusCode);
    }

    @Override
    public void sendError(int statusCode, String msg) throws IOException {
        this.statusCode = statusCode;
        super.sendError(statusCode, msg);
    }

    @Override
    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
        super.setStatus(statusCode);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        this.statusCode = SC_MOVED_TEMPORARILY;
        super.sendRedirect(location);
    }
    
    public int getStatus() {
        return statusCode;
    }

    public boolean isErrorResponse() {
        // Error status codes are 4xx (client error) and 5xx (server error)
        return statusCode >= 400 && statusCode <= 599;
    }
}
