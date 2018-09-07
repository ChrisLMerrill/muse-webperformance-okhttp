package com.webperformance.muse.okhttp;

import okhttp3.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * This was implemented as a temporary measure due to problems with the OkHttp UrlConnection package with
 * classloading priveledges. Probably not needed in the future.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CookieStore implements CookieJar
    {
    @Override
    public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies)
        {
        for (Cookie cookie : cookies)
            _cookies.put(cookie.name(), cookie);
        }

    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl url)
        {
        return new ArrayList<>(_cookies.values());
        }

    @SuppressWarnings("WeakerAccess")
    public Cookie getCookie(String name)
        {
        return _cookies.get(name);
        }

    private Map<String, Cookie> _cookies = new HashMap<>();
    }


