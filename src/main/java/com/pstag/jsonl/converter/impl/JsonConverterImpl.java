package com.pstag.jsonl.converter.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pstag.jsonl.converter.IConverter;

@Component("JSON")
public class JsonConverterImpl implements IConverter {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convert(String[] headers, List<?> values) {

		ObjectNode jsonNode = mapper.createObjectNode();

		for (int i = 0; i < headers.length; i++) {
			Object value = values.get(i);

			if (value instanceof TemporalAccessor) {
				jsonNode.put(headers[i], DATE_FORMAT.format((TemporalAccessor)value));
			} else {
				jsonNode.put(headers[i], value.toString());
			}
		}

		try {
			return mapper.writeValueAsString(jsonNode);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
