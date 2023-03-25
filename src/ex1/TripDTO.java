package ex1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TripDTO {
	private final static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yy/MM/dd hh:mm:ss");
	private static int mileageNumber = 1;
	private int mileage;
	private String bno;

	private String country;
	private String ctiy;
	private String airport;
	private String planeclass;
	private String seatposition;

	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String paymenttime;

	public TripDTO() {
		this.mileage = mileageNumber++;
	}
	public TripDTO(String bno, LocalDateTime startTime, LocalDateTime endTime, String country, String ctiy, String airport,
            String planeclass, String seatLocation, String paymenttime) {
        this.bno = bno;
        this.startTime = startTime;
        this.endTime = endTime;
        this.country = country;
        this.ctiy = ctiy;
        this.airport = airport;
        this.planeclass = planeclass;
        this.seatposition = seatLocation;
        this.paymenttime = paymenttime;
    }
	public int getMileage() {
		return mileage;
	}
	public String getBno() {
		return bno;
	}
	public void setBno(String bno) {
		this.bno = bno;
	}
	


	public String getPaymenttime() {
		return paymenttime;
	}

	public void setPaymenttime(LocalDateTime localDateTime) {
		this.paymenttime = DTF.format(localDateTime);

	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCtiy() {
		return ctiy;
	}
	public void setCtiy(String ctiy) {
		this.ctiy = ctiy;
	}	
	public String getAirport() {
		return airport;
	}
	public void setAirport(String airport) {
		this.airport = airport;
	}
	public String getPlaneclass() {
		return planeclass;
	}
	public void setPlaneclass(String planeclass) {
		this.planeclass = planeclass;
	}
	public String getSeatposition() {
		return seatposition;
	}
	public void setSeatposition(String seatposition) {
		this.seatposition = seatposition;
	}
    public LocalDateTime getStartTime() {
	    return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
	    this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
	    return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
	    this.endTime = endTime;
	}	
		

	public String toPurchaseString() {
	
		String str1 = bno + "\t" + startTime + "\t" + endTime + "\t" + country + "\t" + ctiy + "\t" + airport + "\t" +  planeclass + "\t\t" + seatposition +"\t" + paymenttime;
		return str1;	
	}
	
}
