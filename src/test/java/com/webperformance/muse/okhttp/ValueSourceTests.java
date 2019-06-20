package com.webperformance.muse.okhttp;

import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ValueSourceTests
    {
    @Test
    void bodyContentAsString() throws MuseExecutionError, IOException
        {
        final String body_text = "this is the response body";
        MockHttpResponse response = new MockHttpResponse(body_text);

        MuseProject project = new SimpleProject();
        BaseExecutionContext context = new ProjectExecutionContext(project);
        Result http_result = new Result(null);
        http_result.response = response;
        context.setVariable(HttpStep.DEFAULT_RESULT_NAME, http_result);

        ValueSourceConfiguration source_config = ValueSourceConfiguration.forType(BodyContentStringSource.TYPE_ID);
        MuseValueSource source = new BodyContentStringSource(source_config, project);
        Object value = source.resolveValue(context);
        Assertions.assertEquals(body_text, value.toString());
        }

    @Test
    void htmlDecode() throws MuseInstantiationException, ValueSourceResolutionError
        {
        final String encoded = "&quot;value&quot;";
        final String decoded = "\"value\"";

        ValueSourceConfiguration config = ValueSourceConfiguration.forType(HtmlDecoderSource.TYPE_ID);
        config.setSource(ValueSourceConfiguration.forValue(encoded));
        SimpleProject project = new SimpleProject();
        MuseValueSource source = new HtmlDecoderSource(config, project);
        String value = source.resolveValue(new ProjectExecutionContext(project)).toString();
        Assertions.assertEquals(decoded, value);
        }
    }