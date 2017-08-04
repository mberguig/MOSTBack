package fr.boursorama.most;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import controller.GetBatchs;
import model.Batch;
import model.TreatmentUnit;

public class GetBatchsTest {

	@Test
	public final void getTreatUnitsTest() throws JsonProcessingException, IOException{
		HashMap<String, TreatmentUnit> unitsStarted = new HashMap<String, TreatmentUnit>();
		HashMap<String, TreatmentUnit> unitsFinished = new HashMap<String, TreatmentUnit>();
		
		TreatmentUnit start = new TreatmentUnit();
		start.setUnitName("testUT");		
		start.setUnitId("1");
		start.setStatus("STARTED");
		start.setTreatmentId("a");
		start.setTreatmentName("testBATCH");
		start.setStartTimeLong(1496311200); //01/06/17-12:00:00		
		unitsStarted.put(start.getUnitId(), start);
		
		TreatmentUnit end = new TreatmentUnit();
		end.setUnitName("testUT");		
		end.setUnitId("1");
		end.setStatus("KO");
		end.setTreatmentId("a");
		end.setTreatmentName("testBATCH");
		end.setEndTimeLong(1496313000); //01/06/17-12:30:00
		unitsFinished.put(end.getUnitId(), end);
		
		HashMap<String, Batch> expected = GetBatchs.getTreatUnits(unitsStarted, unitsFinished);
		
		assertTrue("getTreatUnits", expected.get("a").getStatus().equals("KO") && expected.get("a").getDuration() == 1800 && expected.get("a").treatmentUnits.size() == 1);
		
		
	}
}
