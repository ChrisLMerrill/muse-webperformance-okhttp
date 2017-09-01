package com.webperformance.muse.okhttp;

import okhttp3.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CookieStore implements CookieJar
    {
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
        {
        for (Cookie cookie : cookies)
            _cookies.put(cookie.name(), cookie);
        }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url)
        {
        return new ArrayList<>(_cookies.values());
        }

    public Cookie getCookie(String name)
        {
        return _cookies.get(name);
        }

    Map<String, Cookie> _cookies = new HashMap<>();
    }


