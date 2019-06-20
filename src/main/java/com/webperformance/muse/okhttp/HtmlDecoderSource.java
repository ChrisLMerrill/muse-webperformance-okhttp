package com.webperformance.muse.okhttp;

import org.jsoup.parser.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("html-decode")
@MuseValueSourceName("HTML Decode")
@MuseValueSourceTypeGroup("Encoding")
@MuseValueSourceShortDescription("Returns an HTML-decoded version of the sub-source.")
@MuseValueSourceLongDescription("Resolves the sub-source and resolve it to a string. Decode and return the result.")
@MuseStringExpressionSupportImplementation(HtmlDecoderSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Value", description = "subsource to encode (expects a string value)", type = SubsourceDescriptor.Type.Single)
public class HtmlDecoderSource extends BaseValueSource       // TODO this should really be moved to a muse-jsoup extension
    {
    public HtmlDecoderSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        if (config.getSource() != null)
            _subsource = config.getSource().createSource(project);
        else
            throw new MuseInstantiationException("Missing required parameter (subsource)");
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String value = _subsource.resolveValue(context).toString();
        value = Parser.unescapeEntities(value, false);
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), value));
        return value;
        }

    private MuseValueSource _subsource;

    public final static String TYPE_ID = HtmlDecoderSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "htmlDecode";
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return HtmlDecoderSource.TYPE_ID;
            }
        }
    }