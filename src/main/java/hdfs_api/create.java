package hdfs_api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by HuangTian on 01/10/2016.
 */
public class create {

    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        byte[] buff = "Hello world!".getBytes();

        FileSystem hdfs = FileSystem.get(conf);
        Path dfs = new Path("/test");

        FSDataOutputStream outputStream = hdfs.create(dfs);
        outputStream.write(buff, 0, buff.length);
    }
}
