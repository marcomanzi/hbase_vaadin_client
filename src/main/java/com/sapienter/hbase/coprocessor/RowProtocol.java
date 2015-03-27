package com.sapienter.hbase.coprocessor;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;

import java.util.List;

/**
 * Created by marcolin on 25/03/15.
 */
public interface RowProtocol extends CoprocessorProtocol {
    List<Result> retrieveAllRows();
}
