package com.webperformance.muse.okhttp;

import okhttp3.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("wpi-okhttp-cookie-value")
@MuseValueSourceName("Cookie Value")
@MuseValueSourceTypeGroup("OkHttp")
@MuseValueSourceShortDescription("Get the named cookie")
@MuseValueSourceLongDescription("Resolves the {name} source. Returns the value of the cookie or null if it does not exist.")
@MuseStringExpressionSupportImplementation(CookieValueSource.CookieValueSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the cookie to get", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused")  // discovered via reflection
public class CookieValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public CookieValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _name_source = getValueSource(config, true, project);
        }
    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String name = getValue(_name_source, context, false, String.class);
        OkHttpClient client;
        try
            {
            client = OkHttpClientFactory.get(context);
            }
        catch (MuseExecutionError e)
            {
            return null;  // no client...so no cookies.
            }

        CookieJar jar = client.cookieJar();
        if (jar instanceof CookieStore)
            return ((CookieStore)jar).getCookie(name).value();
        return null;
        }

    @Override
    public String getDescription()
        {
        return "cookieValue(" + _name_source.getDescription() + ")";
        }

    private MuseValueSource _name_source;

    private final static String TYPE_ID = CookieValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("unused")  // instantiated via reflection
    public static class CookieValueSourceStringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "cookieValue";
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
            return CookieValueSource.TYPE_ID;
            }
        }
    }


