package com.webperformance.muse.okhttp;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;

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
@MuseSubsourceDescriptor(displayName = "URL", description = "URL to get", type = SubsourceDescriptor.Type.Named, name = HttpStep.URL_PARAM)
@MuseSubsourceDescriptor(displayName = "Headers", description = "Headers to send", type = SubsourceDescriptor.Type.Named, name = HttpStep.HEADERS_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Result name", description = "Name of the variable to store the result in. Default is 'result'.", type = SubsourceDescriptor.Type.Named, name = HttpStep.RESULT_NAME_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Client", description = "The HTTP Client to use. Default is '#\"_http_client\"'.", type = SubsourceDescriptor.Type.Named, name = HttpStep.CLIENT_PARAM, optional = true)
public class GetStep extends HttpStep
    {
    @SuppressWarnings("WeakerAccess")  // discovered by reflection, part of public API
    public GetStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        super(configuration, project);
        }

    @SuppressWarnings("unused")
    public final static String TYPE_ID = GetStep.class.getAnnotation(MuseTypeId.class).value();
    }