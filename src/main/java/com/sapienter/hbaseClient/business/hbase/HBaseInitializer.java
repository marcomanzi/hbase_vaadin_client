package com.sapienter.hbaseClient.business.hbase;

import com.sapienter.hbaseClient.business.utils.ExceptionHelper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

/**
 * Created by marcolin on 25/03/15.
 */
public class HBaseInitializer implements ExceptionHelper {
    private HBaseAdmin admin;
    private Configuration configuration;
    private static String zookeeperHost = "localhost";
    private static HBaseInitializer instance;

    private byte[] exampleFamily = toBytes("exampleFamily");
    private byte[] exampleTable = toBytes("exampleTable");

    private HBaseInitializer() {
        runConvertingToRuntime(() -> {
            configuration =  HBaseConfiguration.create();
            configuration.setInt("timeout", 5000);
            configuration.set("hbase.zookeeper.quorum",zookeeperHost);
            configuration.set("hbase.zookeeper.property.clientPort", "2181");
            admin = new HBaseAdmin(configuration);
        });
    }

    public static HBaseInitializer get() {
        if (instance == null) {
            instance = new HBaseInitializer();
            instance.initHBase();
        }
        return instance;
    }

    private void initHBase() {
        runConvertingToRuntime(() -> {
            if (!admin.tableExists(exampleTable))
                createDefaultTable();
        });
    }

    public void createDefaultTable() {
        runConvertingToRuntime(() -> {
            HTableDescriptor table = new HTableDescriptor(exampleTable);
            HColumnDescriptor family = new HColumnDescriptor(exampleFamily);
            table.addFamily(family);
            admin.createTable(table);
        });
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
