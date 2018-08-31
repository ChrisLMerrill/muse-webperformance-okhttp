package com.webperformance.muse.okhttp;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("wpi-okhttp-create")
@MuseStepName("Create HTTP client")
@MuseInlineEditString("Create client using {factory}")
@MuseStepIcon("glyph:Icons525:GLOBE_2")
@MuseStepTypeGroup("OkHttp")
@MuseStepShortDescription("Create a client")
@MuseStepLongDescription("Create an HTTP Client, using the 'factory' parameter to determine the client properties.")
@MuseSubsourceDescriptor(displayName = "Factory", description = "Factory to be used in creatng the client. Must resolve to an HTTP Client Factory", type = SubsourceDescriptor.Type.Named, name = CreateClientStep.FACTORY_PARAM)
@MuseSubsourceDescriptor(displayName = "Client name", description = "Name of the variable to store the client in. Default is '_http_client'.", type = SubsourceDescriptor.Type.Named, name = CreateClientStep.CLIENT_NAME_PARAM, optional = true)
@SuppressWarnings("unused")  // discovered via reflection
public class CreateClientStep extends BaseStep
    {
    public CreateClientStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        super(configuration);
        _factory_source = getValueSource(configuration, FACTORY_PARAM, true, project);
        _name_source = getValueSource(configuration, CLIENT_NAME_PARAM, false, project);
        }

    @Override
    protected StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        OkHttpClientFactory factory = getValue(_factory_source, context, false, OkHttpClientFactory.class);
        String client_var_name;
        if (_name_source == null)
            client_var_name = OkHttpClientFactory.DEFAULT_CLIENT_NAME;
        else
            client_var_name = getValue(_name_source, context, false, String.class);

        context.setVariable(client_var_name, factory.createClient(context));
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, "Created HTTP client into " + client_var_name);
        }

    private final MuseValueSource _factory_source;
    private final MuseValueSource _name_source;

    @SuppressWarnings({"unused", "WeakerAccess"})
    public final static String FACTORY_PARAM = "factory";
    @SuppressWarnings({"unused", "WeakerAccess"})
    public final static String CLIENT_NAME_PARAM = "client";
    @SuppressWarnings({"unused", "WeakerAccess"})
    public final static String TYPE_ID = CreateClientStep.class.getAnnotation(MuseTypeId.class).value();
    }


