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
	String status;
	int treatSuccessCount;
	int treatErrCount;
	int treatFailedCount;
	List<Step> steps = new ArrayList<Step>();
	
	public TreatmentUnit(String unitId, String unitName, String treatmentId,String treatmentName, Date startTime, Date endTime, long duration, int stepOrder, String status, int treatSuccessCount, int treatErrCount, int treatFailedCount, List<Step> steps) {
		
		this.unitId = unitId;
		this.unitName = unitName;
		this.treatmentId = treatmentId;
		this.treatmentName = treatmentName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.status = status;
		this.treatSuccessCount = treatSuccessCount;
		this.treatErrCount = treatErrCount;
		this.treatFailedCount= treatFailedCount;
		this.steps = steps;
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

	public int gettreatSuccessCount() {
		return treatSuccessCount;
	}

	public void settreatSuccessCount(int treatSuccessCount) {
		this.treatSuccessCount = treatSuccessCount;
	}

	public int gettreatErrCount() {
		return treatErrCount;
	}

	public void settreatErrCount(int treatErrCount) {
		this.treatErrCount = treatErrCount;
	}

	public int gettreatFailedCount() {
		return treatFailedCount;
	}

	public void settreatFailedCount(int treatFailedCount) {
		this.treatFailedCount = treatFailedCount;
	}

	public TreatmentUnit() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getName() {
		return unitName;
	}

	public void setName(String name) {
		this.unitName = name;
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

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	
	public String toString(){
		return "unitId : " + this.unitId + " unitName : " + this.unitName + " treatmentId : " + this.treatmentId + " treatmentName : " + this.treatmentName + " startTime : " + this.startTime;
				
	}
	
	
}
