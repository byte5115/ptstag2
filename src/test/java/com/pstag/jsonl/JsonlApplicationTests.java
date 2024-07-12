package com.pstag.jsonl;

import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.IGNORE_BLANK_STR_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.REMOVE_REGEX_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.SPACE_AFTER_SEP_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.TRIM_SPACES_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.SEPARATOR_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;

import com.pstag.jsonl.converter.impl.JsonConverterConfigImpl;
import com.pstag.jsonl.converter.impl.JsonConverterImpl;
import com.pstag.jsonl.reader.impl.CsvReaderConfigImpl;
import com.pstag.jsonl.reader.impl.CsvReaderImpl;

@SpringBootTest
class JsonlApplicationTests {

	@Test
	public void testConversionDsv1() throws Exception {

		InputStream dsvInputStream = getClass().getClassLoader().getResourceAsStream("DSV input 1.txt");

		InputStreamReader dsvInputStreamReader = new InputStreamReader(dsvInputStream);

		CsvReaderConfigImpl csvConfigImpl = new CsvReaderConfigImpl();

		String sep = "--sep=,";

		DefaultApplicationArguments csvArgs = new DefaultApplicationArguments(sep);

		Map<String, String> csvProperties = csvConfigImpl.getConfig(csvArgs);

		CsvReaderImpl csvReader = new CsvReaderImpl(dsvInputStreamReader, csvProperties);

		JsonConverterConfigImpl jsonConfigImpl = new JsonConverterConfigImpl();

		String removeRegex = "--removeRegex=[|]";
		String trimSpaces = "--trimSpaces";
		String spaceAfterSep = "--spaceAfterSep";
		String continueOnError = "--continueOnError";
		String ignoreBlankString = "--ignoreBlankString";

		DefaultApplicationArguments applicationArguments = new DefaultApplicationArguments(removeRegex, trimSpaces,
				spaceAfterSep, continueOnError, ignoreBlankString);

		Map<String, String> jsonProperties = jsonConfigImpl.getConfig(applicationArguments);

		JsonConverterImpl jsonConverterImpl = new JsonConverterImpl(jsonProperties);

		StringWriter stringWriter = new StringWriter();

		String[] headers = csvReader.getHeader();

		Stream<List<?>> stream = csvReader.getStream();

		stream.forEach(dataRow -> {
			stringWriter.append(jsonConverterImpl.convert(headers, dataRow));
			stringWriter.append("\n");
		});

		stringWriter.flush();

		String actualData = stringWriter.toString();

		InputStream jsonlInputStream = getClass().getClassLoader().getResourceAsStream("JSONL output.jsonl");

		String expectedData = readFromInputStream(jsonlInputStream);

		assertEquals(expectedData, actualData);

	}

	@Test
	public void testConversionDsv2() throws Exception {

		InputStream dsvInputStream = getClass().getClassLoader().getResourceAsStream("DSV input 2.txt");

		InputStreamReader dsvInputStreamReader = new InputStreamReader(dsvInputStream);

		CsvReaderConfigImpl csvConfigImpl = new CsvReaderConfigImpl();

		String sep = "--sep=|";

		DefaultApplicationArguments csvArgs = new DefaultApplicationArguments(sep);

		Map<String, String> csvProperties = csvConfigImpl.getConfig(csvArgs);

		CsvReaderImpl csvReader = new CsvReaderImpl(dsvInputStreamReader, csvProperties);

		JsonConverterConfigImpl jsonConfigImpl = new JsonConverterConfigImpl();

		String removeRegex = "--removeRegex=[,]";
		String soureRegexReplace = "--soureRegexReplace=[|]";
		String targetRegexReplace = "--targetRegexReplace=,";
		String trimSpaces = "--trimSpaces";
		String spaceAfterSep = "--spaceAfterSep";
		String continueOnError = "--continueOnError";
		String ignoreBlankString = "--ignoreBlankString";

		DefaultApplicationArguments applicationArguments = new DefaultApplicationArguments(removeRegex, trimSpaces,
				spaceAfterSep, continueOnError, ignoreBlankString, soureRegexReplace, targetRegexReplace);

		Map<String, String> jsonProperties = jsonConfigImpl.getConfig(applicationArguments);

		JsonConverterImpl jsonConverterImpl = new JsonConverterImpl(jsonProperties);

		StringWriter stringWriter = new StringWriter();

		String[] headers = csvReader.getHeader();

		Stream<List<?>> stream = csvReader.getStream();

		stream.forEach(dataRow -> {
			stringWriter.append(jsonConverterImpl.convert(headers, dataRow));
			stringWriter.append("\n");
		});

		stringWriter.flush();

		String actualData = stringWriter.toString();

		InputStream jsonlInputStream = getClass().getClassLoader().getResourceAsStream("JSONL output.jsonl");

		String expectedData = readFromInputStream(jsonlInputStream);

		assertEquals(expectedData, actualData);

	}

	public static String readFromInputStream(InputStream inputStream) throws IOException {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line);
				content.append("\n");
			}
		}
		return content.toString();
	}

}
