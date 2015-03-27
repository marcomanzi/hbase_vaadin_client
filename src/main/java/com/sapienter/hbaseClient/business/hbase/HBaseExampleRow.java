package com.sapienter.hbaseClient.business.hbase;

import com.sapienter.hbaseClient.business.utils.ExceptionHelper;
import org.apache.hadoop.hbase.client.Result;

/**
 * Created by marcolin on 25/03/15.
 */
public class HBaseExampleRow implements ExceptionHelper {

    private String key;
    private String value;

    public HBaseExampleRow(Result next) {
        runConvertingToRuntime(() -> {
            this.key = new String(next.getRow(), "UTF-8");
            this.value = new String(next.getValue(HBaseExamples.exampleFamily, HBaseExamples.exampleQualifier), "UTF-8");
        });
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
