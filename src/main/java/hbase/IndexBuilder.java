package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by HuangTian on 01/10/2016.
 */
public class IndexBuilder {

    public static final byte[] INDEX_COLUMN = Bytes.toBytes("INDEX");

    public static final byte[] INDEX_QUALIFIER = Bytes.toBytes("ROW");


    public static class Map extends Mapper<ImmutableBytesWritable, Result, ImmutableBytesWritable, Writable> {

        private byte[] family;

        private HashMap<byte[], ImmutableBytesWritable> indexes;

        protected void map(ImmutableBytesWritable rowKey, Result result, Context context) throws IOException, InterruptedException{

            for (java.util.Map.Entry<byte[], ImmutableBytesWritable> index : indexes.entrySet()) {

                byte[] qualifier = index.getKey();

                ImmutableBytesWritable tableName = index.getValue();

                byte[] value = result.getValue(family, qualifier);

                if (value != null) {

                    Put put = new Put(value);
                    put.add(INDEX_COLUMN, INDEX_QUALIFIER, rowKey.get());

                    context.write(tableName, put);
                }
            }

        }

        protected void setup(Context context) throws IOException, InterruptedException {

            Configuration configuration = context.getConfiguration();

            String tableName = configuration.get("index.tablename");
            String[] fields = configuration.getStrings("index.fields");

            String familyName = configuration.get("index.familyname");
            family = Bytes.toBytes(familyName);

            indexes = new HashMap<byte[], ImmutableBytesWritable>();
            for (String field : fields) {
                indexes.put(Bytes.toBytes(field), new ImmutableBytesWritable(Bytes.toBytes(tableName + '-' + field)));
            }
        }
    }


    public static Job configurejob (Configuration conf, String[] args) throws IOException {

        String tableName = args[0];
        String columnFamily = args[1];
        System.out.println("****" + tableName);

        conf.set(TableInputFormat.SCAN, TableMapReduceUtil.convertScanToString(new Scan()));
        conf.set(TableInputFormat.INPUT_TABLE, tableName);

        conf.set("index.tablename", tableName);
        conf.set("index.familyname", columnFamily);


        String[] fields = new String[args.length-2];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = args[i+2];
        }
        conf.setStrings("index.fields", fields);
        conf.set("index.familyname", "attributes");

        Job job = new Job(conf, tableName);
        job.setJarByClass(IndexBuilder.class);
        job.setMapperClass(Map.class);
        job.setNumReduceTasks(0);
        job.setInputFormatClass(TableInputFormat.class);
        job.setOutputFormatClass(MultiTableOutputFormat.class);
        return job;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

        if (otherArgs.length < 3) {
            System.err.println("Only " + otherArgs.length + " arguments supplied, required: 3");
            System.err.println("Usage: IndexBuilder <TABLE_NAME> <COLUMN_FAMILY> <ATTR> [<ATTR> ...]");
            System.exit(-1);
        }

        Job job = configurejob(conf, otherArgs);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
