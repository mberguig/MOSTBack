package controller;

import java.util.HashMap;
import java.util.Hashtable;


public class Referential {
	String batchName;
	Hashtable<String, String[]> utAndSteps = new Hashtable<String, String[]>();



	public Referential(){
		
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Hashtable getUtAndSteps() {
		return utAndSteps;
	}

	public void setUtAndSteps(Hashtable utAndSteps) {
		this.utAndSteps = utAndSteps;
	}
	
	public static HashMap<String, Referential> getReferential(){
		HashMap<String, Referential> referential = new HashMap<String, Referential>();
		Referential CRMSFOL = new Referential();
		CRMSFOL.batchName = "CRMSFOL";
		String[] stepsSfol = {"callBDF", "activateAdditionnalBankAccount", "sendMoney", "activateSavingAccount", "currentBalance", "changeStatusFullOnlineAddon", "currentBalance"};
		CRMSFOL.utAndSteps.put("SfolAccountActivationNew", stepsSfol);
		Referential CRMCDFNDRE = new Referential();
		CRMCDFNDRE.batchName="CRMCDFNDRE";
		String[] stepsFG_MAIL_SEND = {};
		CRMCDFNDRE.utAndSteps.put("FG_MAIL_SEND", stepsFG_MAIL_SEND);

		referential.put(CRMSFOL.getBatchName(), CRMSFOL);
		referential.put(CRMCDFNDRE.getBatchName(), CRMCDFNDRE);

		return referential;
	}
}
