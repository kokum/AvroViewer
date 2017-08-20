package jp.stread.avro;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.IndexedRecord;
import org.apache.avro.io.DatumReader;
import org.codehaus.jackson.map.ObjectMapper;

public class AvroReader {
	
	private Schema schema = null;
	
	public String rootName = null;
	
	private File f = null;
	
	private final static int MAX_NUM_LOAD = 10;
	
	public String getRootName() {
		return schema.getName();
	}
	public AvroReader(File file) {
		FileInputStream inputStream = null;
		BufferedInputStream in = null;
		DataFileStream<IndexedRecord> reader = null;
		try {
			f = file;
			inputStream = new FileInputStream(f);
			in = new BufferedInputStream(inputStream, 256 * 128);
			reader = new DataFileStream<IndexedRecord>(in, new GenericDatumReader<IndexedRecord>());
			schema = reader.getSchema();
		} catch (Exception e) {
		}
		finally {
			try {
				if(reader != null) reader.close();
				if(in != null) in.close();
				if(inputStream != null) inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public List<LinkedHashMap<String,String>> read() {
		DataFileReader<GenericRecord> dataFileReader = null;
		List<LinkedHashMap<String,String>> result = new ArrayList<LinkedHashMap<String,String>>();
		try {
			DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
			dataFileReader = new DataFileReader<GenericRecord>(f, datumReader);
			GenericRecord record = null;
			int i = 0;
			while (i <= MAX_NUM_LOAD && dataFileReader.hasNext()) {
				i++;
				record = dataFileReader.next(record);
				LinkedHashMap<String,String> element = new ObjectMapper().readValue(record.toString(), LinkedHashMap.class);
				result.add(element);
			}
			
		} catch (Exception e) {
		}
		finally {
			try {
				if(dataFileReader != null) dataFileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
