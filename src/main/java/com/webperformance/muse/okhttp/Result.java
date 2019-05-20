package com.webperformance.muse.okhttp;

import okhttp3.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Result
    {
    public boolean success;
    public String failure_message;
    public Response response;
    public Request request;

    public Result(Request request)
        {
        this.request = request;
        }

    @Override
    public String toString()
        {
        if (success)
            return response.code() + " " + response.message();
        else
            return failure_message;
        }
    }


