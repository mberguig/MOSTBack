package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Batch;
import model.Step;
import model.TreatmentUnit;


public class GetSteps {

	public GetSteps(){
		super();
	}

	public static List<Step> Request() throws IOException  {
		HashMap<String, Step> stepsStarted = ElasticRequests.getStartedSteps();
		HashMap<String, Step> stepsFinished = ElasticRequests.getFinishedSteps();
		List<Step> stepsList = new ArrayList<Step>();

		for (String stepId : stepsStarted.keySet()) {
			if(stepsFinished.containsKey(stepId)){
				Step step = stepsStarted.get(stepId);
				Step stepEnd = stepsFinished.get(stepId);
				step = setDataToStep(step, stepEnd);
				stepsList.add(step);
			}else {
				System.out.println(stepId + " non terminé");
				stepsList.add(stepsStarted.get(stepId));
			}
		}

		return stepsList;
	}

	public static HashMap<String, Step> groupByStepName(List<Step> stepsList) throws IOException {
		HashMap<String, Step> stepsByName = new HashMap<String, Step>();

		for (Step step : stepsList) {			
			if(stepsByName.containsKey(step.getstepName())){ //si l'étape existe déjà				
				Step stepTmp = stepsByName.get(step.getstepName());
				updateStep(step, stepTmp);
				recalculateTimes(step, stepTmp);
			}else {
				stepsByName.put(step.getstepName(), step);	
			}
		}	

		return stepsByName;
	}

	public static Step setDataToStep(Step step, Step stepEnd){
		step.setStatus(stepEnd.getStatus());
		step.setEndTime(stepEnd.getEndTime());
		step.setTreatSuccessCount(stepEnd.getTreatSuccessCount());
		step.setTreatFailedCount(stepEnd.getTreatFailedCount());
		step.setTreatErrCount(stepEnd.getTreatErrCount());
		step.setTotalDuration(step.getEndTime().getTime()-step.getStartTime().getTime());
		return step;
	}

	public static Step updateStep(Step step, Step stepTmp){
		stepTmp.addNbExecution(1);
		stepTmp.addTreatErrCount(step.getTreatErrCount());
		stepTmp.addTreatFailedCount(step.getTreatFailedCount());
		stepTmp.addTreatSuccessCount(step.getTreatSuccessCount());
		stepTmp.addTotalDuration(step.getTotalDuration());
		stepTmp.setAverageDuration(stepTmp.getTotalDuration()/stepTmp.getNbExecution());
		recalculateTimes(step, stepTmp);
		return stepTmp;
	}

	public static void recalculateTimes(Step step, Step stepTmp){		
		if (step.getStartTime().getTime() < stepTmp.getStartTime().getTime()){
			stepTmp.setStartTime(step.getStartTime());
		}
		if(stepTmp.getEndTime() != null) {
			if(step.getEndTime().getTime() > stepTmp.getEndTime().getTime()){
				stepTmp.setEndTime(step.getEndTime());
			}
		}	
		switch(stepTmp.getStatus()){
		case "OK" : if(step.getStatus().equals("KOF") || step.getStatus().equals("KOT")) {
			stepTmp.setStatus(step.getStatus());
		}
		break;
		case "KOF" : if(step.getStatus().equals("KOT")){
			stepTmp.setStatus(step.getStatus());
		}
		break;
		}		
		return;
	}


}
