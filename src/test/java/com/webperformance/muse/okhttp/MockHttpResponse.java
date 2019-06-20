package com.webperformance.muse.okhttp;

import okhttp3.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockHttpResponse extends HttpResponse
    {
    public MockHttpResponse(String body) throws IOException
        {
        super(new Response.Builder().request(new Request.Builder().url("http://mocked.obj/").build()).protocol(Protocol.HTTP_2).code(200).message("OK").build());
        _body = body;
        }

    @Override
    public String bodyText()
        {
        return _body;
        }

    private String _body;
    }