package com.pstag.jsonl.reader;

import java.util.List;
import java.util.stream.Stream;

public interface IReader extends AutoCloseable {

	public String[] getHeader();

	public Stream<List<?>> getStream();
}
