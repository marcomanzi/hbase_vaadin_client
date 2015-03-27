package com.sapienter.hbaseClient.business.utils;

import java.util.List;

/**
 * Created by marcolin on 25/03/15.
 */
@FunctionalInterface
public interface CallableThatThrows<R> {
    R apply() throws Exception;
}