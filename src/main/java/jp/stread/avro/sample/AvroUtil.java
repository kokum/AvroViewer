package jp.stread.avro.sample;

import java.io.File;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

public class AvroUtil {
    public static void main(String... args) throws Exception {
        // Schema Definition
        Schema schema = new Schema.Parser().parse(new File("/works/eclipse-oxygen/AvroViewer/src/test/resources/guide.avsc"));
 
        // User Records
        GenericRecord userInfo = new GenericData.Record(schema);
        userInfo.put("username", "漢字１");
        userInfo.put("age", 20);
        Schema s = schema.getField("address1").schema();
        GenericRecord address = new GenericData.Record(s);
        address.put("street", "main street");
        address.put("city", "Setagaya-ku");
        
        s = s.getField("phone").schema();
        GenericRecord phone = new GenericData.Record(s);
        phone.put("phone1", "phone 001-000");
        phone.put("phone2", "phone 002-000");
        address.put("phone", phone);
        userInfo.put("address1", address);
        
        s = schema.getField("address2").schema();
        address = new GenericData.Record(s);
        address.put("street", "sub street");
        address.put("city", "Chiyoda-ku");
        userInfo.put("address2", address);

        // Serialize
        File file = new File("guide.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(userInfo);
        //dataFileWriter.append(userInfo2);
        dataFileWriter.close();
    }
}