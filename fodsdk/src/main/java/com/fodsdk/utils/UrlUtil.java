package com.fodsdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class UrlUtil {
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }
}
