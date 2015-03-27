package com.sapienter.hbaseClient.business.hbase;

import com.sapienter.hbase.coprocessor.RowProtocol;
import com.sapienter.hbaseClient.business.utils.ExceptionHelper;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.filter.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

/**
 * Created by marcolin on 25/03/15.
 */
public class HBaseExamples implements ExceptionHelper {
    private static HBaseExamples instance;
    private static int cdrsCounter = 1;
    public static byte[] exampleFamily = toBytes("exampleFamily");
    public static byte[] exampleTable = toBytes("exampleTable");
    public static byte[] exampleQualifier = toBytes("exampleQualifier");
    public static byte[] counterQualifier = toBytes("counter");
    private HTablePool pool;
    private static int numberOfCdrs = 0;
    private int caching;

    private HBaseExamples() {}


    public static HBaseExamples get(int caching) {
        if (instance == null) {
            instance = new HBaseExamples();
            instance.init();
        }
        instance.caching = caching;
        return instance;
    }

    private void init() {
        runConvertingToRuntime(
                () -> instance.pool = new HTablePool(HBaseInitializer.get().getConfiguration(), 40)
        );
    }

    public void create100000ExampleRows(String rowPrefix) {
        runConvertingToRuntime(() -> {
            HTableInterface table = pool.getTable(exampleTable);
            List<Put> puts = new ArrayList<>();
            IntStream.range(1, 100001).forEach(i -> addPutForHBase(rowPrefix, i, puts));
            table.put(puts);
        });
    }

    private void addPutForHBase(String rowPrefix, int i, List<Put> puts) {
        Put put = new Put(toBytes(rowPrefix + "-" + i));
        put.add(exampleFamily, exampleQualifier, toBytes("value" + i));
        put.add(exampleFamily, counterQualifier, toBytes(cdrsCounter++));
        puts.add(put);
    }

    public void deleteAllExampleRows() {
        runConvertingToRuntime(() -> {
            List<HBaseExampleRow> rows = retrieveAllExampleRows();
            HTableInterface table = pool.getTable(exampleTable);
            for (HBaseExampleRow row: rows) {
                table.delete(new Delete(toBytes(row.getKey())));
            }
        });
    }

    public List<HBaseExampleRow> retrieveAllExampleRows() {
        List<HBaseExampleRow> rows = scanExampleTableWith(new Scan());
        numberOfCdrs = rows.size();
        return rows;
    }

    public List<HBaseExampleRow> retrieveAllExampleRowsInMultiThreadingWithMoreColumnPagination() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<List<HBaseExampleRow>>> columnPaginationCallers = new ArrayList<>();
        Integer limit = numberOfCdrs/25;
        IntStream.range(0, 25).forEach(i ->
                columnPaginationCallers.add(
                        () -> retrievePaginatedRowsWithCounterOnColumn(limit, i)));
        try {
            List<HBaseExampleRow> rows = new ArrayList<>();
            executorService.invokeAll(columnPaginationCallers).forEach(future -> {
                try {rows.addAll(future.get());} catch (Exception e) {}
            });
            return rows;
        } catch (Exception e) {}
        return new ArrayList<>();
    }

    public List<HBaseExampleRow> retrievePaginatedRowsWithPageFilter(Integer limit, Integer offset) {
        return runConvertingToRuntime(() -> {
            String startRow = null;
            for (int page = 0; page < offset; page++) {
                List<HBaseExampleRow> rows = getRowsWithPageFilter(startRow, limit + 1);
                startRow = rows.get(rows.size() - 1).getKey();
            }
            return getRowsWithPageFilter(startRow, limit);
        });
    }

    private List<HBaseExampleRow> getRowsWithPageFilter(String startRow, Integer limit) {
        return scanExampleTableWith(startRow != null ? new Scan(toBytes(startRow)) : new Scan(), new PageFilter(limit));

    }

    public List<HBaseExampleRow> retrievePaginatedRowsWithColumnPaginationFilter(Integer limit, Integer offset) {
        return scanExampleTableWith(new Scan(), new ColumnPaginationFilter(limit, offset));
    }

    private List<HBaseExampleRow> getRowsWithPageFilterAndPrefixFilter(String prefix, String startRow, int limit) {
        return scanExampleTableWith(startRow != null ? new Scan(toBytes(startRow)) : new Scan(), new PageFilter(limit), new PrefixFilter(toBytes(prefix)));
    }

    public List<HBaseExampleRow> retrievePaginatedRowsWithCounterOnColumn(int limit, int offset) {
        return scanExampleTableWith(new Scan(),
                new SingleColumnValueFilter(
                        exampleFamily, counterQualifier, CompareFilter.CompareOp.GREATER_OR_EQUAL, new BinaryComparator(toBytes(offset))),
                new SingleColumnValueFilter(
                        exampleFamily, counterQualifier, CompareFilter.CompareOp.LESS, new BinaryComparator(toBytes(offset + limit)))
        );
    }

    private List<HBaseExampleRow> scanExampleTableWith(Scan scan, Filter... filters) {
        return runConvertingToRuntime( () -> {
            scan.setCaching(caching);
            List<HBaseExampleRow> rows = new ArrayList<>();
            HTableInterface table = pool.getTable(exampleTable);
            if (filters.length > 1) {
                scan.setFilter(new FilterList(FilterList.Operator.MUST_PASS_ALL, filters));
            } else if (filters.length > 0) {
                scan.setFilter(filters[0]);
            }
            table.getScanner(scan).forEach(c -> rows.add(new HBaseExampleRow(c)));
            return rows;
        });
    }

    public List<HBaseExampleRow> retrievePaginatedRowsWithPageFilter(String prefixForRowValue, int limit, int offset) {
        return runConvertingToRuntime(() -> {
            String startRow = null;
            for (int page = 0; page < offset; page++) {
                List<HBaseExampleRow> rows = getRowsWithPageFilterAndPrefixFilter(prefixForRowValue, startRow, limit + 1);
                startRow = rows.get(rows.size() - 1).getKey();
            }
            return getRowsWithPageFilterAndPrefixFilter(prefixForRowValue, startRow, limit);
        });
    }


}
