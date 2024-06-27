/**
 * 
 */
package com.studio.domain;

/**
 * @author ezhilraja_k
 *
 */
public class SmsData {

	private Integer noOfOrders;
	private Integer delivered;
	private Integer inProgress;
	private Integer unProcessed;
	private Double cHand;
	private Double cBank;
	private Double shortageAmnt;
	private Double excessAmnt;
	/**
	 * @return the noOfOrders
	 */
	public Integer getNoOfOrders() {
		return noOfOrders;
	}
	/**
	 * @param noOfOrders the noOfOrders to set
	 */
	public void setNoOfOrders(Integer noOfOrders) {
		this.noOfOrders = noOfOrders;
	}
	/**
	 * @return the delivered
	 */
	public Integer getDelivered() {
		return delivered;
	}
	/**
	 * @param delivered the delivered to set
	 */
	public void setDelivered(Integer delivered) {
		this.delivered = delivered;
	}
	/**
	 * @return the inProgress
	 */
	public Integer getInProgress() {
		return inProgress;
	}
	/**
	 * @param inProgress the inProgress to set
	 */
	public void setInProgress(Integer inProgress) {
		this.inProgress = inProgress;
	}
	/**
	 * @return the unProcessed
	 */
	public Integer getUnProcessed() {
		return unProcessed;
	}
	/**
	 * @param unProcessed the unProcessed to set
	 */
	public void setUnProcessed(Integer unProcessed) {
		this.unProcessed = unProcessed;
	}
	/**
	 * @return the cHand
	 */
	public Double getcHand() {
		return cHand;
	}
	/**
	 * @param cHand the cHand to set
	 */
	public void setcHand(Double cHand) {
		this.cHand = cHand;
	}
	/**
	 * @return the cBank
	 */
	public Double getcBank() {
		return cBank;
	}
	/**
	 * @param cBank the cBank to set
	 */
	public void setcBank(Double cBank) {
		this.cBank = cBank;
	}
	/**
	 * @return the shortageAmnt
	 */
	public Double getShortageAmnt() {
		return shortageAmnt;
	}
	/**
	 * @param shortageAmnt the shortageAmnt to set
	 */
	public void setShortageAmnt(Double shortageAmnt) {
		this.shortageAmnt = shortageAmnt;
	}
	/**
	 * @return the excessAmnt
	 */
	public Double getExcessAmnt() {
		return excessAmnt;
	}
	/**
	 * @param excessAmnt the excessAmnt to set
	 */
	public void setExcessAmnt(Double excessAmnt) {
		this.excessAmnt = excessAmnt;
	}
	
	
}
