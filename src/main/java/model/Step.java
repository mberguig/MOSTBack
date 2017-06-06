package model;


import java.util.Date;


public class Step {

	public String stepExecutionId;
	public String stepName;
	public String unitId;
	public String unitName;
	public Date startTime;
	public Date endTime;
	public long totalDuration;
	public long averageDuration;
	public int stepOrder;
	public String status;
	public int treatSuccessCount;
	public int treatErrCount;
	public int treatFailedCount;
	public int nbExecution = 1;
	
	
	public Step(){
	}


	public Step(String id, String stepName, Date startTime, Date endTime, long totalDuration, long averageDuration, int stepOrder, String status, int treatSuccessCount, int treatErrCount, int treatFailedCount, String unitId, String unitName) {
		this.stepExecutionId = id;
		this.stepName = stepName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalDuration = totalDuration;
		this.averageDuration = averageDuration;
		this.stepOrder = stepOrder;
		this.status = status;
		this.treatSuccessCount = treatSuccessCount;
		this.treatErrCount = treatErrCount;
		this.treatFailedCount= treatFailedCount;
		this.unitId = unitId;
		this.unitName = unitName;
	}
	
	
	
	public long getAverageDuration() {
		return averageDuration;
	}


	public void setAverageDuration(long averageDuration) {
		this.averageDuration = averageDuration;
	}


	public long getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
	}

	public void addTotalDuration(long totalDuration){
		this.totalDuration += totalDuration;
	}

	public String getStepExecutionId() {
		return stepExecutionId;
	}

	public void setId(String StepExecutionId) {
		this.stepExecutionId = StepExecutionId;
	}

	public String getstepName() {
		return stepName;
	}

	public void setstepName(String stepName) {
		this.stepName = stepName;
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

	public int getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(int stepOrder) {
		this.stepOrder = stepOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTreatSuccessCount() {
		return treatSuccessCount;
	}

	public void setTreatSuccessCount(int treatSuccessCount) {
		this.treatSuccessCount = treatSuccessCount;
	}
	public void addTreatSuccessCount(int treatSuccessCount) {
		this.treatSuccessCount += treatSuccessCount;
	}

	public int getTreatErrCount() {
		return treatErrCount;
	}

	public void setTreatErrCount(int treatErrCount) {
		this.treatErrCount = treatErrCount;
	}
	public void addTreatErrCount(int treatErrCount) {
		this.treatErrCount += treatErrCount;
	}
	

	public int getTreatFailedCount() {
		return treatFailedCount;
	}

	public void setTreatFailedCount(int treatFailedCount) {
		this.treatFailedCount = treatFailedCount;
	}
	public void addTreatFailedCount(int treatFailedCount) {
		this.treatFailedCount += treatFailedCount;
	}
	public int getNbExecution() {
		return nbExecution;
	}


	public void setNbExecution(int nbExecution) {
		this.nbExecution = nbExecution;
	}
	public void addNbExecution(int nbExecution) {
		this.nbExecution += nbExecution;
	}

}
