package com.hnd.reactor.errorHandler;

/**
 * @author hnd
 * @description: TODO
 * @date 2023/12/13 11:00
 */
public class BusinessException extends Exception {
    private String errorMsg;
    public BusinessException(){}
    public BusinessException(String errorMsg){
        this.errorMsg = "BusinessException:"+errorMsg;
    }

    @Override
    public String toString() {
        return  errorMsg;
    }
}
