package hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

import java.util.Map;

/**
 * Created by HuangTian on 01/10/2016.
 */
public class HBasicOperation {

    public static void main(String[] args) throws Exception {

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "UbuntuSlave1, UbuntuSlave2, UbuntuSlave3");
        HBaseAdmin admin = new HBaseAdmin(conf);

        HTableDescriptor tableDescriptor = new HTableDescriptor("tab1".getBytes());
        tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
        admin.createTable(tableDescriptor);
        HTable table = new HTable(conf, "tab1");

        Put putRow1 = new Put("row1".getBytes());
        putRow1.add("fam1".getBytes(), "col1".getBytes(), "val1".getBytes());
        table.put(putRow1);


        for (Result row : table.getScanner("fam1".getBytes())) {

            for (Map.Entry<byte[], byte[]> entry : row.getFamilyMap("fam1".getBytes()).entrySet()) {
                String column = new String(entry.getKey());
                String value = new String(entry.getValue());

            }
        }

        admin.disableTable("tab1");
        admin.deleteTable("tab1");
        }
}
