package com.pstag.jsonl.reader;

import java.util.Map;

import org.springframework.boot.ApplicationArguments;

public interface IReaderConfig {

	public Map<String, String> getConfig(ApplicationArguments args);

}
