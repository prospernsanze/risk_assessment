package com.assessment.bank.entities;

public enum TransactionNature {
	
	BillsPayments(0),
	Salary(3),
	normalTransfers(3),
	ATM_Withdraws(3),
	SmallCashDepositsWithdraws(3),
	highValueWireTransactions(30),
	TransactionsWithSanctionIndustry(30),
	TransactionsIntentionallyBelowReportingThresholds(30);
	
	private int riskScore;
	
	TransactionNature(int riskScore) {
		this.riskScore=riskScore;
	}
	public int getRiskScore() {
		return riskScore;
	}
	

}
