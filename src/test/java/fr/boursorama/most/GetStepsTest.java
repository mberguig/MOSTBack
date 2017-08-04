package fr.boursorama.most;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import controller.GetSteps;
import model.Step;


public class GetStepsTest {

	@Test
	public final void getStepsTest() throws JsonProcessingException, IOException{
		HashMap<String, Step> stepsStarted = new HashMap<String, Step>();
		HashMap<String, Step> stepsFinished = new HashMap<String, Step>();
		
		Step step1 = new Step();
		step1.setstepName("step1");
		step1.setId("1");
		step1.setStartTimeLong(1496313000); //01/06/17-12:30:00
		step1.setStatus("STARTED");
		
		stepsStarted.put("1", step1);
		
		Step step1end = new Step();
		step1end.setstepName("step1");
		step1end.setId("1");
		step1end.setEndTimeLong(1496313300); //01/06/17-12:35:00
		step1end.setTreatSuccessCount(1);
		step1end.setTreatFailedCount(0);
		step1end.setTreatErrCount(0);
		step1end.setStatus("OK");
		
		stepsFinished.put("1", step1end);
		
		Step step2 = new Step();
		step2.setstepName("step2");
		step2.setId("2");
		step2.setStartTimeLong(1496313330); //01/06/17-12:35:30
		step2.setStatus("STARTED");
		
		stepsStarted.put("2", step2);
		
		Step step2end = new Step();
		step2end.setstepName("step2");
		step2end.setId("2");
		step2end.setEndTimeLong(1496313400); //01/06/17-12:36:40	
		step2end.setTreatSuccessCount(0);
		step2end.setTreatFailedCount(1);
		step2end.setTreatErrCount(0);
		step2end.setStatus("KOF");
		
		stepsFinished.put("2", step2end);
		
		Step step3 = new Step();
		step3.setstepName("step1");
		step3.setId("3");
		step3.setStartTimeLong(1496313410); //01/06/17- 12:36:50	
		step3.setStatus("STARTED");
		
		stepsStarted.put("3", step3);
		
		Step step3end = new Step();
		step3end.setstepName("step1");
		step3end.setId("3");
		step3end.setEndTimeLong(1496313600); //01/06/17-12:40:00
		step3end.setTreatSuccessCount(1);
		step3end.setTreatFailedCount(0);
		step3end.setTreatErrCount(0);
		step3end.setStatus("OK");
		
		stepsFinished.put("3", step3end);
		
		Step step4 = new Step();
		step4.setstepName("step2");
		step4.setId("4");
		step4.setStartTimeLong(1496313610); //01/06/17-12:40:10
		step4.setStatus("STARTED");
		
		stepsStarted.put("4", step4);
		
		Step step4end = new Step();
		step4end.setstepName("step2");
		step4end.setId("4");
		step4end.setEndTimeLong(1496313800); //01/06/17-12:43:20
		step4end.setTreatSuccessCount(0);
		step4end.setTreatFailedCount(0);
		step4end.setTreatErrCount(1);
		step1end.setStatus("KOT");
		
		stepsFinished.put("4", step4end);
	
		HashMap<String, Step> expected = GetSteps.getStepsByName(stepsStarted, stepsFinished);
		
		Step totalStep1 = expected.get("step1");
		Step totalStep2 = expected.get("step2");
		
		assertTrue("getTreatUnits", totalStep1.getNbExecution() == 2 && totalStep1.getStatus().equals("OK") 
				&& totalStep1.getTreatSuccessCount() == 2 && totalStep1.getTotalDuration() == 490 
				&& totalStep1.getAverageDuration() == 245 
				&& totalStep2.getNbExecution()==2 && totalStep2.getStatus().equals("KOT") 
				&& totalStep2.getTotalDuration() == 280 && totalStep2.getAverageDuration() == 140);
		
		
	}
}
