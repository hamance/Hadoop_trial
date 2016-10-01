package hdfs_api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;

/**
 * Created by HuangTian on 01/10/2016.
 */
public class list {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        DistributedFileSystem hdfs = (DistributedFileSystem) fs;
        DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();

        String[] names = new String[dataNodeStats.length];

        for (int i=0; i<dataNodeStats.length;i++) {
            names[i] = dataNodeStats[i].getHostName();
        }

    }
}
