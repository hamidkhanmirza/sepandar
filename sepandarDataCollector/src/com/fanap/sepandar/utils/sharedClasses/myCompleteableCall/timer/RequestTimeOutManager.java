package com.fanap.sepandar.utils.sharedClasses.myCompleteableCall.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin123 on 2/28/2017.
 */
public class RequestTimeOutManager implements IRequestTimeOutManager {
    Timer timer = new Timer();

    @Override
    public void scheduleTimerTask(TimerTask timerTask, long delayTime, TimeUnit timeUnit) {
        long delayTimeInMilliSeconds = TimeUnit.MILLISECONDS.convert(delayTime, timeUnit);
        timer.schedule(timerTask, delayTimeInMilliSeconds);
    }
}
