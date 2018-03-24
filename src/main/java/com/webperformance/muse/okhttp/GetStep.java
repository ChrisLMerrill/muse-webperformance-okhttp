package com.webperformance.muse.okhttp;

import okhttp3.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("wpi-okhttp-get")
@MuseStepName("GET a URL")
@MuseInlineEditString("Get {url}")
@MuseStepIcon("glyph:Icons525:GLOBE_2")
@MuseStepTypeGroup("OkHttp")
@MuseStepShortDescription("Get the URL")
@MuseStepLongDescription("The 'url' source is resolved to a string and used to retrive a resource via HTTP")
@MuseSubsourceDescriptor(displayName = "URL", description = "URL to get", type = SubsourceDescriptor.Type.Named, name = GetStep.URL_PARAM)
@MuseSubsourceDescriptor(displayName = "Result name", description = "Name of the variable to store the result in. Default is 'result'.", type = SubsourceDescriptor.Type.Named, name = GetStep.RESULT_NAME_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Client", description = "The HTTP Client to use. Default is '#\"_http_client\"'.", type = SubsourceDescriptor.Type.Named, name = GetStep.CLIENT_PARAM, optional = true)
public class GetStep extends BaseStep
    {
    @SuppressWarnings("WeakerAccess")  // discovered by reflection, part of public API
    public GetStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        super(configuration);
        _url_source = getValueSource(configuration, URL_PARAM, true, project);
        _result_name_source = getValueSource(configuration, RESULT_NAME_PARAM, false, project);
        _client_source = getValueSource(configuration, CLIENT_PARAM, false, project);
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

        Object client_object;
        if (_client_source == null)
            client_object = OkHttpClientFactory.get(context);
        else
            client_object = getValue(_client_source, context, false, OkHttpClient.class);
        client = (OkHttpClient) client_object;

        Request request = new Request.Builder().url(url).build();
        Result result = new Result();
        try
            {
            result.success = true;
            result.response = client.newCall(request).execute();
            context.setVariable(result_name, result);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("HTTP GET result (%s) stored in #%s", result.response.code() + " " + result.response.message(), result_name));
            }
        catch (IOException e)
            {
            result.success = false;
            result.failure_message = e.getMessage();
            context.setVariable(result_name, result);
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "Unable to execute GET due to: " + result.failure_message);
            }
        finally
            {
            if (result.response != null)
                result.response.close();
            }
        }

    private final MuseValueSource _url_source;
    private final MuseValueSource _client_source;
    private final MuseValueSource _result_name_source;

    final static String URL_PARAM = "url";
    final static String RESULT_NAME_PARAM = "response";
    final static String CLIENT_PARAM = "client";
    private final static String DEFAULT_RESULT_NAME = "result";
    }


