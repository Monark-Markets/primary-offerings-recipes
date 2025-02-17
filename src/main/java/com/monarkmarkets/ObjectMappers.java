package com.monarkmarkets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMappers {
	private ObjectMappers() {
	}

	public static ObjectMapper createWithDefaults() {
		ObjectMapper mapper = new ObjectMapper();
		return applyDefaults(mapper);
	}

	public static ObjectMapper applyDefaults(ObjectMapper mapper) {
		mapper.registerModules(getModules());
		mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
		return mapper;
	}

	public static Module[] getModules() {
		return new Module[]{new JavaTimeModule(), new Jdk8Module()};
	}
}