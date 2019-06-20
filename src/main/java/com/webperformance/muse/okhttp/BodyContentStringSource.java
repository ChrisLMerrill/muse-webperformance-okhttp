package com.webperformance.muse.okhttp;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("wpi-okhttp-body-string")
@MuseValueSourceName("Response Body as String")
@MuseValueSourceTypeGroup("OkHttp")
@MuseValueSourceShortDescription("Get the response body as a string")
@MuseValueSourceLongDescription("Converts the response body, if present to a string")
@MuseStringExpressionSupportImplementation(BodyContentStringSource.BodyContentStringSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Result Name", description = "Name of the variable that the HTTP transaction result is stored in (if not the default)", name = "result", type = SubsourceDescriptor.Type.Named, optional = true, defaultValue = HttpStep.DEFAULT_RESULT_NAME)
@SuppressWarnings("unused")  // discovered via reflection
public class BodyContentStringSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public BodyContentStringSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _result_name_source = getValueSource(config, "result", false, project);
        }
    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String result_name = getValue(_result_name_source, context, true, String.class);
        if (result_name == null)
            result_name = HttpStep.DEFAULT_RESULT_NAME;

        Object result_obj = context.getVariable(result_name);
        if (!(result_obj instanceof Result))
            throw new ValueSourceResolutionError(String.format("Expected %s to be a Result (from an HTTP transaction). Instead, found a %s", result_name, result_obj.getClass().getSimpleName()));

        Result result = (Result) result_obj;

        String text = result.response.bodyText();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), text.substring(0, Math.min(100, text.length()))));
        return text;
        }

    @Override
    public String getDescription()
        {
        return "bodyAsString()";
        }

    private MuseValueSource _result_name_source;

    public final static String TYPE_ID = BodyContentStringSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("unused")  // instantiated via reflection
    public static class BodyContentStringSourceStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "bodyAsString";
            }

        @Override
        protected int getNumberArguments()
            {
            return 0;
            }

        @Override
        protected String getTypeId()
            {
            return BodyContentStringSource.TYPE_ID;
            }
        }
    }