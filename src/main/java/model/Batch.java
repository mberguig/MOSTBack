package model;

import java.util.Date;
import java.util.HashMap;


public class Batch {
	String treatmentId;
	String treatmentName;
	Date startTime;
	Date endTime;
	long duration;
	String durationTxt;
	String status;
	boolean referenced;
	public HashMap<String, TreatmentUnit> treatmentUnits = new HashMap<String, TreatmentUnit>();
	
	public Batch(String treatmentId, String treatmentName, Date startTime, Date endTime, long duration, String durationTxt, String status, boolean referenced, HashMap<String, TreatmentUnit> treatmentUnits){
		
		this.treatmentId = treatmentId;
		this.treatmentName = treatmentName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.status = status;
		this.referenced = referenced;
		this.treatmentUnits = treatmentUnits;
		this.durationTxt = durationTxt;
	}

	public String getDurationTxt() {
		return durationTxt;
	}

	public void setDurationTxt(String durationTxt) {
		this.durationTxt = durationTxt;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Batch() {
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, TreatmentUnit> addTreatmentUnit(String id, TreatmentUnit treatmentUnit){
		treatmentUnits.put(id, treatmentUnit);
		return treatmentUnits;
	}
	
	public String getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(String id) {
		this.treatmentId = id;
	}

	public String getTreatmentName() {
		return treatmentName;
	}

	public void setTreatmentName(String name) {
		this.treatmentName = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isReferenced() {
		return referenced;
	}

	public void setReferenced(boolean referenced) {
		this.referenced = referenced;
	}

	public HashMap<String, TreatmentUnit> getTreatmentUnits() {
		return treatmentUnits;
	}

	public void setTreatmentUnits(HashMap<String, TreatmentUnit> treatmentUnits) {
		this.treatmentUnits = treatmentUnits;
	}
	
	public String toString(){
		String dataBatch = "id : " + this.treatmentId + " name : " + this.treatmentName + " startTime : " + this.startTime + " endTime : " + this.endTime + " duration : " + this.duration + " status : " + this.status + " referenced : " + this.referenced;
		String dataUnit = "";
		dataBatch = dataBatch + " " + dataUnit;
		return dataBatch;
	}

	public long getDuration() {
		return duration;
	}



}
