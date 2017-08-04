package controller;

import java.util.HashMap;

import model.TreatmentUnit;


public class GetUnits {

	public static HashMap<String, TreatmentUnit> getUnits(HashMap<String, TreatmentUnit> unitsSarted, HashMap<String, TreatmentUnit> unitsEnd){
		HashMap<String, TreatmentUnit> unitsList = new HashMap<String, TreatmentUnit>();
		HashMap<String, Referential> referential = Referential.getReferential();
		for(String unitId : unitsSarted.keySet()){
			TreatmentUnit unitStart = unitsSarted.get(unitId);
			isUnitReferenced(unitStart, referential);
			if(unitsEnd.containsKey(unitId)){
				TreatmentUnit unitEnd = unitsEnd.get(unitId);
				unitEnd.setStartTime(unitStart.getStartTime());
				long duration = unitEnd.getEndTime().getTime() - unitEnd.getStartTime().getTime();
				unitEnd.setDuration(duration);
				unitEnd.setDurationTxt(GetBatchs.durationToString(duration));
				unitEnd.setReferenced(unitStart.getReferenced());
				unitsList.put(unitId, unitEnd);
			}else{
				unitStart.setStatus("EC");
				unitsList.put(unitId, unitStart);
			}
		}
		return unitsList;
	}


	public static void isUnitReferenced(TreatmentUnit unit, HashMap<String, Referential> referential){
		if(referential.containsKey(unit.getTreatmentName())){
			Referential reference = referential.get(unit.getTreatmentName());
			if(reference.utAndSteps.containsKey(unit.getUnitName())){
				unit.setReferenced(true);
			}
		}
		return;
	}

}


