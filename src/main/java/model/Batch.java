package model;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Batch {
	String id;
	String name;
	Date startTime;
	Date endTime;
	long duration;
	String status;
	boolean referenced;
	public List<TreatmentUnit> treatmentUnits = new ArrayList<TreatmentUnit>();
	
	public Batch(String id, String name, Date startTime, Date endTime, long duration, String status, boolean referenced, List<TreatmentUnit> treatmentUnits){
		
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.status = status;
		this.referenced = referenced;
		this.treatmentUnits = treatmentUnits;
	}

	public Batch() {
		// TODO Auto-generated constructor stub
	}

	public List<TreatmentUnit> addTreatmentUnit(TreatmentUnit treatmentUnit){
		treatmentUnits.add(treatmentUnit);
		return treatmentUnits;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
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

	public List<TreatmentUnit> getTreatmentUnits() {
		return treatmentUnits;
	}

	public void setTreatmentUnits(List<TreatmentUnit> treatmentUnits) {
		this.treatmentUnits = treatmentUnits;
	}
	


	public void setTreatmentUnits(int index, TreatmentUnit treatmentUnits) {
		this.treatmentUnits.set(index, treatmentUnits);
	}
	

	public String toString(){
		String dataBatch = "id : " + this.id + " name : " + this.name + " startTime : " + this.startTime + " endTime : " + this.endTime + " duration : " + this.duration + " status : " + this.status + " referenced : " + this.referenced;
		String dataUnit = "";
		if(this.treatmentUnits != null){
		for (TreatmentUnit unit : this.treatmentUnits){
			if(unit != null){
				dataUnit += unit.toString();
			}
		}
		}
		dataBatch = dataBatch + " " + dataUnit;
		return dataBatch;
	}
	
	
}
