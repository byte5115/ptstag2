package com.pstag.jsonl.reader.impl;

import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.SEPARATOR_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.ADDITIONAL_DATE_FORMATS_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class CsvReaderTest {

	@Test
	public void testWithDefaultSettings() throws Exception {

		String headers = "header1,header2,header3";
		String values = "value1,value2,value3";

		StringReader stringReader = new StringReader(headers + "\n" + values);

		HashMap<String, String> properties = new HashMap<String, String>();

		CsvReaderImpl csvReader = new CsvReaderImpl(stringReader, properties);

		assertEquals(3, csvReader.getHeader().length);

		List<List<?>> list = csvReader.getStream().collect(Collectors.toList());

		assertEquals(1, list.size());

		List<?> row = list.get(0);

		assertEquals(3, row.size());

		assertEquals("value1", row.get(0));
		assertEquals("value2", row.get(1));
		assertEquals("value3", row.get(2));

	}

	@Test
	public void testWithNumberAndDateWithDefaultSettings() throws Exception {

		String headers = "header1,header2,header3";
		String values = "value1,123,2024-01-01";

		StringReader stringReader = new StringReader(headers + "\n" + values);

		HashMap<String, String> properties = new HashMap<String, String>();

		CsvReaderImpl csvReader = new CsvReaderImpl(stringReader, properties);

		assertEquals(3, csvReader.getHeader().length);

		List<List<?>> list = csvReader.getStream().collect(Collectors.toList());

		assertEquals(1, list.size());

		List<?> row = list.get(0);

		assertEquals(3, row.size());

		assertEquals("value1", row.get(0));
		assertEquals((byte) 123, row.get(1));
		assertEquals(LocalDate.parse("2024-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")), row.get(2));
	}

	@Test
	public void testWithSeparator() throws Exception {

		String headers = "header1|header2|header3";
		String values = "value1|value2|value3";

		StringReader stringReader = new StringReader(headers + "\n" + values);

		HashMap<String, String> properties = new HashMap<String, String>();

		properties.put(SEPARATOR_KEY, "|");

		CsvReaderImpl csvReader = new CsvReaderImpl(stringReader, properties);

		assertEquals(3, csvReader.getHeader().length);

		List<List<?>> list = csvReader.getStream().collect(Collectors.toList());

		assertEquals(1, list.size());

		List<?> row = list.get(0);

		assertEquals(3, row.size());

		assertEquals("value1", row.get(0));
		assertEquals("value2", row.get(1));
		assertEquals("value3", row.get(2));
	}

	@Test
	public void testWithCustomDate() throws Exception {

		String headers = "header1,header2,header3";
		String values = "value1,value2,2024 01 01";

		StringReader stringReader = new StringReader(headers + "\n" + values);

		HashMap<String, String> properties = new HashMap<String, String>();

		properties.put(ADDITIONAL_DATE_FORMATS_KEY, "yyyy MM dd");

		CsvReaderImpl csvReader = new CsvReaderImpl(stringReader, properties);

		assertEquals(3, csvReader.getHeader().length);

		List<List<?>> list = csvReader.getStream().collect(Collectors.toList());

		assertEquals(1, list.size());

		List<?> row = list.get(0);

		assertEquals(3, row.size());

		assertEquals("value1", row.get(0));
		assertEquals("value2", row.get(1));
		assertEquals(LocalDate.parse("2024-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")), row.get(2));
	}

}
