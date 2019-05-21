package com.webperformance.muse.okhttp;

import okhttp3.*;

import java.io.*;

/**
 * Wraps the OkHttp response object to cache the response body (by default, you can only access
 * the response body once from the OkHttp API).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class HttpResponse
    {
    public HttpResponse(Response response) throws IOException
        {
        _response = response;
        if (_response.body() != null)
            _body_bytes = _response.body().bytes();
        else
            _body_bytes = null;
        }

    public String bodyText()
        {
        if (_body_string == null && _body_bytes != null)
            _body_string = new String(_body_bytes);
        return _body_string;
        }

    public Headers headers()
        {
        return _response.headers();
        }

    public String header(String name)
        {
        return _response.header(name);
        }

    public int status()
        {
        return _response.code();
        }

    public String message()
        {
        return _response.message();
        }

    private final Response _response;
    private final byte[] _body_bytes;
    private String _body_string;
    }