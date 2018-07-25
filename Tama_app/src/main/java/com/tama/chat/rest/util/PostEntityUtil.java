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

    public static String composeConfirmOrderPostEntity(String productIds, String sender_name,
        String sender_mobile, String receiver_name, String receiver_mobile, String pay_by,
        String use_promo) {
        String entityString = "{ \"products\":\""+productIds+"\","
            + " \"sender_name\":\""+sender_name+"\","
            + " \"sender_mobile\": \""+sender_mobile+"\","
            + " \"receiver_name\":\""+receiver_name+"\","
            + " \"receiver_mobile\":\""+receiver_mobile+"\","
            + " \"pay_by\": \""+pay_by+"\","
            + " \"use_promo\" :\""+use_promo+"\" }";
        Logger.i(LOG_TAG, entityString);
        return entityString;
    }

    public static String composeGetDenominationsPostEntity(String topup_no, String country_code) {
        String entityString = "{ \"topup_no\":\""+topup_no+"\","
                             + " \"country_code\" :\""+country_code+"\" }";
        Logger.i(LOG_TAG, entityString);
        return entityString;
    }
}
