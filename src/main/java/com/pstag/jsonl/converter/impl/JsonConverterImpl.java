package com.pstag.jsonl.converter.impl;

import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.CONTINUE_ONE_ERR_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.IGNORE_BLANK_STR_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.OUTPUT_DATE_FORMAT_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.REMOVE_REGEX_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.SPACE_AFTER_SEP_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.TRIM_SPACES_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.TARGET_REGEX_KEY;
import static com.pstag.jsonl.converter.impl.JsonConverterConfigImpl.SOURCE_REGEX_KEY;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pstag.jsonl.converter.IConverter;

@Component("JSON")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JsonConverterImpl implements IConverter {

	private final DateTimeFormatter dateFormatter;

	private final ObjectMapper mapper;

	private final String removeRegex;

	private final String sourceReplaceRegex;

	private final String targetReplaceRegex;

	private final boolean continueOnError;

	private final boolean ignoreBlankString;

	private final boolean trimString;

	private final boolean spaceAfterSep;

	public JsonConverterImpl(Map<String, String> properties) {

		this.removeRegex = properties.getOrDefault(REMOVE_REGEX_KEY, "");

		this.sourceReplaceRegex = properties.getOrDefault(SOURCE_REGEX_KEY, "");

		this.targetReplaceRegex = properties.getOrDefault(TARGET_REGEX_KEY, "");

		this.trimString = Boolean.parseBoolean(properties.getOrDefault(TRIM_SPACES_KEY, "false"));

		this.continueOnError = Boolean.parseBoolean(properties.getOrDefault(CONTINUE_ONE_ERR_KEY, "false"));

		this.ignoreBlankString = Boolean.parseBoolean(properties.getOrDefault(IGNORE_BLANK_STR_KEY, "false"));

		this.spaceAfterSep = Boolean.parseBoolean(properties.getOrDefault(SPACE_AFTER_SEP_KEY, "false"));

		this.dateFormatter = DateTimeFormatter.ofPattern(properties.getOrDefault(OUTPUT_DATE_FORMAT_KEY, "yyyy-MM-dd"));

		this.mapper = new ObjectMapper();
	}

	@Override
	public String convert(String[] headers, List<?> values) {

		ObjectNode jsonNode = this.mapper.createObjectNode();

		for (int i = 0; i < headers.length; i++) {

			Object value = values.get(i);

			if (value instanceof TemporalAccessor) {

				jsonNode.put(headers[i], this.dateFormatter.format((TemporalAccessor) value));

			} else if (value instanceof Number) {

				writeToOriginalNumericClass(jsonNode, headers[i], value);

			} else {

				String string = value == null ? "" : value.toString().replaceAll(this.removeRegex, "");

				if (StringUtils.isNotBlank(this.sourceReplaceRegex)) {
					string = string.replaceAll(this.sourceReplaceRegex, this.targetReplaceRegex);
				}

				if (StringUtils.isBlank(string) && this.ignoreBlankString) {
					continue;
				}

				jsonNode.put(headers[i], this.trimString ? string.trim() : string);
			}
		}

		try {

			String retString = this.mapper.writeValueAsString(jsonNode);

			if (spaceAfterSep) {
				retString = retString.replace(":", ": ");
			}

			return retString;

		} catch (IOException e) {

			if (this.continueOnError) {
				return "";
			}

			throw new RuntimeException(e);
		}

	}

	public static void writeToOriginalNumericClass(ObjectNode jsonNode, String header, Object value) {
		if (value instanceof Integer) {
			jsonNode.put(header, (Integer) value);
		} else if (value instanceof Double) {
			jsonNode.put(header, (Double) value);
		} else if (value instanceof Float) {
			jsonNode.put(header, (Float) value);
		} else if (value instanceof Long) {
			jsonNode.put(header, (Long) value);
		} else if (value instanceof Short) {
			jsonNode.put(header, (Short) value);
		} else if (value instanceof Byte) {
			jsonNode.put(header, (Byte) value);
		}
	}

}
