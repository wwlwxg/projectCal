import java.math.BigDecimal;
import java.util.Date;

public class LaoHuanheExcelBean {
	private int index;
	private BigDecimal amount;
	private Date beginTime;
	private Date endTime;
	
	private BigDecimal interest = new BigDecimal("0");
	
	private int monthsBackup;	// 对于分段的计算，直接填写时间长度月数
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getMonthsBackup() {
		return monthsBackup;
	}
	public void setMonthsBackup(int monthsBackup) {
		this.monthsBackup = monthsBackup;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	@Override
	public String toString() {
		return "LaoHuanheExcelBean [index=" + index + ", amount=" + amount + ", beginTime=" + beginTime + ", endTime="
				+ endTime + ", interest=" + interest + ", monthsBackup=" + monthsBackup + "]";
	}
}
