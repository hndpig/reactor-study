package com.hnd.reactor.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/6 10:51
 */
public class MyEventProcessor {
    List<MyListener> listeners = new ArrayList<>();
    List<String> requests = null;

    public MyEventProcessor(){
        requests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            requests.add("pull:" + i);
        }
    }

    public void register(MyListener myListener) {
        listeners.add(myListener);
    }

    public void remove(MyListener myListener) {
        if (listeners.contains(myListener)) {
            listeners.remove(myListener);
        }
    }

    public void publish(List<String> strings) {
        listeners.forEach(listeners -> {
            listeners.onDataChunk(strings);
        });
    }

    public void publish(String... values) {
        listeners.forEach(listeners -> {
            listeners.onDataChunk(Arrays.stream(values).toList());
        });
    }


    public List<String> request(int n) {
        if (n > 10) {
            n = 10;
        }
        List<String> results = requests.subList(0, n);
        return results;
    }
}
