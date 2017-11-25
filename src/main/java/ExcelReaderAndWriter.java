

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReaderAndWriter {
	
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<BankRateExcelBean> bankRateList = new ArrayList<>();
	private List<LaoHuanheExcelBean> laoHuanheExcelList = new ArrayList<>();
    
	public void init(String filePath) throws IOException {
		//InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
		InputStream is = new FileInputStream(new File(filePath));
		XSSFWorkbook wb = new XSSFWorkbook(is);
		Sheet dataSheet = wb.getSheetAt(0);
		initLaoHuanheBeanList(dataSheet);
		Sheet configSheet = wb.getSheetAt(1); // 配置表
		initBankRateBeanList(configSheet);
		
	}
	
	public void output(String templatePath, String descPath, List<LaoHuanheBean> result) throws IOException {
		InputStream is = new FileInputStream(new File(templatePath));
		XSSFWorkbook wb = new XSSFWorkbook(is);
		outputSum(wb, result);
		outputDetail(wb, result);
		
		
		FileOutputStream os = new FileOutputStream(descPath);
        try {
            wb.write(os);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
        	if(os !=null) {
        		os.close();
        	}
        	if(wb!=null){
        		wb.close();
        	}
        }
	}
	
	private void outputSum(XSSFWorkbook wb, List<LaoHuanheBean> result) {
		Sheet sheet = wb.getSheetAt(0);
		for(LaoHuanheExcelBean bean : laoHuanheExcelList) {
			int index = bean.getIndex();
			logger.info("-----------------------------------------");
			for(LaoHuanheBean lbean : result) {
				if(lbean.getIndex() == index) {
					logger.info("bean interest = " + bean.getInterest()
								+ " , lbean interest = " + lbean.getInterest());
					bean.setInterest(bean.getInterest().add(lbean.getInterest().setScale(4, BigDecimal.ROUND_HALF_UP)));
				}
			}
			logger.info("-----------------------------------------");
			//bean.getInterest().setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		
		// 写excel
		int i = 0;
		double sum = 0.0;
		for(; i < laoHuanheExcelList.size(); i++) {
			LaoHuanheExcelBean bean = laoHuanheExcelList.get(i);
			Row row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(bean.getIndex());
			row.createCell(1).setCellValue(bean.getAmount().setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			row.createCell(2).setCellValue(DateUtils.getDateString(bean.getBeginTime()));
			row.createCell(3).setCellValue(DateUtils.getDateString(bean.getEndTime()));
			row.createCell(4).setCellValue(bean.getMonthsBackup());
			row.createCell(5).setCellValue(bean.getInterest().setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			sum += bean.getInterest().setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		Row rowSum = sheet.createRow(i+1);
		Cell zongjiText = rowSum.createCell(0);
		zongjiText.setCellValue("合计");
		Cell cellSum = rowSum.createCell(5);
		cellSum.setCellStyle(getSumCellStyle(wb));
		cellSum.setCellValue(sum);
		
	}
	
	private void outputDetail(XSSFWorkbook wb, List<LaoHuanheBean> result) {
		Sheet sheet = wb.getSheetAt(1);
		int i = 0;
		double sum = 0.0;
		for(; i < result.size(); i++) {
			LaoHuanheBean bean = result.get(i);
			Row row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(i+1);
			row.createCell(1).setCellValue(bean.getIndex());
			row.createCell(2).setCellValue(bean.getAmount().setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
			row.createCell(3).setCellValue(bean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			row.createCell(4).setCellValue(bean.getRateRise().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			row.createCell(5).setCellValue(DateUtils.getDateString(bean.getBeginTime()));
			row.createCell(6).setCellValue(DateUtils.getDateString(bean.getEndTime()));
			row.createCell(7).setCellValue(Arrays.toString(bean.getDayRate()));
			row.createCell(8).setCellValue(bean.getInterest().setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			sum += bean.getInterest().setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		Row rowSum = sheet.createRow(i+1);
		Cell zongjiText = rowSum.createCell(0);
		zongjiText.setCellValue("合计");
		Cell cellSum = rowSum.createCell(8);
		cellSum.setCellStyle(getSumCellStyle(wb));
		cellSum.setCellValue(sum);

	}
	
	/**
	 * 需要在init方法之后调用
	 * @param list
	 * @return
	 */
	public List<LaoHuanheBean> getResult() {
		List<LaoHuanheBean> result = new LinkedList<>();
		for(LaoHuanheExcelBean bean : laoHuanheExcelList) {
			result.addAll(getLaoHuanheBeans(bean));
		}
		return result;
	}
	
	private List<LaoHuanheBean> getLaoHuanheBeans(LaoHuanheExcelBean bean) {
		// excel上的起始，结束时间
		Calendar bCalendar = Calendar.getInstance();
		bCalendar.setTime(bean.getBeginTime());
		Calendar eCalendar = Calendar.getInstance();
		eCalendar.setTime(bean.getEndTime());
		// 计算迭代，每次会去更改LaoHuanheExcelBean的时间
		List<LaoHuanheBean> result = new ArrayList<>();
		for(int i = 0; i < bankRateList.size(); i++) {
			BankRateExcelBean bankBean = bankRateList.get(i);
			Date bankDate = bankBean.getDate();
			if(eCalendar.getTime().getTime() >= bankBean.getDate().getTime()
				&& eCalendar.getTime().getTime() >= bCalendar.getTime().getTime()) {
				LaoHuanheBean b = new LaoHuanheBean();
				b.setIndex(bean.getIndex());
				b.setAmount(bean.getAmount());
				if(bankDate.getTime() > bCalendar.getTime().getTime()) {
					b.setBeginTime(bankDate);
				} else {
					b.setBeginTime(bCalendar.getTime());
				}
				b.setEndTime(eCalendar.getTime());
				if(bean.getMonthsBackup() > 0) {		// 如果月份数填写了，并且大于0，则时间使用此值
					b.setRate(getRateByMonthLength(bankBean, bean.getMonthsBackup()));
				} else {
					b.setRate(getRate(bankBean,bean.getBeginTime(), bean.getEndTime()));
				}
				b.setRateRise(bankBean.getRateRise());
				int[] arr = DateUtils.getInstance().get(b.getBeginTime(), b.getEndTime());
				b.setDayRate(arr);
				double d = (arr[0] + arr[2]) * 1.0 /360 + arr[1] * 1.0 / 12;	// 计算天数比例
				b.setInterest(b.getAmount()
								.multiply(new BigDecimal(d+""))
								.multiply(b.getRate())
								.multiply(b.getRateRise().add(new BigDecimal("1")))
								.divide(new BigDecimal("100")));
				result.add(b);
				logger.info(b.toString());
				eCalendar.setTime(bankDate);
				eCalendar.add(Calendar.DATE, -1);
			}
//			if(endTime.getTime() == bankDate.getTime()) {
//				LaoHuanheBean b = new LaoHuanheBean();
//				b.setAmount(bean.getAmount());
//				b.setBeginTime(endTime);
//				b.setEndTime(endTime);
//				//b.setDayRate(bankBean.get);
//				//b.setInterest(interest);
//				BigDecimal rate = getRate(bankBean,bCalendar.getTime(), eCalendar.getTime());
//				b.setRate(rate);
//				b.setRateRise(bankBean.getRateRise());
//				result.add(b);
//			} else
//			if(bankDate.getTime() > beginTime.getTime()
//				&& bankDate.getTime() < endTime.getTime()) {
//				LaoHuanheBean b = new LaoHuanheBean();
//				b.setAmount(bean.getAmount());
//				b.setBeginTime(beginTime);
//				b.setDayRate(dayRate);
//				b.setEndTime(endTime);
//				b.setInterest(interest);
//				b.setRate(rate);
//				b.setRateRise(rateRise);
//			}
		}
		return result;
	}
	
	private BigDecimal getRate(BankRateExcelBean bankBean, Date bDate, Date eDate) {
		int[] arr = DateUtils.getInstance().get(bDate, eDate);
		logger.info("dateRate : " + Arrays.toString(arr));
		int beginMonthDays = DateUtils.getInstance().getMonthLastDay(bDate).get(Calendar.DATE);
		int months = arr[1];
		if(arr[0] + arr[2] >= beginMonthDays) {
			months += 1;
		}
		logger.info("months : " + months);
		return getRateByMonthLength(bankBean, months);
	}
	
	private BigDecimal getRateByMonthLength(BankRateExcelBean bankBean, int months) {
		BigDecimal result = null;
		if(months >= 5 * 12) { // 大于等于5年档
			result = bankBean.getRate60plus();
		} else if(months >= 3 * 12) { // 3年到5年档
			result = bankBean.getRate60();
		} else if(months >= 1 * 12) {// 1年到三年档
			result = bankBean.getRate36();
		} else if(months >= 6) { // 6个月到1年档
			result = bankBean.getRate12();
		} else if(months < 6) { // 6个月以内档
			result = bankBean.getRate6();
		}
		logger.info(result.doubleValue()+"");
		return result;
	}
	
	private void initLaoHuanheBeanList(Sheet dataSheet) {
		logger.info("----------- begin initLaoHuanheBeanList ----" );
		int rowNum = dataSheet.getPhysicalNumberOfRows();
		for(int i = 1; i < rowNum; i++) {
			logger.info("i = " + i + ", rowNum : " + rowNum);
			Row row = dataSheet.getRow(i);
			Cell indexCell = row.getCell(0);
			Cell amountCell = row.getCell(1);
			Cell beginTimeCell = row.getCell(2);
			Cell endTimeCell = row.getCell(3);
			Cell monthBacksCell = row.getCell(4);
			if(beginTimeCell.getCellTypeEnum() == CellType.BLANK) {
				break;
			}
			int index = (int) indexCell.getNumericCellValue();
			BigDecimal amount = new BigDecimal(amountCell.getNumericCellValue());
			logger.info("amount:" + amount.doubleValue()+"");
			Date beginTime = null;
			if(beginTimeCell.getCellTypeEnum() == CellType.NUMERIC) {
				beginTime = beginTimeCell.getDateCellValue();
			} else if(beginTimeCell.getCellTypeEnum() == CellType.STRING) {
				try {
					beginTime = new SimpleDateFormat("yyyy/MM/dd").parse(beginTimeCell.getStringCellValue());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			logger.info("beginDate:" + DateUtils.getDateString(beginTime));
			Date endTime = null;
			if(endTimeCell.getCellTypeEnum() == CellType.NUMERIC) {
				endTime = endTimeCell.getDateCellValue();
			} else if(endTimeCell.getCellTypeEnum() == CellType.STRING) {
				try {
					endTime = new SimpleDateFormat("yyyy/MM/dd").parse(endTimeCell.getStringCellValue());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			logger.info("endTime:" + DateUtils.getDateString(endTime));
			LaoHuanheExcelBean bean = new LaoHuanheExcelBean();
			bean.setAmount(amount);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setIndex(index);
			if(monthBacksCell != null && monthBacksCell.getNumericCellValue() > 0) {
				int monthBacks = (int) monthBacksCell.getNumericCellValue();
				logger.info("monthBacks:" + monthBacks);
				bean.setMonthsBackup(monthBacks);
			}
			logger.info(bean.toString());
			laoHuanheExcelList.add(bean);
		}
		logger.info("----------- end initLaoHuanheBeanList ----" );
	}
	
	private void initBankRateBeanList(Sheet configSheet) {
		logger.info("----------- begin initBankRateBeanList ----" );
		int rowNum = configSheet.getPhysicalNumberOfRows();
		for(int i = 1; i < rowNum; i++) {
			logger.info("i = " + i + ", rowNum : " + rowNum);
			Row row = configSheet.getRow(i);
			Date date = row.getCell(0).getDateCellValue();
			BigDecimal rate6 = new BigDecimal(row.getCell(1).getNumericCellValue());
			BigDecimal rate12 = new BigDecimal(row.getCell(2).getNumericCellValue());
			BigDecimal rate36 = new BigDecimal(row.getCell(3).getNumericCellValue());
			BigDecimal rate60 = new BigDecimal(row.getCell(4).getNumericCellValue());
			BigDecimal rate60plus = new BigDecimal(row.getCell(5).getNumericCellValue());
			BigDecimal rateRise = new BigDecimal(row.getCell(6).getNumericCellValue());
			BankRateExcelBean bean = new BankRateExcelBean();
			bean.setDate(date);
			bean.setRate6(rate6);
			bean.setRate12(rate12);
			bean.setRate36(rate36);
			bean.setRate60(rate60);
			bean.setRate60plus(rate60plus);
			bean.setRateRise(rateRise);
			logger.info(bean.toString());
			bankRateList.add(bean);
		}
		logger.info("----------- end initBankRateBeanList ----" );
	}
/**
    public void write(String filePath,String descPath,List<FileBean> datas) throws IOException{
        InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
        HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFSheet sheet = wb.getSheetAt(0);
        for(int i = 0; i <  datas.size(); i++){
        	Row row = sheet.createRow(i+2);
        	Cell cell0 = row.createCell(0);
        	cell0.setCellStyle(getCellStyle(wb));
        	cell0.setCellValue(datas.get(i).getMaster());
        	Cell cell1 = row.createCell(1);
        	cell1.setCellStyle(getCellStyle(wb));
        	cell1.setCellValue(datas.get(i).getCount());
        	Cell cell2 = row.createCell(2);
        	cell2.setCellStyle(getCellStyle(wb));
        	cell2.setCellValue(datas.get(i).getGroup());
        	Cell cell3 = row.createCell(3);
        	cell3.setCellStyle(getCellStyle(wb));
        	cell3.setCellValue(datas.get(i).getFamilyType());
        	Cell cell4 = row.createCell(4);
        	cell4.setCellStyle(getCellStyle(wb));
        	cell4.setCellValue(datas.get(i).getCommunity());
        	Cell cell5 = row.createCell(5);
        	cell5.setCellStyle(getCellStyle(wb));
        	cell5.setCellValue(datas.get(i).getRemarks());
        }
        Cell cell0 = sheet.getRow(0).getCell(2);
        cell0.setCellType(CellType.FORMULA);
        cell0.setCellFormula("C3");
//    	File desktopDir = FileSystemView.getFileSystemView()
//    			.getHomeDirectory();
        FileOutputStream os = new FileOutputStream(descPath+"/tt.xls");
        try {
            wb.write(os);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
        	if(os !=null) {
        		os.close();
        	}
        	if(wb!=null){
        		wb.close();
        	}
        }
        
    }
	
    private CellStyle getCellStyle(Workbook wb){
    	CellStyle style = wb.createCellStyle();
    	style.setBorderBottom(BorderStyle.THIN);   
    	style.setBorderTop(BorderStyle.THIN);   
    	style.setBorderLeft(BorderStyle.THIN);   
    	style.setBorderRight(BorderStyle.THIN);
    	
    	style.setAlignment(HorizontalAlignment.CENTER);
    	style.setVerticalAlignment(VerticalAlignment.CENTER); 
    	
    	style.setWrapText(true);
    	
    	return style;
    }
    */
	
    private CellStyle getSumCellStyle(Workbook wb){
    	CellStyle style = wb.createCellStyle();
    	Font font = wb.createFont();
    	font.setBold(true);
    	style.setFont(font);
    	return style;
    }

}
