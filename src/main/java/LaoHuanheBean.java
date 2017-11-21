import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * 老环河计算bean
 * @author jecky
 *
 */
public class LaoHuanheBean {
	private int index;
	private BigDecimal amount;
	private Date beginTime;
	private Date endTime;

	private BigDecimal rate;
	private BigDecimal rateRise;
	private int[] dayRate;
	private BigDecimal interest;
	
	
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
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getRateRise() {
		return rateRise;
	}
	public void setRateRise(BigDecimal rateRise) {
		this.rateRise = rateRise;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public int[] getDayRate() {
		return dayRate;
	}
	public void setDayRate(int[] dayRate) {
		this.dayRate = dayRate;
	}
	@Override
	public String toString() {
		return "LaoHuanheBean [index=" + index 
				+ ", amount=" + amount 
				+ ", beginTime=" + DateUtils.getDateString(beginTime) 
				+ ", endTime=" + DateUtils.getDateString(endTime) 
				+ ", rate=" + rate 
				+ ", rateRise=" + rateRise 
				+ ", dayRate=" + Arrays.toString(dayRate)
				+ ", interest=" + interest + "]";
	}
}
