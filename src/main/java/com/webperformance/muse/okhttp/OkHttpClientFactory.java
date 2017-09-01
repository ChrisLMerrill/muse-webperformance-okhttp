package com.webperformance.muse.okhttp;

import okhttp3.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.slf4j.*;

import java.io.*;

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

    public OkHttpClient createClient(MuseExecutionContext context)
        {
        final OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieStore())
            .followRedirects(false)
            .followSslRedirects(false)
            .build();
        context.registerShuttable(() ->
            {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            try
                {
                if (client.cache() != null)
                    client.cache().close();
                }
            catch (IOException e)
                {
                // no-op
                }

            });
        return client;
        }

    public final static String TYPE_ID = OkHttpClientFactory.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class OkHttpClientResourceType extends ResourceType
        {
        public OkHttpClientResourceType()
            {
            super(TYPE_ID, "HTTP Client configuration", OkHttpClientFactory.class);
            }
        }

    public static OkHttpClient get(MuseExecutionContext context) throws MuseExecutionError
        {
        final Object client = context.getVariable(DEFAULT_CLIENT_NAME);
        if (client == null)
            return new OkHttpClientFactory().createClient(context);
        if (!(client instanceof OkHttpClient))
            throw new MuseExecutionError("Something has been stored in the variable reserved for the OkHttpClient. It is a " + client.getClass().getSimpleName());
        return (OkHttpClient) client;
        }

    public final static String DEFAULT_CLIENT_NAME = "_okhttp_client";

    private final static Logger LOG = LoggerFactory.getLogger(OkHttpClientFactory.class);
    }