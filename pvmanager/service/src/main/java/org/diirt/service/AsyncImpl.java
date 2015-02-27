package org.diirt.service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 *
 * @author Aaron
 */
public interface AsyncImpl {

    public void execute(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback);

    public void addExecutor(ExecutorService executorService); //?
}
