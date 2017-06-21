package com.madhouse.platform.smartexchange.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by sunshao on 16-1-22.
 */
public class HttpRequestParams {
    private static ResourceBundle bundle = ResourceBundle.getBundle("http");

    public static String PREMIUMMAD_COST_REPORT_URL = bundle.getString("premiummad_cost_report_url");
    public static String ACCESS_TOKEN_URL = bundle.getString("access_token_url");
    public static String ACCESS_TOKEN_FORM_PARAMS = bundle.getString("access_token_form_params");
    public static Map<String, String> ACCESS_TOKEN_FORM_LIST = new HashMap<>();

    static {
        String[] split = HttpRequestParams.ACCESS_TOKEN_FORM_PARAMS.split(";");
        for (String param : split) {
            String[] kv = param.split(":");
            if (2 == kv.length) {
                HttpRequestParams.ACCESS_TOKEN_FORM_LIST.put(kv[0], kv[1]);
            }
        }


    }
}
