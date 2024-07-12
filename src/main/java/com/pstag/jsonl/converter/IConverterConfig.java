package com.pstag.jsonl.converter;

import java.util.Map;

import org.springframework.boot.ApplicationArguments;

public interface IConverterConfig {

	public Map<String, String> getConfig(ApplicationArguments args);

}
