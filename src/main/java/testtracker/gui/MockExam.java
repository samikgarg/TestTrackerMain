package testtracker.gui;

import java.util.ArrayList;

public class MockExam extends Test 
{

	public MockExam(String testName, int score, String reflection, int total, String subject, int ID, ArrayList<Test> tests) {
		super(testName, score, reflection, total, subject, ID, tests);
	}
	
	public String toString ()
	{
		return "MOCK EXAM " + super.toString();
	}

}
