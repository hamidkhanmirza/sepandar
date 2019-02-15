package com.fanap.sepandar.utils.sharedClasses.myCompleteableCall;

/**
 * Created by admin123 on 1/17/2017.
 */
@FunctionalInterface
public interface UniResultFunction<T> {
    public void accept(T t);
}
