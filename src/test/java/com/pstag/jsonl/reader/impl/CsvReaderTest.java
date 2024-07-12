package com.pstag.jsonl.reader.impl;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.pstag.jsonl.reader.impl.CsvReaderImpl;

public class CsvReaderTest {

	@Test
	public void testConversion() throws Exception {

		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("DSV input 1.txt")) {

			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

			HashMap<String, String> properties = new HashMap<String, String>() ;
			
			CsvReaderImpl csvReader = new CsvReaderImpl(inputStreamReader, properties);			

			Arrays.asList(csvReader.getHeader()).stream().forEach(System.out::println);

			csvReader.getStream().forEach(e -> e.stream().forEach(System.out::println));
		}

	}

}
