package com.autochrome.dailyquests;

public class AssignedQuest {
	private Quest quest;
	private Boolean isCompleted;
	private int progress;
	
	public AssignedQuest(Quest quest, Boolean isCompleted, int progress){
		this.quest = quest;
		this.isCompleted = isCompleted;
		this.progress = progress;
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
}
