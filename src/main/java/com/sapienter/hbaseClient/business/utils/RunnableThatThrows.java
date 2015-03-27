package com.sapienter.hbaseClient.business.utils;

/**
 * Created by marcolin on 25/03/15.
 */
@FunctionalInterface
public interface RunnableThatThrows {
    void run() throws Exception;
}