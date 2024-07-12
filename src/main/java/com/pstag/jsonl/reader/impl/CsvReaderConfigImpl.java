package com.pstag.jsonl.reader.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import com.pstag.jsonl.reader.IReaderConfig;

@Component("CSV-config")
public class CsvReaderConfigImpl implements IReaderConfig {

	public static final String SEPARATOR_KEY = "SEPARATOR_KEY";

	public static final String QUOTE_CHAR_KEY = "QUOTE_CHAR_KEY";

	public static final String ESCAPE_CHAR_KEY = "ESCAPE_CHAR_KEY";

	public static final String IGNORE_QUOTATIONS_KEY = "IGNORE_QUOTATIONS_KEY";

	public static final String IGNORE_LEADING_WHITESPACE_KEY = "IGNORE_LEADING_WHITESPACE_KEY";

	public static final String ADDITIONAL_DATE_FORMATS_KEY = "ADDITIONAL_DATE_FORMATS_KEY";

	@Override
	public Map<String, String> getConfig(ApplicationArguments args) {

		HashMap<String, String> map = new HashMap<String, String>();

		if (args.containsOption("sep"))
			map.put(SEPARATOR_KEY, args.getOptionValues("sep").get(0));

		if (args.containsOption("quote"))
			map.put(QUOTE_CHAR_KEY, args.getOptionValues("quote").get(0));

		if (args.containsOption("esc"))
			map.put(ESCAPE_CHAR_KEY, args.getOptionValues("esc").get(0));
		
		if (args.containsOption("ignoreQuote"))
			map.put(IGNORE_QUOTATIONS_KEY, String.valueOf(true));

		if (args.containsOption("ignoreLeading"))
			map.put(IGNORE_LEADING_WHITESPACE_KEY, String.valueOf(true));

		if (args.containsOption("addFormats"))
			map.put(ADDITIONAL_DATE_FORMATS_KEY, args.getOptionValues("addFormats").get(0));

		
		return map;
	}

}
