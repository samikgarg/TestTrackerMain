package testtracker.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class TestDetails extends JFrame {

	private JPanel contentPane;
	private TestExplorer parent;
	private Test selectedTest;
	
	private JLabel lblTestName;
	private JLabel lblSubject;
	private JLabel lblScore;
	private JTextArea lblReflection;
	private JButton btnBack;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestDetails frame = new TestDetails();
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
	public TestDetails() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(TestExplorer.DARK_BLUE);
		contentPane.setForeground(new Color(51, 102, 102));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestNameTitle = new JLabel("Test Name:");
		lblTestNameTitle.setForeground(Color.WHITE);
		lblTestNameTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblTestNameTitle.setBounds(20, 61, 85, 16);
		contentPane.add(lblTestNameTitle);
		
		lblTestName = new JLabel("New label");
		lblTestName.setForeground(Color.WHITE);
		lblTestName.setBounds(116, 61, 328, 16);
		contentPane.add(lblTestName);
		
		lblSubject = new JLabel("New label");
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(114, 96, 328, 16);
		contentPane.add(lblSubject);
		
		JLabel lblSubjectTitle = new JLabel("Subject:");
		lblSubjectTitle.setForeground(Color.WHITE);
		lblSubjectTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblSubjectTitle.setBounds(42, 96, 61, 16);
		contentPane.add(lblSubjectTitle);
		
		lblScore = new JLabel("New label");
		lblScore.setForeground(Color.WHITE);
		lblScore.setBounds(116, 129, 328, 16);
		contentPane.add(lblScore);
		
		JLabel lblScoreTitle = new JLabel("Score:");
		lblScoreTitle.setForeground(Color.WHITE);
		lblScoreTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblScoreTitle.setBounds(61, 129, 44, 16);
		contentPane.add(lblScoreTitle);
		
		JLabel lblReflectionTitle = new JLabel("Reflection:");
		lblReflectionTitle.setForeground(Color.WHITE);
		lblReflectionTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblReflectionTitle.setBounds(29, 168, 76, 16);
		contentPane.add(lblReflectionTitle);
		
		lblReflection = new JTextArea();
		lblReflection.setBackground(TestExplorer.DARK_BLUE);
		lblReflection.setForeground(Color.WHITE);
		lblReflection.setBounds(114, 169, 319, 97);
		contentPane.add(lblReflection);
		
		btnBack = new JButton("< Back");
		btnBack.setForeground(new Color(255, 255, 255));
		btnBack.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				for (int i = 0; i < Test.getChangeLog().size(); i++)
				{
					goBack();
				}
			}
		});
		btnBack.setBorder( new LineBorder(Color.WHITE) );
		btnBack.setBorderPainted(false);
		btnBack.setBounds(6, 6, 67, 29);
		contentPane.add(btnBack);
	}
	
	public TestDetails (TestExplorer parent, Test selectedTest)
	{
		this();
		
		this.parent = parent;
		this.selectedTest = selectedTest;
		
		if (selectedTest instanceof MockExam)
		{
			lblTestName.setText("MOCK EXAM: " + selectedTest.getTestName());
		}
		else
		{
			lblTestName.setText(selectedTest.getTestName());
		}
		
		lblSubject.setText(selectedTest.getSubject());
		lblScore.setText(selectedTest.getScore() + "/" + selectedTest.getTotal() + " (" + selectedTest.testPercentage(false) + "%)");
		lblReflection.setText(selectedTest.getReflection());
		
		
	}
	
	private void goBack()
	{
		this.setVisible(false);
		parent.setVisible(rootPaneCheckingEnabled);
	}
}
