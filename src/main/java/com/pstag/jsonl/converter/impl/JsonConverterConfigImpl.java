package com.pstag.jsonl.converter.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import com.pstag.jsonl.converter.IConverterConfig;

@Component("JSON-config")
public class JsonConverterConfigImpl implements IConverterConfig {

	public static final String REMOVE_REGEX_KEY = "REMOVE_REGEX_KEY";
	
	public static final String SOURCE_REGEX_KEY = "SOURCE_REGEX_KEY";
	
	public static final String TARGET_REGEX_KEY = "TARGET_REGEX_KEY";

	public static final String CONTINUE_ONE_ERR_KEY = "CONTINUE_ONE_ERR_KEY";

	public static final String IGNORE_BLANK_STR_KEY = "IGNORE_BLANK_STR_KEY";
	
	public static final String OUTPUT_DATE_FORMAT_KEY = "OUTPUT_DATE_FORMAT_KEY";
	
	public static final String SPACE_AFTER_SEP_KEY = "SPACE_AFTER_SEP_KEY";
	
	public static final String TRIM_SPACES_KEY = "TRIM_SPACES_KEY";
	

	@Override
	public Map<String, String> getConfig(ApplicationArguments args) {

		HashMap<String, String> map = new HashMap<String, String>();

		if (args.containsOption("removeRegex"))
			map.put(REMOVE_REGEX_KEY, args.getOptionValues("removeRegex").get(0));
		
		if (args.containsOption("soureRegexReplace"))
			map.put(SOURCE_REGEX_KEY, args.getOptionValues("soureRegexReplace").get(0));
		
		if (args.containsOption("targetRegexReplace"))
			map.put(TARGET_REGEX_KEY, args.getOptionValues("targetRegexReplace").get(0));

		if (args.containsOption("outputDateFormat"))
			map.put(OUTPUT_DATE_FORMAT_KEY, args.getOptionValues("outputDateFormat").get(0));
		
		if (args.containsOption("continueOnError"))
			map.put(CONTINUE_ONE_ERR_KEY, String.valueOf(true));

		if (args.containsOption("ignoreBlankString"))
			map.put(IGNORE_BLANK_STR_KEY, String.valueOf(true));

		if (args.containsOption("trimSpaces"))
			map.put(TRIM_SPACES_KEY, String.valueOf(true));
		
		if (args.containsOption("spaceAfterSep"))
			map.put(SPACE_AFTER_SEP_KEY, String.valueOf(true));

		
		return map;
	}
}
