package com.hnd.reactor.create;

import java.util.List;

public interface MyListener<T> {
    void onDataChunk(List<T> data);

    void processComplete();

    void processError(Exception e);
}
