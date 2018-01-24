package projectCal;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Test1 {
	
	public static void main(String[] args) throws UnknownHostException, SocketException {
		InetAddress ia = InetAddress.getLocalHost();
		System.out.println(ia);
		// TODO Auto-generated method stub
		//获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		System.out.println("mac数组长度："+mac.length);
		StringBuffer sb = new StringBuffer("");
		for(int i=0; i<mac.length; i++) {
			if(i!=0) {
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i]&0xff;
			String str = Integer.toHexString(temp);
			System.out.println("每8位:"+str);
			if(str.length()==1) {
				sb.append("0"+str);
			}else {
				sb.append(str);
			}
		}
		System.out.println("本机MAC地址:"+sb.toString().toUpperCase());
	}
	
	public void test1() throws ParseException {
		Date c1 = new SimpleDateFormat("yyyy-MM-dd").parse("2017-2-1");
		Date c2 = new SimpleDateFormat("yyyy-MM-dd").parse("2017-3-31");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(getMonthLastDay(c1).getTime()));
		int[] t = get(c1,c2);
		System.out.println(t[0] + ":" + t[1] + ":" + t[2]);
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
	
	public static Calendar getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(1), calendar.get(2) + 1, 1);
		calendar.add(5, -1);
		return calendar;
	}
}
