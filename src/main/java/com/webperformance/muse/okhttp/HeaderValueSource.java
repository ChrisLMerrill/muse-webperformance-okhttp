package com.webperformance.muse.okhttp;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("wpi-okhttp-header-value")
@MuseValueSourceName("Header Value")
@MuseValueSourceTypeGroup("OkHttp")
@MuseValueSourceShortDescription("Get the value of the named header")
@MuseValueSourceLongDescription("Resolves the {name} source. Returns the value of the header or null if it does not exist.")
@MuseStringExpressionSupportImplementation(HeaderValueSource.HeaderValueSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Header Name", description = "Name of the cookie to get", type = SubsourceDescriptor.Type.Single)
@MuseSubsourceDescriptor(displayName = "Result Name", description = "Name of the variable that the HTTP transaction result is stored in (if not the default)", name = "result", type = SubsourceDescriptor.Type.Named, optional = true, defaultValue = HttpStep.DEFAULT_RESULT_NAME)
@SuppressWarnings("unused")  // discovered via reflection
public class HeaderValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public HeaderValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _header_name_source = getValueSource(config, true, project);
        _result_name_source = getValueSource(config, "result", false, project);
        }
    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String header_name = getValue(_header_name_source, context, false, String.class);
        String result_name = getValue(_result_name_source, context, true, String.class);
        if (result_name == null)
            result_name = HttpStep.DEFAULT_RESULT_NAME;

        Object result_obj = context.getVariable(result_name);
        if (!(result_obj instanceof Result))
            throw new ValueSourceResolutionError(String.format("Expected %s to be a Result (from an HTTP transaction). Instead, found a %s", result_name, result_obj.getClass().getSimpleName()));

        Result result = (Result) result_obj;

        return result.response.header(header_name);
        }

    @Override
    public String getDescription()
        {
        return "headerValue(" + _header_name_source.getDescription() + ")";
        }

    private MuseValueSource _header_name_source;
    private MuseValueSource _result_name_source;

    public final static String TYPE_ID = HeaderValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("unused")  // instantiated via reflection
    public static class HeaderValueSourceStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "headerValue";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }

        @Override
        protected String getTypeId()
            {
            return HeaderValueSource.TYPE_ID;
            }
        }
    }