package com.webperformance.muse.okhttp;

import okhttp3.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.slf4j.*;

import java.io.*;
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
            builder = builder.cookieJar(new CookieStore());

        if (_log_transactions)
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
            _context.raiseEvent(MessageEventType.create(message));

            Response response = chain.proceed(request);
            message = String.format("Received response:\n%s %s\n%s", response.code(), response.message(), response.headers());
            _context.raiseEvent(MessageEventType.create(message));

            return response;
            };
        }

    public boolean isLogTransactions()
        {
        return _log_transactions;
        }

    public void setLogTransactions(boolean log_transactions)
        {
        _log_transactions = log_transactions;
        }

    public boolean isAutoCookies()
        {
        return _auto_cookies;
        }

    public void setAutoCookies(boolean auto_cookies)
        {
        _auto_cookies = auto_cookies;
        }

    private boolean _log_transactions = false;
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