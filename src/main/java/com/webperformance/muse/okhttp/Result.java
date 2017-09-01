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

    @Override
    public String toString()
        {
        if (success)
            return response.code() + " " + response.message();
        else
            return failure_message;
        }
    }


