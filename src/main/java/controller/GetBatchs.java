package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonProcessingException;

import model.Batch;
import model.TreatmentUnit;

public class GetBatchs {

	public GetBatchs (){
	}

	//Returns the total number of each Status of Batchs
	public static HashMap<String, String> getTotalNumberOfBatchs(String date, Boolean isCTI) throws JsonProcessingException, IOException{
		HashMap<String, String> totalBatchs = new HashMap<String, String>();
		long nbAll = ElasticRequests.getNbAllResults(date, isCTI);
		long nbOK = ElasticRequests.getNbResultsByStatus(date, "OK", isCTI);
		long nbKOF = ElasticRequests.getNbResultsByStatus(date, "KOF", isCTI);
		long nbKOT = ElasticRequests.getNbResultsByStatus(date, "KOT", isCTI);
		long nbEC = nbAll-(nbOK + nbKOF + nbKOT);
		totalBatchs.put("ALL", String.valueOf(nbAll));
		totalBatchs.put("OK", String.valueOf(nbOK));
		totalBatchs.put("KOF", String.valueOf(nbKOF));
		totalBatchs.put("KOT", String.valueOf(nbKOT));
		totalBatchs.put("EC", String.valueOf(nbEC));
		return totalBatchs;
	}
	
		
	public static HashMap<String, Batch> getBatchs2(HashMap<String, Batch> treatmentsStart, HashMap<String, Batch> treatmentsEnd){
		HashMap<String, Batch> treatmentsList = new HashMap<String, Batch>();
		HashMap<String, Referential> referential = Referential.getReferential();
		for(String batchId : treatmentsStart.keySet()){
			Batch treatStart = treatmentsStart.get(batchId);
			if(treatmentsEnd.containsKey(batchId)){
				Batch treatEnd = treatmentsEnd.get(batchId);
				long duration = treatEnd.getEndTime().getTime() - treatEnd.getStartTime().getTime();
				treatEnd.setDuration(duration);
				treatEnd.setDurationTxt(durationToString(duration));
				if(referential.containsKey(treatEnd.getTreatmentName())){
					treatEnd.setReferenced(true);
				}else {
					treatEnd.setReferenced(false);
				}
				treatmentsList.put(batchId, treatEnd);
			}else{ //batch not ended
				if(referential.containsKey(treatStart.getTreatmentName())){
					treatStart.setReferenced(true);
				}else {
					treatStart.setStatus("EC");
					treatStart.setReferenced(false);
				}
				treatmentsList.put(batchId, treatStart);
			}
		}
		return treatmentsList;
	}
	
	
	public static HashMap<String, Batch> getBatchByStatus(HashMap<String, Batch> treatmentsList) {
		HashMap<String, Referential> referential = Referential.getReferential();
		for(String batchId : treatmentsList.keySet()){
			Batch batch = treatmentsList.get(batchId);
			long duration = batch.getEndTime().getTime() - batch.getStartTime().getTime();
			batch.setDuration(duration);
			batch.setDurationTxt(durationToString(duration));
			if(referential.containsKey(batch.getTreatmentName())){
				batch.setReferenced(true);
			}else{
				batch.setReferenced(false);
			}
		}
		return treatmentsList;
	}
	
	
	
	
	
	
	
	
	
	//---------

	//Returns the started Units as a HashMap (key : unitId)
	public static HashMap<String, TreatmentUnit> getStartedUnitsByPage(ArrayList<String> treatmentIdsList) throws IOException{
		HashMap<String, TreatmentUnit> startedUnits = new HashMap<String, TreatmentUnit>();
		String request = "";
		for (String treatmentId : treatmentIdsList){
			System.out.println(treatmentId);
			request += " OR " + treatmentId;
		}		
		startedUnits.putAll(ElasticRequests.getUnitsByTreatmentId(request, "STARTED"));
		return startedUnits;		
	}


	/**
	 * Concatène les ids à requêter pour récupérer la fin des UT
	 * @param treatmentIdsList
	 * @return
	 * @throws IOException
	 */
	public static HashMap<String, TreatmentUnit> getfinishedUnitsByPage(ArrayList<String> treatmentIdsList) throws IOException {
		HashMap<String, TreatmentUnit> finishedUnits = new HashMap<String, TreatmentUnit>();
		String request = "";
		for (String treatmentId : treatmentIdsList){
			request += " OR " + treatmentId;
		}		
		finishedUnits.putAll(ElasticRequests.getUnitsByTreatmentId(request, "OK OR KOF OR KOT"));
		return finishedUnits;		
	}

	/** 
	 * Recherche les unités de traitement commencées et terminées et les regroupe en batchs et en unités de traitement
	 * @param HashMap contenant les informations du commencement des unités de traitement
	 * @param HashMap contenant les informations de la fin des unités de traitement
	 * @return HashMap that contains the list of Batchs.
	 * **/
	public static HashMap<String, Batch> getTreatUnits(HashMap<String, TreatmentUnit> unitsStarted, HashMap<String, TreatmentUnit> unitsFinished) throws JsonProcessingException, IOException {;			
	HashMap<String, Batch> batchsList = new HashMap<String, Batch>();
	HashMap<String, Referential> referential = Referential.getReferential();
	for(String unitId : unitsStarted.keySet()){
		TreatmentUnit unit = unitsStarted.get(unitId);
		if(unitsFinished.get(unitId) != null){ //regroupement des UT commmencées et celles terminées

			TreatmentUnit unitEnd = unitsFinished.get(unitId);
			affectDataToTreatUnit(unit, unitEnd);								

			if(batchsList.containsKey(unit.getTreatmentId())) { //regroupement des UT au niveau batch
				recalculateTimes(batchsList.get(unit.getTreatmentId()), unit);
			}else {		
				Batch batch = affectDataToBatch(unit);
				isBatchReferenced(batch, unit, referential);
				batchsList.put(unit.getTreatmentId(), batch);
			}
		}else {
			System.out.println("unitId : " + unitId);
			if(batchsList.containsKey(unit.getTreatmentId())) { //regroupement des UT au niveau batch
			}else {		
				Batch batch = affectDataToBatchEC(unit);
				isBatchReferenced(batch, unit, referential);
				batchsList.put(unit.getTreatmentId(), batch);
			}
		}
	}
	return batchsList;
	}

/*
	public static HashMap<String, Batch> getCompletedUnitsByStatus (HashMap<String, TreatmentUnit> unitsFinished) throws JsonProcessingException, IOException{
		HashMap<String, Batch> batchsList = new HashMap<String, Batch>();
		System.out.println("getCompletedUnitsByStatus");
		for(String unitId : unitsFinished.keySet()){
			TreatmentUnit unitStarted = ElasticRequests.getStartedUnitsByStatus(unitId);
			TreatmentUnit unitEnd = unitsFinished.get(unitId);
			if(unitStarted != null){
				affectDataToTreatUnit(unitStarted, unitEnd);
				if(batchsList.containsKey(unitEnd.getTreatmentId())) { //regroupement des UT au niveau batch
					recalculateTimes(batchsList.get(unitEnd.getTreatmentId()), unitEnd);
				}else {		
					Batch batch = affectDataToBatch(unitEnd);
					batchsList.put(unitEnd.getTreatmentId(), batch);
				}
			}
		}
		return batchsList;
	}*/


	/**
	 * Regroupe les informations de lancement et de fin des unités de traitement
	 * @param unit Unité de traitement en cours (avec les données de lancement)
	 * @param unitEnd Unité de traitement terminée
	 */
	public static void affectDataToTreatUnit(TreatmentUnit unit, TreatmentUnit unitEnd){
		unit.setEndTime(unitEnd.getEndTime());
		unit.setDuration(unit.getEndTime().getTime() - unit.getStartTime().getTime());
		unit.setDurationTxt(durationToString(unit.getDuration()));
		unit.setStatus(unitEnd.getStatus());
		unit.setTreatSuccessCount(unitEnd.getTreatSuccessCount());
		unit.setTreatFailedCount(unitEnd.getTreatFailedCount());
		unit.setTreatErrCount(unitEnd.getTreatErrCount());
		isUnitReferenced(unit);
		return;
	}

	/**
	 * Regroupe l'unité de traitement avec son batch correspondant
	 * @param unit Unité de traitement
	 * @return
	 */
	public static Batch affectDataToBatch(TreatmentUnit unit){
		Batch batch = new Batch();
		batch.setTreatmentName(unit.getTreatmentName()); 
		batch.setTreatmentId(unit.getTreatmentId());
		batch.setStartTime(unit.getStartTime());
		batch.setEndTime(unit.getEndTime());
		batch.setDuration(unit.getDuration());
		batch.setDurationTxt(durationToString(unit.getDuration()));
		batch.setStatus(unit.getStatus());			
		batch.addTreatmentUnit(unit.getUnitId(), unit);
		return batch;
	}

	public static Batch affectDataToBatchEC(TreatmentUnit unit){
		Batch batch = new Batch();
		batch.setTreatmentName(unit.getTreatmentName()); 
		batch.setTreatmentId(unit.getTreatmentId());
		batch.setStartTime(unit.getStartTime());
		batch.setStatus("EC");			
		batch.addTreatmentUnit(unit.getUnitId(), unit);
		return batch;
	}

	/**
	 * Vérifie si l'unité de traitement est référencée
	 * @param unit Unité de traitement à vérifier
	 */
	public static void isUnitReferenced(TreatmentUnit unit){
		//referential comparison
		HashMap<String, Referential> referential = Referential.getReferential();
		if(referential.containsKey(unit.getTreatmentName())){
			Referential reference = referential.get(unit.getTreatmentName());
			if(reference.utAndSteps.containsKey(unit.getUnitName())){
				unit.setReferenced(true);
			}					
		}
		return;
	}

	/**
	 * Vérifie si le batch est référencé
	 * @param batch 
	 * @param unit
	 */
	public static void isBatchReferenced(Batch batch, TreatmentUnit unit, HashMap<String, Referential> referential){
		if(unit.getReferenced()) batch.setReferenced(true);
		else {
			if(referential.containsKey(unit.getTreatmentName()))
				batch.setReferenced(true);
			else
				batch.setReferenced(false);
		}
	}

	/**
	 * Réattribue les bonnes dates de début/fin ainsi que les status
	 * @param batch batch quelquel appartien l'unité de traitement
	 * @param unit unité de traitement à insérer dans le batch
	 */
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
			batch.setDuration(batch.getEndTime().getTime() - batch.getStartTime().getTime());
			batch.setDurationTxt(durationToString(batch.getDuration()));
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
		batch.addTreatmentUnit(unit.getUnitId(), unit);
		return;
	}

	/**
	 * formate la durée au format HH:mm:ss
	 * @param duration durée donnée
	 * @return durée au format HH:mm:ss
	 */
	public static String durationToString(long duration)
	{
		long longVal = duration/1000;
		int hours = (int) longVal / 3600;
		int remainder = (int) longVal - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;

		//int[] ints = {hours , mins , secs};
		String timeString = String.format("%02d:%02d:%02d", hours, mins, secs);

		return timeString;
	}



}
