package com.webperformance.muse.okhttp;

import kotlin.*;
import okhttp3.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("wpi-okhttp-postform")
@MuseStepName("POST fields")
@MuseInlineEditString("Post fields to {url}")
@MuseStepIcon("glyph:Icons525:GLOBE_2")
@MuseStepTypeGroup("OkHttp")
@MuseStepShortDescription("POST a list of form fields to the URL")
@MuseStepLongDescription("The 'url' source is resolved to a string and used to post an update to a resource via HTTP")
@MuseSubsourceDescriptor(displayName = "URL", description = "URL to post to", type = SubsourceDescriptor.Type.Named, name = HttpStep.URL_PARAM)
@MuseSubsourceDescriptor(displayName = "Headers", description = "Headers to send", type = SubsourceDescriptor.Type.List, name = HttpStep.HEADERS_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Result name", description = "Name of the variable to store the result in. Default is 'result'.", type = SubsourceDescriptor.Type.Named, name = HttpStep.RESULT_NAME_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Client", description = "The HTTP Client to use. Default is '#\"_http_client\"'.", type = SubsourceDescriptor.Type.Named, name = HttpStep.CLIENT_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Body", description = "The list of fields to send as the body of the post", type = SubsourceDescriptor.Type.Named, name = PostFormStep.BODY_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Encode parameters", description = "If true, apply URL encoding to each field name and value.", type = SubsourceDescriptor.Type.Named, name = PostFormStep.ENCODE_PARAM, optional = true, defaultValue = "true")
@SuppressWarnings("unused")
public class PostFormStep extends HttpStep
    {
    @SuppressWarnings("unused,WeakerAccess")  // discovered by reflection, part of public API
    public PostFormStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        super(configuration, project);
        _body_source = getValueSource(configuration, BODY_PARAM, false, project);
        _encode_source = getValueSource(configuration, ENCODE_PARAM, false, project);
        }

    @Override
    protected Request.Builder addBody(StepExecutionContext context, Request.Builder builder) throws ValueSourceResolutionError
        {
        FormBody.Builder body_builder = new FormBody.Builder();
        Boolean encode = false;
        if (_encode_source != null)
            {
            Boolean value = getValue(_encode_source, context, true, Boolean.class);
            if (value != null)
                encode = value;
            }

        if (_body_source != null)
            {
            List<Pair> fields = getValue(_body_source, context, true, List.class);
            if (fields != null)
                {
                for (Pair field : fields)
                    {
                    if (encode)
                        body_builder = body_builder.add(field.getFirst().toString(), field.getSecond().toString());
                    else
                        body_builder = body_builder.addEncoded(field.getFirst().toString(), field.getSecond().toString());
                    }
                }
            }

        RequestBody body = body_builder.build();
        return builder.post(body);
        }

    private final MuseValueSource _body_source;
    private final MuseValueSource _encode_source;

    public final static String BODY_PARAM = "body";
    public final static String ENCODE_PARAM = "encode";
    @SuppressWarnings("unused")
    public final static String TYPE_ID = PostFormStep.class.getAnnotation(MuseTypeId.class).value();
    }