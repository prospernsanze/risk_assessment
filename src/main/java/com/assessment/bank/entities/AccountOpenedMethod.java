package com.assessment.bank.entities;

public enum AccountOpenedMethod {
	
	FaceToFace(0),
	NoFaceToFace(2);
	
	private int riskScore;
	
	AccountOpenedMethod(int riskScore) {
		this.riskScore=riskScore;
	}
	public int getRiskScore() {
		return riskScore;
	}
}
