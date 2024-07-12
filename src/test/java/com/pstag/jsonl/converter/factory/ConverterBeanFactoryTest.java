package com.pstag.jsonl.converter.factory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.pstag.jsonl.converter.IConverter;

@SpringBootTest
public class ConverterBeanFactoryTest {

	@Autowired
	private ConverterBeanFactory converterBeanFactory;

	@Test
	public void testGetJSONInstance() {
		DefaultApplicationArguments defaultApplicationArguments = new DefaultApplicationArguments("");

		IConverter iconverter = converterBeanFactory.getConverterBean("JSON", defaultApplicationArguments);

		assertTrue(iconverter != null);
	}

	@Test
	public void testGetInvalidInstance() {
		DefaultApplicationArguments defaultApplicationArguments = new DefaultApplicationArguments("");

		assertThrows(NoSuchBeanDefinitionException.class,
				() -> converterBeanFactory.getConverterBean("XML", defaultApplicationArguments));
	}

}