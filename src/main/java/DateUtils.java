import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	private static final DateUtils instance = new DateUtils();
	
	public static final DateUtils getInstance() {
		 return instance;
	}
	
	public static String getDateString(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	public int[] get(Date beginTime, Date endTime) {
		int[] result = {0,0,0};
		Calendar c1 = Calendar.getInstance();
		c1.setTime(beginTime);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(endTime);
		
		// 开始月份到月最后一天的天数
		int beginMonthDays = getMonthLastDay(c1.getTime()).get(Calendar.DATE);
		int beginYear = c1.get(Calendar.YEAR);
		int beginMonth = c1.get(Calendar.MONTH);
		int beginDay = c1.get(Calendar.DATE);
		
		int endMonthDays = getMonthLastDay(c2.getTime()).get(Calendar.DATE);
		int endYear = c2.get(Calendar.YEAR);
		int endMonth = c2.get(Calendar.MONTH);
		int endDay = c2.get(Calendar.DATE);
		
		int months = (endYear - beginYear) * 12 + endMonth - beginMonth + 1;
		
		if(beginDay == 1 && endDay == endMonthDays) {
			result[1] = months;
		} else if(months == 1) {
			result[0] = endDay - beginDay + 1;
		} else if(months > 1) {
			if(beginDay == 1) {
				result[2] = endDay;
				result[1] = months - 1;
			} else if(endDay == endMonthDays) {
				result[0] = beginMonthDays - beginDay + 1;
				result[1] = months - 1;
			} else {
				result[0] = beginMonthDays - beginDay + 1;
				result[1] = months - 2;
				result[2] = endDay;
			}
		}
		
		return result;
	}
	
	/**
	 * 获取本月的最后一天
	 * @param date
	 * @return
	 */
	public Calendar getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(1), calendar.get(2) + 1, 1);
		calendar.add(5, -1);
		return calendar;
	}
    
}
