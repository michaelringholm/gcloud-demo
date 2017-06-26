package com.stelinno.demo.gcp;
import java.util.Date;

import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.Property;

@Entity(kind = "parcel")
public class ParcelCatatumbo {

	public ParcelCatatumbo() {
		
	}
	
    public ParcelCatatumbo(String trackAndTraceNo, Date receivedDate) {
		this.trackAndTraceNo = trackAndTraceNo;
		this.receivedDate = receivedDate;
	}

	@Identifier
    private long id;

    @Property(name = "trackAndTraceNo")
    private String trackAndTraceNo;

    @Property(name = "receivedDate")
    private Date receivedDate;

	public String getTrackAndTraceNo() {
		return trackAndTraceNo;
	}

	public void setTrackAndTraceNo(String trackAndTraceNo) {
		this.trackAndTraceNo = trackAndTraceNo;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

}