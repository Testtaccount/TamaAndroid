package com.tama.chat.rest.util;

import com.tama.chat.rest.EncodeUtil;
import com.tama.chat.rest.Logger;

public class PostEntityUtil {

    private static final String LOG_TAG = PostEntityUtil.class.getSimpleName();

    public static String composeFindARetailersPostEntity(String zipCode) {
        String entityString = "{\"qry\": \"" + EncodeUtil.escapeString(zipCode) + "\"}";
        Logger.i(LOG_TAG, entityString);
        return entityString;
    }

}
