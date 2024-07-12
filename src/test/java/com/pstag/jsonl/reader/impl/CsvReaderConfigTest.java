package com.pstag.jsonl.reader.impl;

import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.SEPARATOR_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.ADDITIONAL_DATE_FORMATS_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.ESCAPE_CHAR_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.IGNORE_LEADING_WHITESPACE_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.IGNORE_QUOTATIONS_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.QUOTE_CHAR_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

public class CsvReaderConfigTest {

	@Test
	public void testConfig() {

		CsvReaderConfigImpl configImpl = new CsvReaderConfigImpl();

		String sep = "--sep=,";
		String quote = "--quote='";
		String esc = "--esc=\\";
		String ignoreQuote = "--ignoreQuote";
		String ignoreLeading = "--ignoreLeading";
		String addFormats = "--addFormats=yyyy-dd-mm";

		DefaultApplicationArguments applicationArguments = new DefaultApplicationArguments(sep, quote, esc, ignoreQuote,
				ignoreLeading, addFormats);

		Map<String, String> map = configImpl.getConfig(applicationArguments);

		assertEquals(6, map.size());

		assertEquals(",", map.get(SEPARATOR_KEY));
		assertEquals("'", map.get(QUOTE_CHAR_KEY));
		assertEquals("\\", map.get(ESCAPE_CHAR_KEY));
		assertEquals("yyyy-dd-mm", map.get(ADDITIONAL_DATE_FORMATS_KEY));
		assertEquals("true", map.get(IGNORE_QUOTATIONS_KEY));
		assertEquals("true", map.get(IGNORE_LEADING_WHITESPACE_KEY));
	}

}
