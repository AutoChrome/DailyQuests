package com.autochrome.dailyquests;

public class Quest {
	private String reward;
	private String type;
	private String condition;
	private int conditionQuantity;
	private String rewardDescription;
	
	public Quest(String type, String condition, int conditionQuantity, String reward, String rewardDescription) {
		this.type = type;
		this.condition = condition;
		this.conditionQuantity = conditionQuantity;
		this.reward = reward;
		this.rewardDescription = rewardDescription;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getConditionQuantity() {
		return conditionQuantity;
	}

	public void setConditionQuantity(int conditionQuantity) {
		this.conditionQuantity = conditionQuantity;
	}
	
	public String getRewardDescription() {
		return rewardDescription;
	}

	public void setRewardDescription(String rewardDescription) {
		this.rewardDescription = rewardDescription;
	}
}
