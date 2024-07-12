package com.pstag.jsonl.converter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.CONTINUE_ONE_ERR_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.IGNORE_BLANK_STR_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.OUTPUT_DATE_FORMAT_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.SPACE_AFTER_SEP_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.TRIM_SPACES_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.REMOVE_REGEX_KEY;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class JsonConverterTest {

	@Test
	public void testJsonConverterWithDefaultSettings() {
		HashMap<String, String> properties = new HashMap<String, String>();

		JsonConverterImpl jsonConverterImpl = new JsonConverterImpl(properties);

		String[] header = new String[] { "header" };

		String jsonString = jsonConverterImpl.convert(header, Arrays.asList("value"));

		assertEquals("{\"header\":\"value\"}", jsonString);

		jsonString = jsonConverterImpl.convert(header, Arrays.asList(100));

		assertEquals("{\"header\":100}", jsonString);

		jsonString = jsonConverterImpl.convert(header,
				Arrays.asList(LocalDate.parse("2024-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

		assertEquals("{\"header\":\"2024-01-01\"}", jsonString);

	}

	@Test
	public void testJsonConverterWithSpaceAfter() {
		HashMap<String, String> properties = new HashMap<String, String>();

		properties.put(SPACE_AFTER_SEP_KEY, "true");

		JsonConverterImpl jsonConverterImpl = new JsonConverterImpl(properties);

		String[] header = new String[] { "header" };

		String jsonString = jsonConverterImpl.convert(header, Arrays.asList("value"));

		assertEquals("{\"header\": \"value\"}", jsonString);
	}

	@Test
	public void testJsonConverterWithTrim() {
		HashMap<String, String> properties = new HashMap<String, String>();

		properties.put(TRIM_SPACES_KEY, "true");

		JsonConverterImpl jsonConverterImpl = new JsonConverterImpl(properties);

		String[] header = new String[] { "header" };

		String jsonString = jsonConverterImpl.convert(header, Arrays.asList("   value   "));

		assertEquals("{\"header\":\"value\"}", jsonString);
	}

	@Test
	public void testJsonConverterWithoutTrim() {
		HashMap<String, String> properties = new HashMap<String, String>();

		properties.put(TRIM_SPACES_KEY, "false");

		JsonConverterImpl jsonConverterImpl = new JsonConverterImpl(properties);

		String[] header = new String[] { "header" };

		String jsonString = jsonConverterImpl.convert(header, Arrays.asList("   value   "));

		assertEquals("{\"header\":\"   value   \"}", jsonString);
	}

	@Test
	public void testJsonConverterWithIgnoreBlankString() {
		HashMap<String, String> properties = new HashMap<String, String>();

		properties.put(IGNORE_BLANK_STR_KEY, "true");

		JsonConverterImpl jsonConverterImpl = new JsonConverterImpl(properties);

		String[] header = new String[] { "header", "2nd header" };

		String jsonString = jsonConverterImpl.convert(header, Arrays.asList("value", ""));

		assertEquals("{\"header\":\"value\"}", jsonString);
	}

	@Test
	public void testJsonConverterWithCustomDate() {
		HashMap<String, String> properties = new HashMap<String, String>();

		properties.put(OUTPUT_DATE_FORMAT_KEY, "yyyy/MM/dd");
		
		JsonConverterImpl jsonConverterImpl = new JsonConverterImpl(properties);

		String[] header = new String[] { "header" };

		String jsonString = jsonConverterImpl.convert(header,
				Arrays.asList(LocalDate.parse("2024-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

		assertEquals("{\"header\":\"2024/01/01\"}", jsonString);

	}

}
