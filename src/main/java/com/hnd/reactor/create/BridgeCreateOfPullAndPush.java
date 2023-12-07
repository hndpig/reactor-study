package com.hnd.reactor.create;

import com.hnd.reactor.listener.MyEventProcessor;
import com.hnd.reactor.listener.MyListener;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/6 16:50
 * 不理解
 */
public class BridgeCreateOfPullAndPush {
    public static void main(String[] args) {
        MyEventProcessor myEventProcessor = new MyEventProcessor();

        Flux.create(sink -> {
            myEventProcessor.register(new MyListener<String>() {
                @Override
                public void onDataChunk(List<String> data) {
                    data.forEach(s -> sink.next(s));
                }

                @Override
                public void processComplete() {
                    System.out.println("processComplete");
                }

                @Override
                public void processError(Exception e) {
                    e.printStackTrace();
                }
            });
            sink.onRequest(n -> {
                System.out.println("==============" + n);
                List<String> requests = myEventProcessor.request((int)n);
                requests.forEach(s -> sink.next(s));
            });
        }).subscribe(new BaseSubscriber<>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
               request(15);
            }

            @Override
            protected void hookOnNext(Object value) {
                System.out.println(value);
            }
        });

        myEventProcessor.publish("h","n","k","a","b","c","d","e","f","g","h");
    }
}
