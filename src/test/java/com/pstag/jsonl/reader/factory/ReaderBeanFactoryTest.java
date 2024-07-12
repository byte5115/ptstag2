package com.pstag.jsonl.reader.factory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pstag.jsonl.converter.IConverter;
import com.pstag.jsonl.converter.factory.ConverterBeanFactory;

@SpringBootTest
public class ReaderBeanFactoryTest {

	@Autowired
	private ReaderBeanFactory readerBeanFactory;

	@Test
	public void testGetCsvReaderInstance() {

	}

	@Test
	public void testGetInvalidInstance() {

	}
}
