package com.stelinno.demo.gcp;

//import javax.persistence.Entity;
//import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Parcel {
    public Parcel(String trackAndTraceNo) {
		this.trackAndTraceNo = trackAndTraceNo;
	}
    
	@Id //id fields can be long, Long or String. Only Long automatically generates keys when it s null
    public Long id;
    @Index
    public String trackAndTraceNo;
}