package com.parquet;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;

import java.io.File;
import java.io.IOException;

/**
 * @author phil.zhang
 * @date 2019/2/15
 */
public class Avro2Parquet {

    public static void main(String[] args) throws IOException {
        File f = new File(args[0]);
        Path parPath = new Path(args[1]);
        DataFileReader<Object> reader = new DataFileReader<>(f, new GenericDatumReader<>());
        Schema schema = reader.getSchema();


        ParquetWriter<Object> writer = AvroParquetWriter.builder(parPath)
                .withSchema(schema)
                .withConf(new Configuration())
                .build();
        while (reader.hasNext()) {
            Object record = reader.next();
            writer.write(record);
        }
        writer.close();
    }
}
