package fr.boursorama.most.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import controller.ElasticRequests;
import controller.GetSteps;
import controller.GetUnits;
import controller.Referential;
import controller.GetBatchs;
import model.Batch;
import model.Step;
import model.TreatmentUnit;

@RestController
@RequestMapping("/api/")
public class Routes {

	// Get nb Results
	@RequestMapping(value="/getTotalNumberOfBatchs/{date}/{isCTI}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, String> getNbAll(@PathVariable String date, @PathVariable Boolean isCTI) throws IOException{
		HashMap<String, String> totalBatchs = new HashMap<String, String>();
		long nb = ElasticRequests.getNbAllResults(date, isCTI);
		totalBatchs.put("result", String.valueOf(nb));
		return totalBatchs;
	}

	@RequestMapping(value="/getTotalNumberOfBatchsByStatus/{status}/{date}/{isCTI}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, String> getNbByStatus(@PathVariable String status, @PathVariable String date, @PathVariable Boolean isCTI) throws IOException{
		HashMap<String, String> totalBatchs = new HashMap<String, String>();
		long nb = ElasticRequests.getNbResultsByStatus(date, status, isCTI);
		totalBatchs.put("result", String.valueOf(nb));
		return totalBatchs;
	}

	@RequestMapping(value="/getTotalNumberOfBatchsEC/{date}/{isCTI}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, String> getNbEC(@PathVariable String date, @PathVariable Boolean isCTI) throws IOException{
		HashMap<String, String> totalBatchs = new HashMap<String, String>();
		long nb = ElasticRequests.getNbAllResults(date, isCTI);
		long nbEnd = ElasticRequests.getNbResultsByStatus(date, "OK OR KOF OR KOT", isCTI);		
		totalBatchs.put("result", String.valueOf(nb-nbEnd));
		return totalBatchs;
	}

	//récupère tous les batchs
	@RequestMapping(value = "/batchs2/{date}/{page}/{isCTI}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Batch> getBatch2(@PathVariable String date, @PathVariable Integer page, @PathVariable boolean isCTI) throws IOException{
		HashMap<String, Batch> treatmentsStart = ElasticRequests.getTreatments(date, page, isCTI, "STARTED");
		HashMap<String, Batch> treatmentsEnd = ElasticRequests.getTreatments(date, page, isCTI, "OK OR KOF OR KOT");
		return GetBatchs.getBatchs2(treatmentsStart, treatmentsEnd);
	}

	//Récupère les batchs par statut :
	@RequestMapping(value = "/batchsByStatus/{status}/{date}/{page}/{isCTI}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Batch> getBatchByStatus(@PathVariable String date, @PathVariable Integer page, @PathVariable boolean isCTI, 
			@PathVariable String status) throws IOException{
		HashMap<String, Batch> treatments = ElasticRequests.getTreatmentsIdsStatus(date, status, page, isCTI);
		return GetBatchs.getBatchByStatus(treatments);
	}

	//Recupere les UT selon l'id du batch (effectif)
	@RequestMapping(value = "/units/{batchId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, TreatmentUnit> getUnits(@PathVariable String batchId) throws IOException{	
		HashMap<String, TreatmentUnit> unitsStarted = ElasticRequests.getUnitsByTreatmentId(batchId, "STARTED");
		HashMap<String, TreatmentUnit> unitsFinished = ElasticRequests.getUnitsByTreatmentId(batchId, "OK OR KOF OR KOT");
		HashMap<String, TreatmentUnit> steps = GetUnits.getUnits(unitsStarted, unitsFinished);
		return steps;
	}
	
	//Recupere les etapes selon l'id de l'UT (effectif)
	@RequestMapping(value = "/steps/{unitId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Step> getSteps(@PathVariable String unitId) throws IOException{	
		HashMap<String, Step> stepsStarted = ElasticRequests.getSteps(unitId, "STARTED");
		HashMap<String, Step> stepsFinished = ElasticRequests.getSteps(unitId, "OK OR KOF OR KOT");
		HashMap<String, Step> steps = GetSteps.getStepsByName(stepsStarted, stepsFinished);
		return steps;
	}














	//-------------------------




	//Recupere les batchs par date et statut (effectif)
	/*@RequestMapping(value = "/batchsByStatus/{date}/{status}", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Batch> getBatchByStatus(@PathVariable String date, @PathVariable String status) throws IOException{
		HashMap<String, Batch> batchs = new HashMap<String, Batch>();
		HashMap<String, TreatmentUnit> unitsFinished = ElasticRequests.getUnitsByStatus(date, status);	
		batchs = GetBatchs.getCompletedUnitsByStatus(unitsFinished);
		return batchs;
	}*/


	@RequestMapping(value="/referential", method = RequestMethod.GET, headers = "Accept=application/json")
	public HashMap<String, Referential> getReferential() throws IOException{
		HashMap<String, Referential> referential = Referential.getReferential();
		return referential;
	}

	@RequestMapping(value="/testBatchs/{date}/{page}/{isCTI}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ArrayList<String> getTestBatchs(@PathVariable String date, @PathVariable Integer page, @PathVariable boolean isCTI) throws IOException{
		return ElasticRequests.getTreatmentsIds(date, page, isCTI);
	}




}
