package com.fanap.sepandar.utils.sharedClasses.myCompleteableCall.timer;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin123 on 2/28/2017.
 */
public interface IRequestTimeOutManager {
    public void scheduleTimerTask(TimerTask timerTask, long delayTime, TimeUnit timeUnit);
}
