package au.com.suttons.notification.jobs;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseJob
{
    private static final Logger logger = LoggerFactory.getLogger(BaseJob.class);

    @Resource
    protected TimerService timerService;


    @PostConstruct
    protected void construct() {
    	this.cancelTimers();
        this.setupScheduler();
    }

    @Timeout
    protected final void timeout(Timer timer) {
    	logger.info(this.getClass().getSimpleName()+"(JOB) has started.");
        this.run();
    }

    protected final void cancelTimers() {
        for (Timer timer : timerService.getTimers()) {
            timer.cancel();
        }
    }

    abstract protected void setupScheduler(); 

    abstract protected void run();
}
