package com.pstag.jsonl.reader.factory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;

import com.pstag.jsonl.converter.IConverter;
import com.pstag.jsonl.converter.factory.ConverterBeanFactory;
import com.pstag.jsonl.reader.IReader;

@SpringBootTest
public class ReaderBeanFactoryTest {

	@Autowired
	private ReaderBeanFactory readerBeanFactory;

	@Test
	public void testGetCsvReaderInstance() {
		StringReader stringReader = new StringReader("");
		DefaultApplicationArguments defaultApplicationArguments = new DefaultApplicationArguments("");

		IReader ireader = readerBeanFactory.getReaderBean("CSV", stringReader, defaultApplicationArguments);

		assertTrue(ireader != null);
	}

	@Test
	public void testGetInvalidInstance() {
		StringReader stringReader = new StringReader("");
		DefaultApplicationArguments defaultApplicationArguments = new DefaultApplicationArguments("");

		assertThrows(NoSuchBeanDefinitionException.class,
				() -> readerBeanFactory.getReaderBean("XML", stringReader, defaultApplicationArguments));
	}
}
