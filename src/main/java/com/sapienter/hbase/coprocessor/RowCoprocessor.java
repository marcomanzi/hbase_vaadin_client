package com.sapienter.hbase.coprocessor;


import com.sapienter.hbase.coprocessor.RowProtocol;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;

import java.util.ArrayList;
import java.util.List;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

/**
 * Created by marcolin on 25/03/15.
 */
public class RowCoprocessor extends BaseEndpointCoprocessor implements
        RowProtocol {
    public static byte[] exampleTable = toBytes("exampleTable");

    @Override
    public List<Result> retrieveAllRows(){
        RegionCoprocessorEnvironment environment = (RegionCoprocessorEnvironment) getEnvironment();
        // use an internal scanner to perform scanning.
        InternalScanner scanner = null;
        List<Result> rows = new ArrayList<>();
        try {
            HTableInterface table = environment.getTable(exampleTable);
            table.getScanner(new Scan()).forEach(c ->
                            rows.add(c)
            );
            return rows;
        } catch (Exception e) {} finally {
            if (scanner != null) try {scanner.close();}catch (Exception e) {}
        }
        return rows;
    }
}