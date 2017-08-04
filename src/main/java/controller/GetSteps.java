package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import model.Step;

public class GetSteps {

	public GetSteps(){
	}

	public static HashMap<String, Step> getStepsByName(HashMap<String, Step> stepsStarted, HashMap<String, Step> stepsFinished) 
			throws JsonProcessingException, IOException {
		HashMap<String, Step> stepsMap = new HashMap<String, Step>();
		for(String stepId : stepsStarted.keySet()){
			Step stepStart = stepsStarted.get(stepId);
			String stepName = stepStart.getstepName();
			if(stepsMap.containsKey(stepName)){     //1 étape a déjà le même nom
				Step stepMap = stepsMap.get(stepName);
				if(stepsFinished.containsKey(stepId)){
					Step stepEnd = stepsFinished.get(stepId);				
					setDataToStep(stepMap, stepStart, stepEnd);
				}else {                  //etape non terminée
					stepMap.addNbExecution(1);
					stepMap.setStatus("EC");
				}
			}else { //si l'étape n'existe pas avant
				if(stepsFinished.containsKey(stepId)){
					Step stepEnd = stepsFinished.get(stepId);
					long duration = stepEnd.getEndTime().getTime() - stepStart.getStartTime().getTime();
					stepEnd.setTotalDuration(duration);
					stepEnd.setAverageDuration(duration);
					stepEnd.setAverageDurationTxt(GetBatchs.durationToString(duration));
					stepEnd.setTotalDurationTxt(GetBatchs.durationToString(duration));		
					stepsMap.put(stepName, stepEnd);										
				} else{  //non terminée
					stepStart.setStatus("EC");
					stepsMap.put(stepName, stepStart);
				}
			}
		}
		return stepsMap;
	}


	public static void setDataToStep(Step stepMap, Step stepStart, Step stepEnd){
		stepMap.addNbExecution(1);
		long duration = stepEnd.getEndTime().getTime() - stepStart.getStartTime().getTime();
		stepMap.addTotalDuration(duration);
		stepMap.setAverageDuration(stepMap.getTotalDuration()/stepMap.getNbExecution());
		stepMap.setTotalDurationTxt(GetBatchs.durationToString(stepMap.getTotalDuration()));
		stepMap.setAverageDurationTxt(GetBatchs.durationToString(stepMap.getAverageDuration()));
		stepMap.addTreatErrCount(stepEnd.getTreatErrCount());
		stepMap.addTreatFailedCount(stepEnd.getTreatFailedCount());
		stepMap.addTreatSuccessCount(stepEnd.getTreatSuccessCount());		
		switch(stepMap.getStatus()){
		case "OK" : if(stepEnd.getStatus().equals("KOF") || stepEnd.getStatus().equals("KOT")) {
			stepMap.setStatus(stepEnd.getStatus());
		}
		break;
		case "KOF" : if(stepEnd.getStatus().equals("KOT")){
			stepMap.setStatus(stepEnd.getStatus());
		}
		break;
		}
		return;
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
