package com.hnd.reactor.create;

import com.hnd.reactor.listener.MyEventProcessor;
import com.hnd.reactor.listener.MyListener;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
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
                    data.forEach(s -> {sink.next(s);
                        if (s.equals("b")) {
                           sink.error(new RuntimeException("嘎 了"));
                        }
                        if (s.equals("onDispose")) {
                            sink.complete();
                        } else if (s.equals("onCancel")) {

                        }});
                }

                @Override
                public void processComplete() {
                    System.out.println("processComplete");
                }

                @Override
                public void processError(Exception e) {
                    System.out.println(e);
                }
            });

            sink.onRequest(n -> {
                System.out.println("==============" + n);
                List<String> requests = myEventProcessor.request((int)n);
                requests.forEach(s -> {
                    sink.next(s);
                });
            }).onCancel(()->System.out.println("onCancel-> onDispose-> dispose"))
                    .onDispose(()->System.out.println("onRequest -> onDispose-> dispose"));

        }).doOnError(System.out::println).subscribe(new BaseSubscriber<>() {
            @Override
            public void dispose() {
                System.out.println("dddddd");
            }

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
               request(15);
            }

            @Override
            protected void hookOnNext(Object value) {
                System.out.println(value);
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("hookOnComplete");
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                System.out.println("dddd"+throwable);
            }

        });

        myEventProcessor.publish("h","7","k","a","b","c","d","e","f","5","g","h");
        myEventProcessor.publish("onDispose","onCancel");
    }
}
