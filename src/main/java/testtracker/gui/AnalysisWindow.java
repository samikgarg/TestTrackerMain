package testtracker.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;

public class AnalysisWindow extends JFrame {

	private JPanel contentPane;
	
	private JComboBox cmbSubject;
	private JLabel lblSubject;
	private JLabel lblCurrScore;
	private JLabel lblGoalScore;
	private JLabel lblScoreRequired;
	private JPanel pnlChart;
	//private JPanel pnlChart;
	
	private TestExplorer parent;
	private HashMap<String, ArrayList<Test>> tests = new HashMap<String, ArrayList<Test>>();
	
	private boolean isPossible = true;
	private boolean isZeroTests;

	/**;
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnalysisWindow frame = new AnalysisWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AnalysisWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 817, 557);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(TestExplorer.DARK_BLUE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cmbSubject = new JComboBox();
		cmbSubject.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				setData();
				drawChart(cmbSubject.getSelectedItem().toString());
			}
		});
		cmbSubject.setBounds(19, 83, 187, 27);
		resetSubjects();
		contentPane.add(cmbSubject);
		cmbSubject.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, TestExplorer.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		cmbSubject.setBackground(TestExplorer.LIGHT_BLUE);
		
		lblSubject = new JLabel("Subject");
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(23, 64, 61, 16);
		contentPane.add(lblSubject);
		
		lblCurrScore = new JLabel("Your Current Score is: ");
		lblCurrScore.setForeground(Color.WHITE);
		lblCurrScore.setBounds(23, 134, 421, 16);
		contentPane.add(lblCurrScore);
		
		lblGoalScore = new JLabel("Your Goal Score is: ");
		lblGoalScore.setForeground(Color.WHITE);
		lblGoalScore.setBounds(23, 162, 421, 16);
		contentPane.add(lblGoalScore);
		
		lblScoreRequired = new JLabel("Score Required in next Test to Reach Goal:");
		lblScoreRequired.setForeground(Color.WHITE);
		lblScoreRequired.setBounds(23, 190, 770, 16);
		contentPane.add(lblScoreRequired);
		
		JButton btnBack = new JButton("< Back");
		btnBack.setForeground(new Color(255, 255, 255));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				back();
			}
		});
		btnBack.setBorder( new LineBorder(Color.WHITE) );
		btnBack.setBorderPainted(false);
		btnBack.setBounds(6, 6, 67, 29);
		contentPane.add(btnBack);
		
	}
	
	@Override
	public void setVisible (boolean visible)
	{
		if (visible)
		{
			resetSubjects();
		}
		
		super.setVisible(visible);
	}
	
	private void resetSubjects()
	{
		DefaultComboBoxModel DCMB = new DefaultComboBoxModel();
		DCMB.addElement("All Tests");
		for (int i = 0; i < Test.globalSubjects.size(); i++)
		{
			DCMB.addElement(Test.globalSubjects.get(i));
		}
		cmbSubject.setModel(DCMB);
	}
	
	public AnalysisWindow (TestExplorer parent)
	{
		this();
		this.parent = parent;
		
		//if (tests.containsKey(parent.cmbSubjectFilter.getSelectedItem().toString()))
		setScoresData();
		setData();
		drawChart(cmbSubject.getSelectedItem().toString());
		
		if (!parent.cmbSubjectFilter.getSelectedItem().toString().equals("All Tests"))
		{
			cmbSubject.setSelectedItem(parent.cmbSubjectFilter.getSelectedItem());
		}
	}
	
	private void setScoresData()
	{
		tests.put("All Tests", parent.getTests());
		
		for (int i = 0; i < Test.globalSubjects.size(); i++)
		{
			ArrayList<Test> scoresAL = new ArrayList<Test>();
				
			for (int k = 0; k < parent.getTests().size(); k++)
			{
				if (parent.getTests().get(k).getSubject().equalsIgnoreCase(Test.globalSubjects.get(i)))
				{
					scoresAL.add(parent.getTests().get(k));
				}
			}
				
			tests.put(Test.globalSubjects.get(i), scoresAL);
		}
	}
	
	private double[] getScoreSum (String subject)
	{
		if (tests.containsKey(subject))
		{
			double sum = 0;
			double noTests = 0;
			
			ArrayList <Test> currTests = tests.get(subject);
			
			for (int i = 0; i < currTests.size(); i++)
			{
				if (Goals.goals.containsKey(subject))
				{
					if (!Goals.goals.get(subject).isIncludeMocks())
					{
						if (!(currTests.get(i) instanceof MockExam))
						{
							sum += currTests.get(i).testPercentage(false);
							noTests ++;
						}
					}
					else
					{
						sum += currTests.get(i).testPercentage(false);
						noTests ++;
					}
				}
				else
				{
					sum += currTests.get(i).testPercentage(false);
					noTests ++;
				}
			}
			
			double[] sumArray = new double[2];
			sumArray[0] = sum;
			sumArray[1] = noTests;
			
			return sumArray;
		}
		else
		{
			return null;
		}
	}
	
	private double getScoreAverage (String subject)
	{
		double[] sumArray = getScoreSum(subject);
		
		if (sumArray == null)
		{
			return 0;
		}
		else
		{
			return sumArray[0]/sumArray[1];
		}
		
	}
	
	private double[] getNextTestsScores (String subject)
	{
		
		isPossible = true;
		double[] nextTestsScoresArray = new double[2];
		
		double[] sumArray = getScoreSum(subject);
		double sum = sumArray[0];
		double noTests = sumArray[1];
		double goalAverage = Goals.goals.get(subject).getScoreGoal();
		
		double noNextTests = 0;
		double nextTestsSum = 0;
		
		while (true)
		{
			double currNextTestsSum = goalAverage * ((noTests + noNextTests) + 1);
			double currAverage = currNextTestsSum - (sum + nextTestsSum);
			
			
			if (noNextTests > 50)
			{
				isPossible = false;
				break;
			}
			else if (currAverage > 100)
			{
				nextTestsSum += 100;
				noNextTests++;
			}
			else 
			{
				nextTestsSum += currAverage;
				noNextTests++;
				break;
			}
		}
		
		nextTestsScoresArray[0] = nextTestsSum / noNextTests;
		nextTestsScoresArray[1] = noNextTests;
		
		isZeroTests = noTests == 0;
		
		return nextTestsScoresArray;
	}
	
	
	private void setData()
	{
		String subject = cmbSubject.getSelectedItem().toString();
		
		double average = getScoreAverage (subject);
		average = Math.round(average * 10);
		average /= 10;
		double nextTestScore = 0;
		
		lblCurrScore.setText("Your Average Score is " + average + "%");
		
		if (Goals.goals.containsKey(subject))
		{			
			double goal = Goals.goals.get(subject).getScoreGoal();
			goal = Math.round(goal * 10);
			goal /= 10;
			
			double[] nextTestsScores = getNextTestsScores(subject);
			nextTestScore = nextTestsScores[0];
			long noNextTests = Math.round(nextTestsScores[1]);
			nextTestScore = Math.round(nextTestScore * 10);
			nextTestScore /= 10;
			
			if (isZeroTests)
			{
				lblCurrScore.setText("You have not done any tests for " + subject);
			}
			
			lblGoalScore.setText("Your Goal Score is " + goal + "%");
			
			if (!isPossible)
			{
				lblScoreRequired.setText("It is not Possible to meet your Goal in the next 50 Tests");
			}
			else if (nextTestScore < 0)
			{
				lblScoreRequired.setText("Your Goal will be maintained no Matter what your Next Test Score is");
			}
			else if (goal < average)
			{
				lblScoreRequired.setText("Great Job, you have met your Goal! The Average Score Required to Maintain your Goal is at least " + nextTestScore + "% across " + noNextTests + " tests");
			}
			else
			{
				lblScoreRequired.setText("The Average Score Required to Reach your Goal is at least " + nextTestScore + "% across " + noNextTests + " tests");
			}
			
			lblScoreRequired.setVisible(true);
		}
		else
		{
			lblGoalScore.setText("You do not have a Goal for " + subject);
			lblScoreRequired.setVisible(false);
		}
	}
	
	private void back()
	{
		parent.setVisible(true, this);
		this.setVisible(false);
	}
	
	public void drawChart(String subject)
	{
		ArrayList<Test> currTests = tests.get(subject);
		
		boolean searching = true;
		
		while (searching)
		{
			searching = false;
			for (int i = 0; i < currTests.size()-1; i++)
			{
				if (currTests.get(i).getID() > currTests.get(i+1).getID())
				{
					Test tempTest = currTests.get(i);
					currTests.set(i, currTests.get(i+1));
					currTests.set(i+1, tempTest);
					searching = true;
					
				}
			}
		}
		
		if (Goals.goals.containsKey(subject))
		{
			pnlChart = new JPanel();
			pnlChart.setBounds(23, 264, 350, 248);
			contentPane.add(pnlChart);
			pnlChart.setLayout(new BorderLayout(0, 0));
			
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
			
			for (int i = 0; i < currTests.size(); i++)
			{
				if (currTests.get(i) instanceof MockExam && Goals.goals.get(subject).isIncludeMocks())
				{
					
				}
				else
				{
					dataset.addValue(currTests.get(i).testPercentage(false), "Current Test Scores", currTests.get(i).getTestName());
				}
			}
			
			double[] nextTestScores = getNextTestsScores(subject);
			
			for (int i = 1; i < nextTestScores[1] + 1; i++)
			{
				dataset.addValue(nextTestScores[0], "Next Test Scores", "Next Test " + i);
			}
			
			JFreeChart chart = ChartFactory.createBarChart(  
			        subject,  
			        "test", 
			        "Score",
			        dataset  
			        ); 
			
			chart.setBackgroundPaint(TestExplorer.DARK_BLUE);
		    Plot plot = chart.getPlot();
			plot.setBackgroundPaint(new Color(1, 24, 61));
			
			CategoryPlot cp = (CategoryPlot) plot;
			cp.getDomainAxis().setTickLabelsVisible(false);
			
			if (plot instanceof CategoryPlot) {
		        setAxisFontColor(((CategoryPlot) plot).getDomainAxis(), Color.WHITE);
		        setAxisFontColor(((CategoryPlot) plot).getRangeAxis(), Color.WHITE);
		    } else if (plot instanceof XYPlot) {
		        setAxisFontColor(((XYPlot) plot).getDomainAxis(), Color.WHITE);
		        setAxisFontColor(((XYPlot) plot).getRangeAxis(), Color.WHITE);
		    }
			
			chart.getTitle().setPaint(Color.WHITE);
			
			chart.getLegend().setBackgroundPaint(TestExplorer.DARK_BLUE);
			chart.getLegend().setItemPaint(Color.WHITE);
			
		    ChartPanel panel = new ChartPanel(chart);  
		    pnlChart.add(panel); 
		    
		    /*if (nextTestScores[1] > 50 || nextTestScores[0] <= 0)
		    {
		    	pnlChart.setVisible(false);
		    	panel.setVisible(false);
		    }
		    else
		    {
		    	pnlChart.setVisible(true);
		    	panel.setVisible(true);
		    }*/
		}
	}
	
	private void setAxisFontColor(Axis axis, Color fontColor) 
	{
	    if (!fontColor.equals(axis.getLabelPaint()))
	    	axis.setLabelPaint(fontColor);
	    if (!fontColor.equals(axis.getTickLabelPaint()))
	        axis.setTickLabelPaint(fontColor);
	}
}
