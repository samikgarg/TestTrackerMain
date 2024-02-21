package testtracker.gui;

import java.io.Serializable;

public class Goal implements Serializable
{
	private boolean includeMocks;
	private double scoreGoal; 
	private String subject;
	int y = 0;
	
	public Goal(boolean includeMocks, double scoreGoal, String subject)
	{
		setIncludeMocks(includeMocks);
		setScoreGoal(scoreGoal);
		setSubject(subject);
		
	}


	public boolean isIncludeMocks() {
		return includeMocks;
	}


	public void setIncludeMocks(boolean includeMocks) {
		Test.changeLog.add("Changed Mocks Included from '" + this.includeMocks + "' to '" + includeMocks + "'");
		this.includeMocks = includeMocks;
	}


	public double getScoreGoal() {
		return scoreGoal;
	}


	public void setScoreGoal(double scoreGoal) {
		Test.changeLog.add("Changed Score Goal Included from '" + this.scoreGoal + "' to '" + scoreGoal + "'");
		this.scoreGoal = scoreGoal;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		Test.changeLog.add("Changed Subject Goal Included from '" + this.subject + "' to '" + subject + "'");
		this.subject = subject;
	}

}
