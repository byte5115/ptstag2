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
					+ "[--inFormat=<input file format>] "  // CSV only for now 
					+ "[--outFormat=<output file format>] " // JSON only for now
					+ "[--buffersize=<buffer size>] " // JSON only for now
					+ "[--removeRegex=<string to be removed in the values in output file>] " // remove chars from output file
					+ "[--soureRegexReplace=<string to be replaced in the values in output file>] " 
					+ "[--targetRegexReplace=<replacement string to for soureRegexReplace>] " 
					+ "[--sep=<separator>] " // separator char used input files 
					+ "[--quote=<quote character>] " // quote char used input files 
					+ "[--esc=<escape character>] "  // esc char used input files
					+ "[--addFormats=<additional date formats comma separated>] "
					+ "[--outputDateFormat=<date format for output file>] "
					+ "[--trimSpaces] " // remove leading space in output file 
					+ "[--ignoreBlankString] " // ignore blank string in output file  
					+ "[--spaceAfterSep] " // add space after value separator when jsonifying 					
					+ "[--continueOnError] " // ignore exception when jsonifying
					+ "[--ignoreQuote] " // ignore quotations when processing input files
					+ "[--ignoreLeading] " // ignore leading white space when processing input files
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

		if (args.getOptionNames().contains("inFormat")) {
			inputFormat = args.getOptionValues("inFormat").get(0);
		}

		if (args.getOptionNames().contains("outFormat")) {
			outputFormat = args.getOptionValues("outFormat").get(0);
		}

		if (args.getOptionNames().contains("buffersize")) {
			bufferSize = Integer.parseInt(args.getOptionValues("buffersize").get(0));
		}
		
		IConverter iconverter = converterBeanFactory.getConverterBean(outputFormat, args);

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
							bufferedWriter.newLine() ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
			}
		}

	}

}
