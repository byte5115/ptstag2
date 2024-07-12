package com.pstag.jsonl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pstag.jsonl.converter.IConverter;
import com.pstag.jsonl.converter.factory.ConverterBeanFactory;
import com.pstag.jsonl.reader.IReader;
import com.pstag.jsonl.reader.factory.ReaderBeanFactory;

@SpringBootApplication
public class JsonlApplication implements ApplicationRunner {

	@Autowired
	private ReaderBeanFactory readerBeanFactory;

	@Autowired
	private ConverterBeanFactory converterBeanFactory;

	public static void main(String[] args) {
		SpringApplication.run(JsonlApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		List<String> argSet = args.getNonOptionArgs();

		if (argSet.size() < 2) {
			System.out.println("Usage: java -jar <jar> " 
					+ "[--informat=<input file format>] "  // CSV only for now 
					+ "[--outformat=<output file format>] " // JSON only for now
					+ "[--buffersize=<buffer size>] " // JSON only for now
					+ "[--sep=<separator>] " 
					+ "[--qoute=<quote character>] "
					+ "[--esc=<escape character>] " 
					+ "[--addFormats=<additional date formats comma separated>] "
					+ "[--iquote] " // ignore quotations
					+ "[--ileading] " // ignore leading white space
					+ "[--force] " // force override
					+ "[input files...] <output file>");
			return;
		}

		String outputFilename = argSet.get(argSet.size()-1);
		String outputFormat = "JSON";
		String inputFormat = "CSV";
		int bufferSize = 8192 ;
		
		List<String> inputFilenameList = argSet.subList(0, argSet.size() - 1);
		
		if (args.getOptionNames().contains("force") == false && new File(outputFilename).exists() ) {
			System.out.println( "Existing output file exist ! Please use --f to force override." ) ; 
			return ; 
		}

		if (args.getOptionNames().contains("informat")) {
			inputFormat = args.getOptionValues("informat").get(0);
		}

		if (args.getOptionNames().contains("outformat")) {
			outputFormat = args.getOptionValues("outformat").get(0);
		}

		if (args.getOptionNames().contains("buffersize")) {
			bufferSize = Integer.parseInt(args.getOptionValues("buffersize").get(0));
		}
		
		IConverter iconverter = converterBeanFactory.getConverterBean(outputFormat);

		try (FileWriter fileWriter = new FileWriter(outputFilename);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter , bufferSize)) {

			for (String inputFilename : inputFilenameList) {

				FileReader fileReader = new FileReader(inputFilename);

				try (IReader reader = readerBeanFactory.getReaderBean(inputFormat, fileReader, args)) {

					String[] headers = reader.getHeader();

					Stream<List<?>> stream = reader.getStream();

					stream.forEach(dataRow -> {
						try {
							bufferedWriter.append(iconverter.convert(headers, dataRow));
							bufferedWriter.append("\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
			}
		}

	}

}
