package com.hnd.reactor.push;

import com.hnd.reactor.listener.MyEventProcessor;
import com.hnd.reactor.listener.MyListener;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/6 16:02
 */
public class BridgePush {
    public static void main(String[] args) {
        MyEventProcessor myEventProcessor = new MyEventProcessor();
        Flux.push(sink -> {
            myEventProcessor.register(new MyListener<String>() {
                @Override
                public void onDataChunk(List<String> data) {
                    data.forEach(s ->{
                        sink.next(s);
                        if ("done".equals(s)) {
                            processComplete();
                        }
                    });
                }

                @Override
                public void processComplete() {
                    sink.complete();
                }

                @Override
                public void processError(Exception e) {
                    sink.error(e);
                }
            });
        }).subscribe(s -> {
            System.out.println(s);
        }, Throwable::printStackTrace, () -> {
            System.out.println("is completed");
        });

        List<String> strings = new ArrayList<>();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                strings.add(i + "");
            }
            myEventProcessor.publish(strings);
            myEventProcessor.publish("done");
        }).start();


    }
}
