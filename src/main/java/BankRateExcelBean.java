import java.math.BigDecimal;
import java.util.Date;

public class BankRateExcelBean {
	
	private Date date;
	private BigDecimal rate6;
	private BigDecimal rate12;
	private BigDecimal rate36;
	private BigDecimal rate60;
	private BigDecimal rate60plus;
	private BigDecimal rateRise;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getRate6() {
		return rate6;
	}

	public void setRate6(BigDecimal rate6) {
		this.rate6 = rate6;
	}

	public BigDecimal getRate12() {
		return rate12;
	}

	public void setRate12(BigDecimal rate12) {
		this.rate12 = rate12;
	}

	public BigDecimal getRate36() {
		return rate36;
	}

	public void setRate36(BigDecimal rate36) {
		this.rate36 = rate36;
	}

	public BigDecimal getRate60() {
		return rate60;
	}

	public void setRate60(BigDecimal rate60) {
		this.rate60 = rate60;
	}

	public BigDecimal getRate60plus() {
		return rate60plus;
	}

	public void setRate60plus(BigDecimal rate60plus) {
		this.rate60plus = rate60plus;
	}
	public BigDecimal getRateRise() {
		return rateRise;
	}

	public void setRateRise(BigDecimal rateRise) {
		this.rateRise = rateRise;
	}

	@Override
	public String toString() {
		return "BankRateExcelBean [date=" + DateUtils.getDateString(date)
				+ ", rate6=" + rate6 
				+ ", rate12=" + rate12 
				+ ", rate36=" + rate36
				+ ", rate60=" + rate60 
				+ ", rate60plus=" + rate60plus 
				+ ", rateRise=" + rateRise + "]";
	}

}
