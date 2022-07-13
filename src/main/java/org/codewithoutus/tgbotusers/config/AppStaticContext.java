package org.codewithoutus.tgbotusers.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppStaticContext {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String CALLBACK_QUERY_DATA_ID_FIELD = "dataId";
}