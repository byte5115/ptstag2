package com.pstag.jsonl.converter.factory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import com.pstag.jsonl.converter.IConverter;
import com.pstag.jsonl.converter.IConverterConfig;
import com.pstag.jsonl.reader.IReader;
import com.pstag.jsonl.reader.IReaderConfig;

@Component
public class ConverterBeanFactory {

	@Autowired
	private BeanFactory beanFactory;

	public IConverter getConverterBean(String outputFormat, ApplicationArguments args) {

		IConverterConfig iConverterConfig = this.beanFactory.getBean(outputFormat + "-config", IConverterConfig.class);

		return (IConverter) this.beanFactory.getBean(outputFormat, iConverterConfig.getConfig(args));
	}

}
