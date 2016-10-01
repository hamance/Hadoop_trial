package hdfs_api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by HuangTian on 01/10/2016.
 */
public class rename {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path frpath = new Path("/test");
        Path topath = new Path("/test1");
        boolean isRename = hdfs.rename(frpath, topath);
    }
}
