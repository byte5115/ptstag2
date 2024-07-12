package com.pstag.jsonl.converter.factory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pstag.jsonl.converter.IConverter;

@Component
public class ConverterBeanFactory {

	@Autowired
	private BeanFactory beanFactory;

	public IConverter getConverterBean(String outputFormat) {
		return beanFactory.getBean(outputFormat, IConverter.class);
	}
	

}

