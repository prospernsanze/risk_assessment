package com.assessment.bank.entities;

public enum PurposeAccount {
	
	PersonalSaving(0),
	Investment(5),
	BusinessOperations(5),
	NonProfit(20),
	Salary(2),
	TrustAccount(2),
	SecurityTrading(30),
	ThirdPayment(30),
	Others(10);
	
    private int riskScore;
	
	PurposeAccount(int riskScore) {
		this.riskScore=riskScore;
	}
	public int getRiskScore() {
		return riskScore;
	}
	

}
