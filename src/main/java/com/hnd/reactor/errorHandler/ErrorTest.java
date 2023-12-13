package com.hnd.reactor.errorHandler;

import reactor.core.publisher.Flux;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/13 10:11
 */
public class ErrorTest {
    public static void main(String[] args) {

    }
/*
 * @Author hnd
 * @Date 17:49 2023/12/13
 * 发射包装后的异常
**/
    private static void onErrorMapToNewException() {
        Flux.range(0, 10).map(i -> {
            return i + "";
        }).map(s -> {
            if (s.equals("4")) {
                throw new RuntimeException("got it " + 4);
            }
            return s;
        }).onErrorMap(e -> new BusinessException("dd")).subscribe(System.out::println,System.out::println);
    }

    /*
     * @Author hnd
     * @Date 11:10 2023/12/13
     *onErrorResume 可以发射一个新的值，或者一个包装后的异常
     * 发射包装后的异常
     **/
    private static void onErrorResumeToNewException() {
        Flux.range(0, 10).map(i -> {
            return i + "";
        }).map(s -> {
            if (s.equals("4")) {
                throw new RuntimeException("got it " + 4);
            }
            return s;
        }).onErrorResume(e -> Flux.error(new BusinessException(e.getMessage()))
        ).subscribe(System.out::println, System.out::println);
    }

    /*
     * @Author hnd
     * @Date 11:10 2023/12/13
     *onErrorResume 可以发射一个新的值，或者一个包装后的异常
     * 发射新值
     **/
    private static void onErrorResumeToNewValue() {
        Flux<String> stringFlux = Flux.range(0, 10).map(i -> {
            return i + "";
        }).map(s -> {
            if (s.equals("4")) {
                throw new RuntimeException("got it " + 4);
            } else if (s.equals("5")) {
                throw new RuntimeException("got it " + 5);
            }
            return s;
        }).onErrorResume(throwable -> {//只要抛出了异常就会停止序列
            if (throwable.getMessage().equals("got it 4"))
                return Flux.just("44");//将异常转成正常序列，但这个是序列的最后一个值，
            else if (throwable.getMessage().equals("got it 5"))
                return Flux.just("55");
            else
                return Flux.error(new RuntimeException("unknown exception"));
        });
        stringFlux.subscribe(System.out::println, e -> {
            System.out.println("error:" + e);
        });
        String s = stringFlux.blockLast();
        System.out.println("last " + s);


    }

    private static void onErrorReturn() {
        //根据异常类型
        Flux.range(0, 10).map(i -> {
            return i + "";
        }).map(s -> {
            if (s.equals("5")) {
                throw new RuntimeException("got it " + s);
            }
            return s;
        }).onErrorReturn(MatchException.class, "got it error").subscribe(s -> System.out.println(s), System.out::println);

        //自定义逻辑
        Flux.range(0, 10).map(i -> {
            return i + "";
        }).map(s -> {
            if (s.equals("5")) {
                throw new RuntimeException("got it " + s);
            }
            return s;
        }).onErrorReturn(e -> {
            if (e.getMessage().startsWith("got it")) {
                return true;
            }
            return false;
        }, "捕获异常").subscribe(System.out::println, e -> {
            System.out.println("error");
        });
    }
}
