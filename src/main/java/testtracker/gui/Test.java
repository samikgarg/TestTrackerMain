package testtracker.gui;

import java.io.Serializable;
import java.util.ArrayList;

public class Test implements Serializable
{

	public static ArrayList<String> globalSubjects = new ArrayList<String>();
	
	static ArrayList<String> changeLog = new ArrayList<String>();
	
	
	private String testName;
	
	private int score;
	
	private String reflection;
	
	private int total;
	
	private String subject;
	
	private int ID;
	
	private int y = 0;
	
	public Test(String testName, int score, String reflection, int total, String subject, int ID, ArrayList<Test> tests) 
	{
		super();
		
		setTestName(testName);
		setScore(score);
		setReflection(reflection);
		setTotal(total);
		setSubject(subject.trim());
		setID(ID, tests);
		
		ammendGlobalSubjects(subject);
	}
	
	public String getSubject()
	{
		return subject;
	}
	
	public String toString()
	{
		return subject + ", " + testName + ": " + testPercentage() + "%";
	}
	
	public String testPercentage()
	{
		String testPercentage;
		
		double dScore = (double) score;
		double dTotal = (double) total;
		
		double percentage = (dScore/dTotal) * 100;
		percentage = Math.round(percentage * 10);
		percentage /= 10;
		
		testPercentage = String.valueOf(percentage);
		
		return testPercentage;
	}
	
	public double testPercentage (boolean rawPercentage)
	{
		double percentage = (double) score / (double) total;
		
		if (!rawPercentage)
		{
			percentage = 100 * percentage;
			percentage = Math.round(percentage * 10);
			percentage /= 10;
		}
		else
		{
			percentage = Math.round(percentage * 10);
			percentage /= 10;
		}
		
		return percentage;
	}
	
	private void ammendGlobalSubjects(String subject)
	{
		if (!globalSubjects.contains(subject))
		{
			globalSubjects.add(subject);
		}
	}

	public static ArrayList<String> getChangeLog() {
		return changeLog;
	}

	public static void setChangeLog(ArrayList<String> changeLog) {
		Test.changeLog = changeLog;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		changeLog.add("Changed Test Name from '" + this.testName + "' to '" + testName + "'");
		
		this.testName = testName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		changeLog.add("Changed Score from '" + this.score + "' to '" + score + "'");
		
		this.score = score;
	}

	public String getReflection() {
		return reflection;
	}

	public void setReflection(String reflection) {
		changeLog.add("Changed Reflection from '" + this.reflection + "' to '" + reflection + "'");
		
		this.reflection = reflection;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		changeLog.add("Changed Total from '" + this.total + "' to '" + total + "'");
		
		this.total = total;
	}

	public void setSubject(String subject) {
		changeLog.add("Changed Subject from '" + this.subject + "' to '" + subject + "'");
		
		ammendGlobalSubjects(subject);
		
		this.subject = subject;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public void setID (int ID, ArrayList<Test> tests)
	{
		if (ID == 0)
		{
			int maxID = 0;
			
			for (int i = 0; i < tests.size(); i++)
			{
				if (tests.get(i).getID() > maxID)
				{
					maxID = tests.get(i).getID();
				}
			}
			
			this.ID = maxID + 1;
		}
		else
		{
			this.ID = ID;
		}
	}
	
	public static ArrayList<String> getGlobalSubjects()
	{
		return globalSubjects;
	}
	
	public static void setGlobalSubjects (ArrayList<String> globalSubjects)
	{
		Test.globalSubjects = globalSubjects;
	}
}
