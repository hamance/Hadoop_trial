package hdfs_api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by HuangTian on 01/10/2016.
 */
public class locate {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);

        Path fpath = new Path("/test1");

        FileStatus fileStatus = hdfs.getFileStatus(fpath);
        BlockLocation[] blockLocations = hdfs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        int blockLen = blockLocations.length;

        for (int i=0; i<blockLen; i++) {
            String[] hosts = blockLocations[i].getHosts();
        }
    }
}
