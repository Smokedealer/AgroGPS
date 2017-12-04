package dev.agrogps.server.utils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;


public class Responses {

    private static Map<String, Object> build(String status, String eKey, Object eValue) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);
        if(eKey != null && eValue != null) {
            map.put(eKey, eValue);
        }
        return map;
    }

    private static Map<String, Object> build(String status) {
        return build(status, null, null);
    }


    public static Response error(int code, String message) {
        return Response
                .status(code)
                .entity(build("error", "description", message))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }



    public static Response success() {
        return Response
                .ok(build("success"), MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    public static Response singleValue(String key, Object value) {
        return Response
                .ok(build("success", key, value), MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

}
