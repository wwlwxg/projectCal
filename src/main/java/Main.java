

import java.io.IOException;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Main {
	public static void main(String[] args) throws IOException {
		String root = System.getProperty("user.dir");
		System.out.println("sssss : " + root+"/111/数据.xlsx");
		if(!tt("4A-E2-44-F6-B6-19")){
			return;
		}
		ExcelReaderAndWriter rw = new ExcelReaderAndWriter();
		rw.init(root+"/111/数据.xlsx");
		System.out.println("xxxx----->");
		System.out.println(rw.getResult());
		rw.output(root+"/111/template.xlsx", root+"/output.xlsx", rw.getResult());
		System.out.println("<-----xxxx");
		
	}
	
	public static boolean tt(String macString) {
		boolean result = false;
		try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                StringBuffer stringBuffer = new StringBuffer();
                NetworkInterface networkInterface = enumeration.nextElement();
                if (networkInterface != null) {
                    byte[] bytes = networkInterface.getHardwareAddress();
                    if (bytes != null) {
                        for (int i = 0; i < bytes.length; i++) {
                            if (i != 0) {
                                stringBuffer.append("-");
                            }
                            int tmp = bytes[i] & 0xff; // 字节转换为整数
                            String str = Integer.toHexString(tmp);
                            if (str.length() == 1) {
                                stringBuffer.append("0" + str);
                            } else {
                                stringBuffer.append(str);
                            }
                        }
                        String mac = stringBuffer.toString().toUpperCase();  
                        if(mac.equals(macString)) {
                        	result = true;
                        	break;
                        }
                        System.out.println(mac);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
		return result;
	}
}
