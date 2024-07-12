package com.pstag.jsonl.reader.factory;

import java.io.Reader;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import com.pstag.jsonl.reader.IReader;
import com.pstag.jsonl.reader.IReaderConfig;

@Component
public class ReaderBeanFactory {

	@Autowired
	private BeanFactory beanFactory;

	public IReader getReaderBean(String fileFormat, Reader reader, ApplicationArguments args) {

		IReaderConfig iReaderConfig = this.beanFactory.getBean(fileFormat + "-config", IReaderConfig.class);

		return (IReader) this.beanFactory.getBean(fileFormat, reader, iReaderConfig.getConfig(args));

	}

}
