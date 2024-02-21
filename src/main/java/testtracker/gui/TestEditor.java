package testtracker.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.imageio.ImageIO;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class TestEditor extends JFrame {

	private JPanel contentPane;
	private JTextField txtTestName;
	private JLabel lblSubject;
	private JLabel lblMyMarks;
	private JLabel lblTotalMarks;
	private JTextField jtaReflection;
	private JComboBox cmbSubject;
	private JCheckBox chkMock;
	private JSpinner spnScore;
	private JSpinner spnTotal;
	private JLabel lblReflection;
	JLabel lblError;
	
	private TestExplorer parent;
	
	private Test targetTest;
	private Test oldTest;
	
	private boolean isNew;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestEditor frame = new TestEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TestEditor() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 363);
		contentPane = new JPanel();
		contentPane.setBackground(TestExplorer.DARK_BLUE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestName = new JLabel("Test Name");
		lblTestName.setForeground(Color.WHITE);
		lblTestName.setBounds(19, 6, 74, 16);
		contentPane.add(lblTestName);
		
		txtTestName = new JTextField();
		txtTestName.setBounds(19, 22, 147, 26);
		contentPane.add(txtTestName);
		txtTestName.setColumns(10);
		txtTestName.setBackground(TestExplorer.LIGHT_BLUE);
		
		lblSubject = new JLabel("Subject");
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(19, 60, 74, 16);
		contentPane.add(lblSubject);
		
		lblMyMarks = new JLabel("My Marks");
		lblMyMarks.setForeground(Color.WHITE);
		lblMyMarks.setBounds(19, 119, 74, 16);
		contentPane.add(lblMyMarks);
		
		lblTotalMarks = new JLabel("Total Marks");
		lblTotalMarks.setForeground(Color.WHITE);
		lblTotalMarks.setBounds(19, 177, 74, 16);
		contentPane.add(lblTotalMarks);
		
		cmbSubject = new JComboBox();
		cmbSubject.setEditable(true);
		cmbSubject.setBounds(19, 79, 147, 27);
		cmbSubject.setBackground(TestExplorer.LIGHT_BLUE);
		contentPane.add(cmbSubject);
		
		cmbSubject.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, TestExplorer.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		((JTextField) cmbSubject.getEditor().getEditorComponent()).setBackground(TestExplorer.LIGHT_BLUE);
        
		
		
		DefaultComboBoxModel DCMB = new DefaultComboBoxModel();
		for (int i = 0; i < Test.globalSubjects.size(); i++)
		{
			DCMB.addElement(Test.globalSubjects.get(i));
		}
		
		cmbSubject.setModel(DCMB);
		
		spnScore = new JSpinner();
		spnScore.setBounds(19, 139, 147, 26);
		spnScore.setBackground(TestExplorer.LIGHT_BLUE);
		contentPane.add(spnScore);
		JSpinner.NumberEditor jsEditor1 = (JSpinner.NumberEditor) spnScore.getEditor(); 
		jsEditor1.getTextField().setBackground(TestExplorer.LIGHT_BLUE);
		
		spnTotal = new JSpinner();
		spnTotal.setBounds(19, 195, 147, 26);
		spnTotal.setBackground(TestExplorer.LIGHT_BLUE);
		contentPane.add(spnTotal);
		JSpinner.NumberEditor jsEditor2 = (JSpinner.NumberEditor) spnTotal.getEditor(); 
		jsEditor2.getTextField().setBackground(TestExplorer.LIGHT_BLUE);
		
		chkMock = new JCheckBox("Mock Exam");
		chkMock.setForeground(Color.WHITE);
		//chkMock.setBackground(Color.BLACK);
		//chkMock.setOpaque(true);
		//chkMock.setIcon(new ImageIcon("light_blue.png"));
		chkMock.setBounds(19, 233, 128, 23);
		contentPane.add(chkMock);
		
		lblReflection = new JLabel("Reflection");
		lblReflection.setForeground(Color.WHITE);
		lblReflection.setBounds(200, 6, 89, 16);
		contentPane.add(lblReflection);
		
		jtaReflection = new JTextField();
		jtaReflection.setBounds(200, 22, 244, 234);
		jtaReflection.setBackground(TestExplorer.LIGHT_BLUE);
		contentPane.add(jtaReflection);
		jtaReflection.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.setForeground(new Color(255, 255, 255));
		btnSave.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				saveTest();
			}
		});
		btnSave.setBorder( new LineBorder(Color.WHITE) );
		btnSave.setBounds(355, 300, 89, 29);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setForeground(new Color(255, 255, 255));
		btnCancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				cancel();
			}
		});
		btnCancel.setBorder( new LineBorder(Color.WHITE) );
		btnCancel.setBounds(254, 300, 89, 29);
		contentPane.add(btnCancel);
		
		lblError = new JLabel("New label");
		lblError.setForeground(Color.RED);
		lblError.setBounds(19, 272, 425, 16);
		contentPane.add(lblError);
		
		lblError.setVisible(false);
	}
	
	public TestEditor (TestExplorer parent) throws IOException
	{
		this();
		this.parent = parent;
		isNew = true;
		
		if (Test.globalSubjects.contains(parent.cmbSubjectFilter.getSelectedItem().toString()))
		{
			cmbSubject.setSelectedItem(parent.cmbSubjectFilter.getSelectedItem().toString());
		}
	}
	
	public TestEditor (TestExplorer parent, Test targetTest) throws IOException
	{
		this (parent);
		this.targetTest = targetTest;
		isNew = false;
		
		if (targetTest != null)
		{
			txtTestName.setText(targetTest.getTestName());
			cmbSubject.setSelectedItem(targetTest.getSubject());
			spnScore.setValue(targetTest.getScore());
			spnTotal.setValue(targetTest.getTotal());
			jtaReflection.setText(targetTest.getReflection());
			chkMock.setSelected(targetTest instanceof MockExam);
			
			oldTest = new Test(targetTest.getTestName(), targetTest.getScore(), targetTest.getReflection(), targetTest.getTotal(), targetTest.getSubject(), targetTest.getID(), parent.getTests());
		}
		
		
	}
	
	public void cancel ()
	{
		TestExplorer.isNewSave = false;
		TestExplorer.isEditSave = false;
		
		this.setVisible(false);
		parent.setVisible(true);
	}
	
	private void saveTest()
	{
		if (!isError())
		{
			
			if (isNew)
			{
				TestExplorer.isNewSave = true;
			}
			else
			{
				TestExplorer.isEditSave = true;
			}
			
			if (targetTest ==  null)
			{
				if (chkMock.isSelected())
				{
					targetTest = new MockExam((String) txtTestName.getText(), Integer.parseInt(spnScore.getValue().toString()), (String) jtaReflection.getText(), Integer.parseInt(spnTotal.getValue().toString()), (String) cmbSubject.getSelectedItem(), 0, parent.getTests());
				}
				else
				{
					targetTest = new Test((String) txtTestName.getText(), Integer.parseInt(spnScore.getValue().toString()), (String) jtaReflection.getText(), Integer.parseInt(spnTotal.getValue().toString()), (String) cmbSubject.getSelectedItem(), 0, parent.getTests());
				}
				
				parent.storeTest(targetTest);
				
				parent.undoNewTests.add(targetTest);
				parent.isNewRedo = false;
				
				this.setVisible(false);
				parent.setVisible(true);
			}
			else
			{
				targetTest.setTestName(txtTestName.getText());
				targetTest.setScore(Integer.parseInt(spnScore.getValue().toString()));
				targetTest.setTotal(Integer.parseInt(spnTotal.getValue().toString()));
				targetTest.setReflection(jtaReflection.getText());
				targetTest.setSubject((String) cmbSubject.getSelectedItem());
				
				if (chkMock.isSelected() != targetTest instanceof MockExam)
				{
					parent.mockToggle(targetTest, false, false, true);
				}
				
				Test targetTestClone = new Test(targetTest.getTestName(), targetTest.getScore(), targetTest.getReflection(), targetTest.getTotal(), targetTest.getSubject(), targetTest.getID(), parent.getTests()); 
				
				Test[] undoTests = {oldTest, targetTestClone};
				parent.undoEdits.add(undoTests);
				parent.isEditRedo = false;
				
				this.setVisible(false);
				parent.setVisible(true);
			}
		}
	}
	
	private boolean isError()
	{
		if (Integer.parseInt(spnTotal.getValue().toString()) <= 0)
		{
			lblError.setText("Please Enter a Valid Total Value");
			lblError.setVisible(true);
			return true;
		}
		else if (Integer.parseInt(spnScore.getValue().toString()) < 0)
		{
			lblError.setText("Please Enter a Valid Score Value");
			lblError.setVisible(true);
			return true;
		}
		else if (Integer.parseInt(spnTotal.getValue().toString()) < Integer.parseInt(spnScore.getValue().toString()))
		{
			lblError.setText("Please Ensure Total is greater than Score");
			lblError.setVisible(true);
			return true;
		}
		else if (txtTestName.getText().equals(""))
		{
			lblError.setText("Please Enter a Test Name");
			lblError.setVisible(true);
			return true;
		}
		else if (cmbSubject.getSelectedItem().equals(""))
		{
			lblError.setText("Please Enter a Subject");
			lblError.setVisible(true);
			return true;
		}
		else if (jtaReflection.getText().equals(""))
		{
			lblError.setText("Please Enter a Reflection");
			lblError.setVisible(true);
			return true;
		}
		else
		{
			try 
			{	int i = Integer.parseInt(spnTotal.getValue().toString());
			
				try
				{
					int k = Integer.parseInt(spnScore.getValue().toString());
					lblError.setVisible(false);
					return false;
				}
				catch (NumberFormatException nfe)
				{
					lblError.setText("Please Enter a valid Score Value");
					lblError.setVisible(true);
					return true;
				}
			}
			catch (NumberFormatException nfe)
			{
				lblError.setText("Please Enter a valid Total Value");
				lblError.setVisible(true);
				return true;
			}
		}
	}
}
