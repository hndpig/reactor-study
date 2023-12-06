package com.hnd.reactor.create;

import java.util.List;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/6 10:51
 */
public class MyListenerImpl implements MyListener<String>{
    @Override
    public void onDataChunk(List<String> data) {
        data.forEach(System.out::println);
    }

    @Override
    public void processComplete() {
        System.out.println("processComplete");
    }

    @Override
    public void processError(Exception e) {
        System.out.println(e);
    }
}
