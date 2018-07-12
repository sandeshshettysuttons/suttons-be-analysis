package au.com.suttons.notification.jobs.util;

import javax.ejb.ScheduleExpression;

public class CronUtil
{
    public static ScheduleExpression createScheduleExpressionFromString(String cronStr)
    {
        String[] cronParts = cronStr.split(" ");

        if (cronParts.length < 6) {
            throw new IllegalArgumentException(cronStr);
        }

        String secondExp      = cronParts[0];
        String minuteExp      = cronParts[1];
        String hourExp        = cronParts[2];
        String dayOfMonthExp  = cronParts[3];
        String monthExp       = cronParts[4];
        String dayOfWeekExp   = cronParts[5];
        
        return new ScheduleExpression()
                .second(secondExp)
                .minute(minuteExp)
                .hour(hourExp)
                .dayOfMonth(dayOfMonthExp)
                .month(monthExp)
                .dayOfWeek(dayOfWeekExp);
    }
}
