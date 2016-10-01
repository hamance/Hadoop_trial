package hdfs_api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by HuangTian on 01/10/2016.
 */
public class upload {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path src = new Path("/home/hadoop/a.jar");
        Path dst = new Path("/");

        hdfs.copyFromLocalFile(src, dst);

        FileStatus files[] = hdfs.listStatus(dst);

        for (FileStatus file : files) {
            System.out.println(file.getPath());
        }
    }
}
