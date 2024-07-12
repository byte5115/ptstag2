package com.pstag.jsonl.reader.impl;


import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.ADDITIONAL_DATE_FORMATS_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.ESCAPE_CHAR_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.IGNORE_LEADING_WHITESPACE_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.IGNORE_QUOTATIONS_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.QUOTE_CHAR_KEY;
import static com.pstag.jsonl.reader.impl.CsvReaderConfigImpl.SEPARATOR_KEY;

import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import com.pstag.jsonl.reader.IReader;

@Component("CSV")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE )
public class CsvReaderImpl implements IReader {
	
	private static final List<DateTimeFormatter> DEFAULT_DATE_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    );	

	private final CSVReader csvReader;

	private final String[] headers;

	private final Stream<List<?>> stream;
	
	private final List<DateTimeFormatter> dateTimeFormatters ; 

	public CsvReaderImpl(Reader reader, Map<String, String> properties) throws Exception {

		this.csvReader = initCsvReader(reader, properties);

		this.headers = csvReader.readNext();
		
		this.dateTimeFormatters = initDateFormatter(properties) ;

		this.stream = initStream(csvReader) ; 
	}
	
	private CSVReader initCsvReader(Reader reader, Map<String, String> properties ) 
	{
		String quoteChar = properties.getOrDefault(
				QUOTE_CHAR_KEY, 
				String.valueOf(ICSVParser.DEFAULT_QUOTE_CHARACTER)) ;		
		
		String escapeChar = properties.getOrDefault(
				ESCAPE_CHAR_KEY, 
				String.valueOf(ICSVParser.DEFAULT_ESCAPE_CHARACTER)) ;	
		
		String separatorChar = properties.getOrDefault(
				SEPARATOR_KEY, 
				String.valueOf(ICSVParser.DEFAULT_SEPARATOR)) ;
		
		String ignoreQuotations = properties.getOrDefault(
				IGNORE_QUOTATIONS_KEY, 
				String.valueOf(ICSVParser.DEFAULT_IGNORE_QUOTATIONS)) ;
		
		String ignoreLeadingWhitespace = properties.getOrDefault(
				IGNORE_LEADING_WHITESPACE_KEY, 
				String.valueOf(ICSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE)) ;
		
		return new CSVReaderBuilder(reader)
				.withSkipLines(0)
				.withCSVParser(
						new CSVParserBuilder()
						.withSeparator(separatorChar.charAt(0))
						.withQuoteChar(quoteChar.charAt(0))
						.withEscapeChar(escapeChar.charAt(0))
						.withIgnoreQuotations(Boolean.valueOf(ignoreQuotations))
						.withIgnoreLeadingWhiteSpace(Boolean.valueOf(ignoreLeadingWhitespace))
						.build())
				.build();
	}

	
	private List<DateTimeFormatter> initDateFormatter( Map<String, String> properties)
	{		
		List<DateTimeFormatter> list = new ArrayList<>() ;

		String additionalFormats = properties.getOrDefault(ADDITIONAL_DATE_FORMATS_KEY, "") ;
		
		// user based format takes higher priority than the default formats  
        if (StringUtils.isNotBlank(additionalFormats)) {
            String[] formats = additionalFormats.split(",");
            for (String format : formats) {
            	list.add(DateTimeFormatter.ofPattern(format.trim()));
            }
        }
        
        list.addAll(DEFAULT_DATE_FORMATTERS) ; 
        
        return list ; 	
	}
	
	private Stream<List<?>> initStream(CSVReader csvReader)  
	{	
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(
						csvReader.iterator(), 
						Spliterator.NONNULL),
						false) 
				.map( this::checkDate ) ;		
	}
	
	private List<?> checkDate(String[] values) {
		return Arrays
				.asList(values)
				.stream()
				.map(value -> {
			for (DateTimeFormatter formatter : this.dateTimeFormatters) {
				try {
					// is a date
					return LocalDate.parse(value, formatter);
				} catch (DateTimeParseException e) {
					// go to the next
				}
			}
			// not a date 
			return value;
		}).toList();
	}

	@Override
	public String[] getHeader() {
		return Arrays.copyOf(headers, headers.length);
	}

	@Override
	public Stream<List<?>> getStream() {
		return this.stream;
	}

	@Override
	public void close() throws Exception {
		this.csvReader.close();
	}

}
