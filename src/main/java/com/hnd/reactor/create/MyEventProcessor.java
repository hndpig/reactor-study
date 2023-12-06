package com.hnd.reactor.create;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/6 10:51
 */
public class MyEventProcessor {
    List<MyListener> listeners = new ArrayList<>();

    public void register(MyListener myListener) {
        listeners.add(myListener);
    }

    public void remove(MyListener myListener) {
        if (listeners.contains(myListener)) {
            listeners.remove(myListener);
        }
    }
    public void publish(List<String> strings) {
        listeners.forEach(listeners->{
            listeners.onDataChunk(strings);
        });
    }
}
