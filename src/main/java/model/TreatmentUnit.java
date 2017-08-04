package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TreatmentUnit {

	String unitId;
	String unitName;
	String treatmentId;
	String treatmentName;
	Date startTime;
	Date endTime;
	long duration;
	String durationTxt;
	String status;
	int treatSuccessCount;
	int treatErrCount;
	int treatFailedCount;
	Boolean referenced = false;
	List<Step> steps = new ArrayList<Step>();
	
	public TreatmentUnit() {

	}

	public TreatmentUnit(String unitId, String unitName, String treatmentId,String treatmentName, Date startTime, Date endTime, long duration, String durationTxt, int stepOrder, String status, int treatSuccessCount, int treatErrCount, int treatFailedCount, List<Step> steps) {
		
		this.unitId = unitId;
		this.unitName = unitName;
		this.treatmentId = treatmentId;
		this.treatmentName = treatmentName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.durationTxt = durationTxt;
		this.status = status;
		this.treatSuccessCount = treatSuccessCount;
		this.treatErrCount = treatErrCount;
		this.treatFailedCount= treatFailedCount;
		this.steps = steps;
	}
	
	public String getDurationTxt() {
		return durationTxt;
	}

	public void setDurationTxt(String durationTxt) {
		this.durationTxt = durationTxt;
	}

	public int getTreatSuccessCount() {
		return treatSuccessCount;
	}

	public void setTreatSuccessCount(int treatSuccessCount) {
		this.treatSuccessCount = treatSuccessCount;
	}

	public int getTreatErrCount() {
		return treatErrCount;
	}

	public void setTreatErrCount(int treatErrCount) {
		this.treatErrCount = treatErrCount;
	}

	public int getTreatFailedCount() {
		return treatFailedCount;
	}

	public void setTreatFailedCount(int treatFailedCount) {
		this.treatFailedCount = treatFailedCount;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getTreatmentName() {
		return treatmentName;
	}

	public void setTreatmentName(String treatmentName) {
		this.treatmentName = treatmentName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(String treatmentId) {
		this.treatmentId = treatmentId;
	}



	public List<Step> addStep(Step step){
		steps.add(step);
		return steps;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setStartTimeLong(long startTime) {
		this.startTime = new Date(startTime);
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setEndTimeLong(long endTime) {
		this.endTime = new Date(endTime);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getDuration() {
		return duration;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	
	@Override
	public String toString(){
		return "unitId : " + this.unitId + " unitName : " + this.unitName + " treatmentId : " + this.treatmentId + " treatmentName : " + this.treatmentName + " startTime : " + this.startTime + " referenced : " + this.referenced;
				
	}

	public Boolean getReferenced() {
		return referenced;
	}

	public void setReferenced(Boolean referenced) {
		this.referenced = referenced;
	}


}
