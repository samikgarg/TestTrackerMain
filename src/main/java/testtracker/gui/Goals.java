package testtracker.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.event.ChangeEvent;
import java.awt.Color;

public class Goals extends JFrame implements Serializable 
{

	private JComboBox<String> cmbSubject;
	private JSpinner spnScoreGoal;
	private JCheckBox chckIncludeMocks;
	private JButton btnCancel; 
	private JButton btnSave;
	
	private JPanel contentPane;
	private TestExplorer parent;
	
	private String currSubject;
	
	public static HashMap<String,Goal> goals = new HashMap<String,Goal>();
	private HashMap<String,Goal> tempGoals;
	private JLabel lblError;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Goals frame = new Goals();
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
	public Goals() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 301, 329);
		contentPane = new JPanel();
		contentPane.setBackground(TestExplorer.DARK_BLUE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tempGoals = new HashMap<String,Goal>();
		
		if (goals.get("All Tests") != null)
		{
			tempGoals.put("All Tests", goals.get("All Tests"));
		}
		
		for (int i = 0; i < Test.globalSubjects.size(); i++)
		{
			if (goals.get(Test.globalSubjects.get(i)) != null)
			{
				tempGoals.put(Test.globalSubjects.get(i), goals.get(Test.globalSubjects.get(i)));
			}
		}
		
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(20, 24, 61, 16);
		contentPane.add(lblSubject);
		lblSubject.setBackground(TestExplorer.LIGHT_BLUE);
		
		spnScoreGoal = new JSpinner();
		spnScoreGoal.addChangeListener(new ChangeListener() 
		{
			public void stateChanged(ChangeEvent e) 
			{
				tempSave();
			}
		});
		spnScoreGoal.setBounds(19, 114, 147, 26);
		JSpinner.NumberEditor jsEditor1 = (JSpinner.NumberEditor) spnScoreGoal.getEditor(); 
		jsEditor1.getTextField().setBackground(TestExplorer.LIGHT_BLUE);
		contentPane.add(spnScoreGoal);
		
		JLabel lblAverageScore = new JLabel("Average Percentage Score Goal");
		lblAverageScore.setForeground(Color.WHITE);
		lblAverageScore.setBounds(19, 94, 199, 16);
		contentPane.add(lblAverageScore);
		
		chckIncludeMocks = new JCheckBox("Include Mock Exams in Average");
		chckIncludeMocks.setForeground(Color.WHITE);
		chckIncludeMocks.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				tempSave();
			}
		});
		chckIncludeMocks.setBounds(16, 172, 237, 23);
		contentPane.add(chckIncludeMocks);
		
		
		cmbSubject = new JComboBox();
		DefaultComboBoxModel DCMB = new DefaultComboBoxModel();
		DCMB.addElement("All Tests");
		for (int i = 0; i < Test.globalSubjects.size(); i++)
		{
			DCMB.addElement(Test.globalSubjects.get(i));
		}
		cmbSubject.setModel(DCMB);
		cmbSubject.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (!isError())
				{
					fillFields();
				}
				else
				{
					cmbSubject.setSelectedItem(currSubject);
				}
			}
		});
		cmbSubject.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, TestExplorer.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		((JTextField) cmbSubject.getEditor().getEditorComponent()).setBackground(TestExplorer.LIGHT_BLUE);
		cmbSubject.setBounds(16, 41, 189, 27);
		contentPane.add(cmbSubject);
		
		btnSave = new JButton("Save");
		btnSave.setForeground(new Color(255, 255, 255));
		btnSave.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					save();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		btnSave.setBorder( new LineBorder(Color.WHITE) );
		btnSave.setBounds(206, 266, 89, 29);
		contentPane.add(btnSave);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setForeground(new Color(255, 255, 255));
		btnCancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				cancel();
			}
		});
		btnCancel.setBorder( new LineBorder(Color.WHITE) );
		btnCancel.setBounds(105, 266, 89, 29);
		contentPane.add(btnCancel);
		
		lblError = new JLabel("New label");
		lblError.setForeground(Color.RED);
		lblError.setBounds(20, 226, 275, 16);
		contentPane.add(lblError);
		
		lblError.setVisible(false);
		
		fillFields();
	}
	
	public Goals (TestExplorer parent)
	{
		this();
		this.parent = parent;
	}
	
	public void fillFields()
	{
		currSubject = (String) cmbSubject.getSelectedItem();
		
		if (tempGoals.containsKey(currSubject))
		{
			Goal currGoal = tempGoals.get(currSubject);
			chckIncludeMocks.setSelected(currGoal.isIncludeMocks());
			spnScoreGoal.setValue(currGoal.getScoreGoal());
			
		}
		else if (tempGoals.containsKey("All Tests"))
		{
			Goal currGoal = tempGoals.get("All Tests");
			chckIncludeMocks.setSelected(currGoal.isIncludeMocks());
			spnScoreGoal.setValue(currGoal.getScoreGoal());
		}
		else
		{
			chckIncludeMocks.setSelected(false);
			spnScoreGoal.setValue(0);
		}
	}
	
	public void cancel()
	{
		this.setVisible(false);
		parent.setVisible(true, this);
	}
	
	public void tempSave()
	{
		if (!isError())
		{
			Goal goal = new Goal(chckIncludeMocks.isSelected(), Double.parseDouble(spnScoreGoal.getValue().toString()), (String) cmbSubject.getSelectedItem());
			
			if (tempGoals.containsKey(currSubject))
			{
				tempGoals.replace(currSubject, goal);
			}
			else
			{
				tempGoals.put(currSubject, goal);
			}
		}
	}
	
	public void save() throws IOException
	{
		if (!isError())
		{
			goals = tempGoals;
			
			if (goals.get("All Tests") != null)
			{
				for (int i = 0; i < Test.globalSubjects.size(); i++)
				{
					if (!goals.containsKey(Test.globalSubjects.get(i)))
					{
						Goal goal = goals.get("All Tests");
						goals.put(Test.globalSubjects.get(i), goal);
					}
				}
			}
			
			this.setVisible(false);
			parent.setVisible(true, this);
			
			saveData();
		}
	}
	
	private void saveData() throws IOException
	{
		try 
		{
			File file = new File("Goals.dat");
			FileOutputStream FOS = new FileOutputStream("Goals.dat");
			ObjectOutputStream OOS = new ObjectOutputStream(FOS);
			OOS.writeObject(goals);
			OOS.close();
			FOS.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadData()
	{
		try
		{
			File file = new File("Goals.dat");
			FileInputStream FIS = new FileInputStream ("Goals.dat");
			ObjectInputStream OIS = new ObjectInputStream(FIS);
			goals = (HashMap<String, Goal>) OIS.readObject();;
			FIS.close();
			OIS.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean isError()
	{
		if (Double.parseDouble(spnScoreGoal.getValue().toString()) <= 0)
		{
			lblError.setText("Please Enter a Valid Goal Value");
			lblError.setVisible(true);
			return true;
		}
		else
		{
			lblError.setVisible(false);
			return false;
		}
	}
	
	public static void resetGoals()
	{
		if (!goals.containsKey("All Tests"))
		{
			goals.put("All Tests", new Goal(true, 80, "All Tests"));
		}
		
		for (int i = 0; i < Test.globalSubjects.size(); i++)
		{
			if (!goals.containsKey(Test.globalSubjects.get(i)))
			{
				if (goals.containsKey("All Tests"))
				{
					goals.put(Test.globalSubjects.get(i), goals.get("All Tests"));
				}
				else
				{
					goals.put(Test.globalSubjects.get(i), new Goal(true, 80, Test.globalSubjects.get(i)));
				}
			}
		}
	}

}
