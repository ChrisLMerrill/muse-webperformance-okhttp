package com.webperformance.muse.okhttp;

import okhttp3.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Result
    {
    @SuppressWarnings("WeakerAccess")  // exposed for access from the test context
    public boolean success;
    @SuppressWarnings("WeakerAccess")  // exposed for access from the test context
    public String failure_message;
    @SuppressWarnings("WeakerAccess")  // exposed for access from the test context
    public HttpResponse response;
    @SuppressWarnings("WeakerAccess")  // exposed for access from the test context
    public Request request;

    public Result(Request request)
        {
        this.request = request;
        }

    @Override
    public String toString()
        {
        if (success)
            return response.status() + " " + response.message();
        else
            return failure_message;
        }
    }


