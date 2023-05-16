package com.assessment.bank.entities;

public enum NatureBusiness {
	
	Retail(3),
	consulting_firms(3),
	Education(2),
	Hospitality(5),
	Realestate(5),
	HealthCare(4),
	MoneyService(25),
	Gambling(30),
	CryptoCurrency(30),
	NonProfitOrganization(30),
	Gaming(30);
	
    private int riskScore;
	
	NatureBusiness(int riskScore) {
		this.riskScore=riskScore;
	}
	public int getRiskScore() {
		return riskScore;
	}
	
	
}
