package com.tama.chat.rest.util;

import static com.tama.chat.app.MyContextWrapper.getLanguage;

import android.content.Context;
import com.tama.chat.rest.HttpRequestManager;

public class APIUtil {

    private static final String HOST_PRODUCTION = "http://tamaexpress.com:585/api/";
    private static final String HOST_STAGING = "http://your_domain.stag.com";

    /**
     * @param requestType   - int constant that helps us to distinguish
     *                        requests and compose necessary api url
     * @param id            - item id which can be passed to url to get item by id from server
     * @param param         - any param which can be requided in url
     */

    public static String getURL(int requestType, int id, String param) {
        return composeUrl(requestType, id, param);
    }

    public static String getURL(int requestType, String param) {
        return composeUrl(requestType, 0, param);
    }

    public static String getURL(int requestType, int id) {
        return composeUrl(requestType, id, null);
    }

    public static String getURL(int requestType) {
        return composeUrl(requestType, 0, null);
    }


    private static String composeUrl(int urlType, int id, String param) {
        String apiUrl;

        switch (urlType) {
            case HttpRequestManager.RequestType.LOG_IN:
                apiUrl = getHost() + "/api/auth/login/";
                break;

            case HttpRequestManager.RequestType.LOG_OUT:
                apiUrl = getHost() + "/api/auth/logout/";
                break;

            case HttpRequestManager.RequestType.ITEM:
                apiUrl = getHost() + "/api/items/" + id;
                break;
                case HttpRequestManager.RequestType.HISTORIES:
                apiUrl = getHost() + "history" + param;
                break;

            default:
                apiUrl = "";
                break;
        }
        return apiUrl;
    }

    public static String getHost() {
//        if (BuildConfig.BUILD_TYPE.equals(Constant.BuildType.RELEASE)) {
            return HOST_PRODUCTION;
//        } else {
//            return HOST_STAGING;
//        }
    }

    public static String getLanguages(Context context) {
        return "?lang=" + getLanguage(context);
    }
}