package controller;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;

import model.Batch;
import model.TreatmentUnit;

public class GetTreatmentUnits {

		public GetTreatmentUnits (){
		}
		
		
		public static HashMap<String, Batch> getTreatUnits() throws JsonProcessingException, IOException {
			HashMap<String, TreatmentUnit> unitsStarted = ElasticRequests.getStartedBatchs();
			HashMap<String, TreatmentUnit> unitsFinished = ElasticRequests.getFinishedBatchs();			
			HashMap<String, Batch> batchsList = new HashMap<String, Batch>();
						
			for(String unitId : unitsStarted.keySet()){
				
				if(unitsFinished.get(unitId) != null){ //regroupement des UT commmencées et celles terminées
					TreatmentUnit unit = unitsStarted.get(unitId);
					TreatmentUnit unitEnd = unitsFinished.get(unitId);
					affectDataToTreatUnit(unit, unitEnd);								
									
					if(batchsList.containsKey(unit.getTreatmentId())) { //regroupement des UT au niveau batch
						recalculateTimes(batchsList.get(unit.getTreatmentId()), unit);
					}else {		
						Batch batch = affectDataToBatch(unit);
						batchsList.put(unit.getTreatmentId(), batch);
					}
					
				}
			}
			return batchsList;
		}
		
		public HashMap<String, TreatmentUnit> getBatchs(HashMap<String, TreatmentUnit> unitsList){
			HashMap<String, TreatmentUnit>  batchsList = new HashMap<String, TreatmentUnit>();
			return batchsList;
		}
		
		public static void affectDataToTreatUnit(TreatmentUnit unit, TreatmentUnit unitEnd){
			unit.setEndTime(unitEnd.getEndTime());
			unit.setDuration(unit.getEndTime().getTime() - unit.getStartTime().getTime());
			unit.setStatus(unitEnd.getStatus());
			unit.settreatSuccessCount(unitEnd.gettreatSuccessCount());
			unit.settreatFailedCount(unitEnd.gettreatFailedCount());
			unit.settreatErrCount(unitEnd.gettreatErrCount());
			return;
		}
		
		public static Batch affectDataToBatch(TreatmentUnit unit){
			Batch batch = new Batch();
			batch.setName(unit.getTreatmentName()); 
			batch.setId(unit.getTreatmentId());
			batch.setStartTime(unit.getStartTime());
			batch.setEndTime(unit.getEndTime());
			batch.setDuration(unit.getDuration());
			batch.setStatus(unit.getStatus());			
			batch.addTreatmentUnit(unit);		
			return batch;
		}
		
		public static void recalculateTimes(Batch batch, TreatmentUnit unit){
			Boolean isModified = false;
			if (unit.getStartTime().getTime() < batch.getStartTime().getTime()){
				batch.setStartTime(unit.getStartTime());
				isModified = true;
			}
			if(unit.getEndTime().getTime() > batch.getEndTime().getTime()) {
				batch.setEndTime(unit.getEndTime());
				isModified = true;
			}
			if(isModified){
				batch.setDuration(batch.getEndTime().getTime()-batch.getStartTime().getTime());
			}			
			switch(batch.getStatus()){
				case "OK" : if(unit.getStatus().equals("KOF") || unit.getStatus().equals("KOT")) {
								batch.setStatus(unit.getStatus());
							}
						break;
				case "KOF" : if(unit.getStatus().equals("KOT")){
								batch.setStatus(unit.getStatus());
							}
						break;
				}		
			batch.addTreatmentUnit(unit);
			return;
		}
		
	
}
