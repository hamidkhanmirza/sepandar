package com.fanap.sepandar.utils.sharedClasses.myCompleteableCall;


import com.fanap.sepandar.utils.sharedClasses.myCompleteableCall.timer.IRequestTimeOutManager;
import com.fanap.sepandar.utils.sharedClasses.myCompleteableCall.timer.RequestTimeOutManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by admin123 on 1/17/2017.
 */
public class MyUniCompleableCall<T> {
    final static Logger logger = LogManager.getLogger();

    static IRequestTimeOutManager requestTimeOutManager =
            new RequestTimeOutManager();

    UniResultFunction<T> applyFn;
    UniResultFunction<Object> exceptionFn;

    Vector<T> unReportedResultVector = new Vector();
    int unReportedResultIndex = 0;
    int maxUnReportCache = 50;

    Vector<Object> unReportedExceptionVector = new Vector();
    int unReportedExceptionIndex = 0;

    AtomicBoolean timedOut = new AtomicBoolean(false);

    AtomicBoolean resultRecieved = new AtomicBoolean(false);
    Object timeOutMutex = new Object();

    public MyUniCompleableCall() {
        setTimeOut(500l, TimeUnit.SECONDS, (T)null);
    }

    private void applyResult(T result) {
        if (applyFn != null) {
            applyFn.accept(result);
        } else {
            if (unReportedResultIndex < maxUnReportCache) {
                unReportedResultVector.add(unReportedResultIndex, result);
                unReportedResultIndex++;
            }
        }
    }

    public void complete(T result) {
        synchronized (timeOutMutex) {
            if (timedOut.get())
                return;
            resultRecieved.set(true);
            applyResult(result);
        }
    }

    public void completeExceptionally(Object ex) {
        synchronized (timeOutMutex) {
            if (timedOut.get())
                return;
            resultRecieved.set(true);
            if (exceptionFn != null)
                exceptionFn.accept(ex);
            else {
                if (unReportedExceptionIndex < maxUnReportCache) {
                    unReportedExceptionVector.add(unReportedExceptionIndex, ex);
                    unReportedExceptionIndex++;
                }
            }
        }
    }

    public MyUniCompleableCall<T> thenApply(UniResultFunction<T> fn) {
        if (!unReportedResultVector.isEmpty()) {
            for (int i = 0; i < unReportedResultVector.size(); i++) {
                T result = unReportedResultVector.get(i);
                fn.accept(result);
            }
            unReportedResultVector.clear();
        }
        this.applyFn = fn;
        return this;
    }

    public MyUniCompleableCall<T> exceptionally(UniResultFunction<Object> fn) {
        if (!unReportedExceptionVector.isEmpty()) {
            for (int i = 0; i < unReportedExceptionVector.size(); i++) {
                Object result = unReportedExceptionVector.get(i);
                fn.accept(result);
            }
            unReportedExceptionVector.clear();
        }
        this.exceptionFn = fn;
        return this;
    }

    public void setTimeOut(long ms, TimeUnit timeUnit, VoidResultFunction fn) {
        requestTimeOutManager.scheduleTimerTask(
                new TimerTask() {
                    @Override
                    public void run() {
                        long waitTimeOutInMilliSeconds = TimeUnit.MILLISECONDS.convert(ms, timeUnit);
                        try {
                            Thread.sleep(waitTimeOutInMilliSeconds);
                        } catch (InterruptedException ex) {
                            logger.error("error occured interrupting uniCompletableCall ", ex);
                        }
                        synchronized (timeOutMutex) {
                            timedOut.set(true);
                            if (!resultRecieved.get())
                                fn.apply();
                        }
                    }
                }, ms, timeUnit
        );
    }

    public void setTimeOut(long ms, TimeUnit timeUnit, T defaultValue) {
        requestTimeOutManager.scheduleTimerTask(
                new TimerTask() {
                    @Override
                    public void run() {
                        long waitTimeOutInMilliSeconds = TimeUnit.MILLISECONDS.convert(ms, timeUnit);
                        try {
                            Thread.sleep(waitTimeOutInMilliSeconds);
                        } catch (InterruptedException ex) {
                            logger.error("error occured interrupting uniCompletableCall ", ex);
                        }
                        synchronized (timeOutMutex) {
                            timedOut.set(true);
                            if (!resultRecieved.get())
                                applyResult(defaultValue);
                        }
                    }
                }, ms, timeUnit
        );
    }
}
