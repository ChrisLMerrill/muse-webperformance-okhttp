package com.webperformance.muse.okhttp;

import okhttp3.*;
import okio.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("okhttp-client-factory")
@SuppressWarnings("unused")  // discovered and instantiated by reflection (see class ResourceTypes)
public class OkHttpClientFactory extends BaseMuseResource
    {
    @Override
    public ResourceType getType()
        {
        return new OkHttpClientResourceType();
        }

    OkHttpClient createClient(MuseExecutionContext context)
        {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .followRedirects(false)
            .followSslRedirects(false);
        if (_auto_cookies)
            builder = builder.cookieJar(new JavaNetCookieJar(new CookieManager()));

        if (_log_headers)
            {
            builder = builder.addNetworkInterceptor(createLogger());
            _context = context;
            }
        final OkHttpClient client = builder.build();
        context.registerShuttable(() ->
            {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            try
                {
                Objects.requireNonNull(client.cache()).close();
                }
            catch (IOException e)
                {
                // no-op
                }

            });
        return client;
        }

    private Interceptor createLogger()
        {
        return chain ->
            {
            Request request = chain.request();
            String message = String.format("Sending request:\n%s %s\n%s", request.method(), request.url().encodedPath(), request.headers());
            if (_log_content && request.body() != null && request.body().contentLength() > 0)
                {
                Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                //noinspection ConstantConditions
                copy.body().writeTo(buffer);
                String body = buffer.readUtf8();
                message += "\n" + body;
                }
            _context.raiseEvent(MessageEventType.create(message));

            Response response = chain.proceed(request);
            message = String.format("Received response:\n%s %s (%s)\n%s", response.code(), response.message(), response.protocol(), response.headers());

            if (_log_content)
                {
                String response_content = null;
                if (response.body() != null)
                    response_content = response.body().string();

                if (response_content != null && response_content.length() > 0)
                    {
                    if (response_content.length() > 1000)
                        response_content = response_content.substring(0, 1000) + "...";
                    message += "\n" + response_content;
                    }
                }
            _context.raiseEvent(MessageEventType.create(message));

            return response;
            };
        }

    
    public boolean isLogHeaders()
        {
        return _log_headers;
        }

    public void setLogHeaders(boolean log_headers)
        {
        _log_headers = log_headers;
        }

    public boolean isLogContent()
        {
        return _log_content;
        }

    public void setLogContent(boolean log_content)
        {
        _log_content = log_content;
        }

    public boolean isAutoCookies()
        {
        return _auto_cookies;
        }

    public void setAutoCookies(boolean auto_cookies)
        {
        _auto_cookies = auto_cookies;
        }

    private boolean _log_headers = false;
    private boolean _log_content = false;
    private boolean _auto_cookies = false;
    private MuseExecutionContext _context;

    @SuppressWarnings("WeakerAccess")  // public API
    public final static String TYPE_ID = OkHttpClientFactory.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class OkHttpClientResourceType extends ResourceType
        {
        public OkHttpClientResourceType()
            {
            super(TYPE_ID, "HTTP Client configuration", OkHttpClientFactory.class);
            }
        }

    @SuppressWarnings("WeakerAccess")  // public API
    public static OkHttpClient get(MuseExecutionContext context) throws MuseExecutionError
        {
        final Object client = context.getVariable(DEFAULT_CLIENT_NAME);
        if (client == null)
            return new OkHttpClientFactory().createClient(context);
        if (!(client instanceof OkHttpClient))
            throw new MuseExecutionError("Something has been stored in the variable reserved for the OkHttpClient. It is a " + client.getClass().getSimpleName());
        return (OkHttpClient) client;
        }

    @SuppressWarnings("WeakerAccess")  // public API
    public final static String DEFAULT_CLIENT_NAME = "_okhttp_client";

    private final static Logger LOG = LoggerFactory.getLogger(OkHttpClientFactory.class);
    }