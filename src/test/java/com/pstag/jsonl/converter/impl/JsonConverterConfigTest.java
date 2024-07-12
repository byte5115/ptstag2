package com.pstag.jsonl.converter.impl;

import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.CONTINUE_ONE_ERR_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.IGNORE_BLANK_STR_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.OUTPUT_DATE_FORMAT_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.SPACE_AFTER_SEP_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.TRIM_SPACES_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.REMOVE_REGEX_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

public class JsonConverterConfigTest {

	@Test
	public void testConfig() {

		JsonConverterConfigImpl configImpl = new JsonConverterConfigImpl();

		String removeRegex = "--removeRegex=,";
		String trimSpaces = "--trimSpaces";
		String spaceAfterSep = "--spaceAfterSep";
		String continueOnError = "--continueOnError";
		String ignoreBlankString = "--ignoreBlankString";
		String outputDateFormat = "--outputDateFormat=yyyy-dd-mm";

		DefaultApplicationArguments applicationArguments = new DefaultApplicationArguments(removeRegex, trimSpaces,
				spaceAfterSep, continueOnError, ignoreBlankString, outputDateFormat);

		Map<String, String> map = configImpl.getConfig(applicationArguments);

		assertEquals(6, map.size());

		assertEquals(",", map.get(REMOVE_REGEX_KEY));
		assertEquals("yyyy-dd-mm", map.get(OUTPUT_DATE_FORMAT_KEY));
		assertEquals("true", map.get(CONTINUE_ONE_ERR_KEY));
		assertEquals("true", map.get(TRIM_SPACES_KEY));
		assertEquals("true", map.get(SPACE_AFTER_SEP_KEY));
		assertEquals("true", map.get(IGNORE_BLANK_STR_KEY));

	}

}
