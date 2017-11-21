

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		String root = System.getProperty("user.dir");
		System.out.println("sssss : " + root+"/111/数据.xlsx");

		ExcelReaderAndWriter rw = new ExcelReaderAndWriter();
		rw.init(root+"/111/数据.xlsx");
		System.out.println("xxxx----->");
		System.out.println(rw.getResult());
		rw.output(root+"/111/template.xlsx", root+"/output.xlsx", rw.getResult());
		System.out.println("<-----xxxx");
		
	}
	
	
}
