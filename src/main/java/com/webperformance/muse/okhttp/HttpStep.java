package com.webperformance.muse.okhttp;

import kotlin.*;
import okhttp3.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class HttpStep extends BaseStep
    {
    @SuppressWarnings("WeakerAccess")  // discovered by reflection, part of public API
    public HttpStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        super(configuration);
        _url_source = getValueSource(configuration, URL_PARAM, true, project);
        _result_name_source = getValueSource(configuration, RESULT_NAME_PARAM, false, project);
        _client_source = getValueSource(configuration, CLIENT_PARAM, false, project);
        _headers_source = getValueSource(configuration, HEADERS_PARAM, false, project);
        }

    @Override
    protected StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        String url = getValue(_url_source, context, false, String.class);
        String result_name;
        OkHttpClient client;

        if (_result_name_source == null)
            result_name = DEFAULT_RESULT_NAME;
        else
            result_name = getValue(_result_name_source, context, false, String.class);

        if (_client_source == null)
            client = OkHttpClientFactory.get(context);
        else
            client = getValue(_client_source, context, false, OkHttpClient.class);

        Request.Builder builder = createBuilder(url);
        builder = addHeaders(context, builder);
        builder = addBody(context, builder);

        Request request = builder.build();
        Result result = new Result(request);
        Response response = null;
        try
            {
            result.success = true;
            response = client.newCall(request).execute();
            result.response = new HttpResponse(response); // This reads the response body! After that, the body can't be accessed again.
            context.setVariable(result_name, result);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("HTTP %s result (%s) stored in #%s", request.method(), result.response.status() + " " + result.response.message(), result_name));
            }
        catch (IllegalStateException | IOException e)
            {
            result.success = false;
            result.failure_message = e.getMessage();
            context.setVariable(result_name, result);
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, String.format("Unable to execute %s due to: %s", request.method(), result.failure_message));
            }
        finally
            {
            if (response != null)
                response.close();
            }
        }

    @SuppressWarnings("WeakerAccess") // available for subclasses
    protected Request.Builder addHeaders(StepExecutionContext context, Request.Builder builder) throws org.musetest.core.values.ValueSourceResolutionError
        {
        if (_headers_source != null)
            {
            List<Pair<Object, Object>> headers = getValue(_headers_source, context, true, List.class);
            if (headers != null)
                for (Pair<Object, Object> header : headers)
                    builder = builder.addHeader(header.getFirst().toString(), header.getSecond().toString());
            }
        return builder;
        }

    @SuppressWarnings({"WeakerAccess", "RedundantThrows", "unused"}) // available for subclasses
    protected Request.Builder addBody(StepExecutionContext context, Request.Builder builder) throws ValueSourceResolutionError
        {
        return builder;  // no-op for methods without a body
        }

    @SuppressWarnings("WeakerAccess") // available for subclasses
    protected Request.Builder createBuilder(String url)
        {
        return new Request.Builder().url(url);
        }

    private final MuseValueSource _url_source;
    private final MuseValueSource _client_source;
    private final MuseValueSource _result_name_source;
    private final MuseValueSource _headers_source;

    @SuppressWarnings({"unused","WeakerAccess"})
    public final static String URL_PARAM = "url";
    public final static String HEADERS_PARAM = "headers";
    final static String RESULT_NAME_PARAM = "response";
    final static String CLIENT_PARAM = "client";
    final static String DEFAULT_RESULT_NAME = "result";
    }