package com.hnd.reactor.create;

import com.hnd.reactor.listener.MyEventProcessor;
import com.hnd.reactor.listener.MyListener;
import reactor.core.publisher.Flux;

import java.util.List;

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
                System.out.println("=============="+n);
                List<String> requests = myEventProcessor.request(Long.bitCount(n));
                requests.forEach(s -> sink.next(s));
            });
        }).subscribe(System.out::println);
        myEventProcessor.publish("h","n","k","a","b","c");
    }
}
