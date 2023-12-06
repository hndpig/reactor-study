package com.hnd.reactor.create;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author hnd
 * @description: create 异步生产序列
 * @date 2023/12/6 10:50
 */
public class BridgeCreate {
    public static void main(String[] args) {
        MyEventProcessor myEventProcessor = new MyEventProcessor();
        Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> sink) {
                myEventProcessor.register(new MyListener<String>() {
                    @Override
                    public void onDataChunk(List<String> data) {
                        data.forEach(s -> sink.next(s));
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
            }
        }).subscribe(System.out::println);


        List<String> strs = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            strs.add(i + "");
            if (i > 9 && i % 10 == 0) {
                List<String> finalStrs = strs;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> collect = finalStrs.stream().map(s -> s + ":" + Thread.currentThread().getName()).collect(Collectors.toList());
                        myEventProcessor.publish(collect);
                    }
                }).start();
                strs = new ArrayList<>();
            }
        }

    }
}
