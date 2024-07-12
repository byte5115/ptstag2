package com.pstag.jsonl.converter;

import java.util.List;

public interface IConverter {

	public String convert(String[] headers, List<?> values);

}
