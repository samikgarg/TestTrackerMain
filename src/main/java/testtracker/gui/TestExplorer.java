package testtracker.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JToggleButton;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.JTable;
import java.awt.Canvas;
import java.awt.Button;

public class TestExplorer extends JFrame {

	private ArrayList<Test> tests = new ArrayList<Test>();
	private ArrayList<Test> filteredTests = new ArrayList<Test>();
	
	public ArrayList<Test> undoNewTests = new ArrayList<Test>();
	public ArrayList<Test> redoNewTests = new ArrayList<Test>();
	
	private ArrayList<Test> undoDeletes = new ArrayList<Test>();
	private ArrayList<Test> redoDeletes = new ArrayList<Test>();
	
	public ArrayList<Test[]> undoEdits = new ArrayList<Test[]>();
	public ArrayList<Test[]> redoEdits = new ArrayList<Test[]>();
	
	private ArrayList<Integer> undoMocks = new ArrayList<Integer>();
	private ArrayList<Integer> redoMocks = new ArrayList<Integer>();
	
	private JPanel contentPane;
	public JComboBox cmbSubjectFilter;
	private JPanel pnlChart;
	private JComboBox<String> cmbSort;
	private JComboBox cmbGraphType;
	
	BufferedImage img;
	
	public static Color LIGHT_BLUE = new Color(192, 234, 252);
	public static Color DARK_BLUE = new Color(1, 21, 61);
	
	Goals goals;
	AnalysisWindow aw;
	
	final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	
	private JScrollPane scrollPane;
	private Canvas canvasAnalyse;
	
	JButton btnUndo;
	JButton btnRedo;
	
	ArrayList<Integer> edits = new ArrayList<Integer>();
	ArrayList<Integer> redos = new ArrayList<Integer>();
	
	public static boolean isNewSave;
	public static boolean isEditSave;
	
	public boolean isNewUndo;
	public boolean isNewRedo;
	public boolean isEditUndo;
	public boolean isEditRedo;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					TestExplorer frame = new TestExplorer();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestExplorer() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1120, 516);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBackground(new Color(1, 24, 61));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTests = new JLabel("Tests");
		lblTests.setForeground(new Color(255, 255, 255));
		lblTests.setBounds(28, 20, 61, 16);
		contentPane.add(lblTests);
		
		cmbSubjectFilter = new JComboBox();
		cmbSubjectFilter.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) 
			{
				buildTestList();
				drawChart();
			}
		});
		cmbSubjectFilter.setEditable(true);
		cmbSubjectFilter.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				buildTestList();
				drawChart();
			}
		});
		cmbSubjectFilter.setBounds(82, 16, 188, 27);
		contentPane.add(cmbSubjectFilter);
		
		JButton btnShowLog = new JButton("Show Log");
		btnShowLog.setForeground(new Color(255, 255, 255));
		btnShowLog.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				for (int i = 0; i < Test.getChangeLog().size(); i++)
				{
					System.out.println(Test.getChangeLog().get(i));
				}
			}
		});
		btnShowLog.setBorder( new LineBorder(Color.WHITE) );
		btnShowLog.setBounds(840, 420, 274, 44);
		contentPane.add(btnShowLog);
		btnShowLog.setVisible(true);
		
		JButton btnAnalyse = new JButton("Analyse");
		btnAnalyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				openAnalyse();
			}
		});
		btnAnalyse.setForeground(new Color(255, 255, 255));
		btnAnalyse.setBorder( new LineBorder(Color.WHITE) );
		btnAnalyse.setBounds(840, 364, 270, 44);
		contentPane.add(btnAnalyse);
		btnAnalyse.setVisible(true);
		
		JButton btnGoals = new JButton("Goals");
		btnGoals.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				openGoals();
			}
		});
		btnGoals.setForeground(new Color(255, 255, 255));
		btnGoals.setBorder( new LineBorder(Color.WHITE) );
		btnGoals.setBounds(840, 275, 270, 44);
		btnGoals.setBounds(840, 308, 274, 44);
		contentPane.add(btnGoals);
		
		JButton btnAdd = new JButton("+");
		btnAdd.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		btnAdd.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try {
					newTest();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnAdd.setBorder( new LineBorder(Color.WHITE) );
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setBounds(797, 21, 29, 29);
		contentPane.add(btnAdd);
		
		pnlChart = new JPanel();
		pnlChart.setBackground(new Color(0, 0, 51));
		pnlChart.setBounds(840, 62, 274, 213);
		contentPane.add(pnlChart);
		pnlChart.setLayout(new BorderLayout(0, 0));
		
		cmbSort = new JComboBox();
		cmbSort.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				buildTestList();
				drawChart();
			}
		});
		cmbSort.setBounds(336, 16, 274, 27);
		contentPane.add(cmbSort);
		
		DefaultComboBoxModel<String> DCMB = new DefaultComboBoxModel();
		DCMB.addElement("Percentage Score (Highest to Lowest)");
		DCMB.addElement("Percentage Score (Lowest to Highest)");
		DCMB.addElement("Test Name (A to Z)");
		DCMB.addElement("Test Name (Z to A)");
		DCMB.addElement("Subject (A to Z)");
		DCMB.addElement("Subject (Z to A)");
		DCMB.addElement("Date Added (Oldest to Newest)");
		DCMB.addElement("Date Added (Newest to Oldest)");
		cmbSort.setModel(DCMB);
		
		JLabel lbSortLabel = new JLabel("Sort");
		lbSortLabel.setForeground(new Color(255, 255, 255));
		lbSortLabel.setBounds(302, 20, 33, 16);
		contentPane.add(lbSortLabel);
		
		cmbGraphType = new JComboBox();
		cmbGraphType.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				drawChart();
			}
		});
		cmbGraphType.setBounds(926, 16, 188, 27);
		contentPane.add(cmbGraphType);
		
		JLabel lblGraphType = new JLabel("Graph Type");
		lblGraphType.setForeground(new Color(255, 255, 255));
		lblGraphType.setBounds(840, 20, 75, 16);
		contentPane.add(lblGraphType);
		
		scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(153, 255, 255));
		
		table = new JTable();
		
		scrollPane.setBounds(16, 62, 812, 402);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(table);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		isNewUndo = false;
		isNewRedo = false;
		isEditUndo = false;
		isEditRedo = false;
		
		Canvas canvasUndo = new Canvas()
		{
			@Override
	        public void paint(Graphics g) 
			{
				try 
				{
					img = ImageIO.read(getClass().getResource("undo.png"));
					img = scale(img, getWidth(), getHeight());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				super.paint(g);
	            if (img != null) {
	                int x = (getWidth() - img.getWidth()) / 2;
	                int y = (getHeight() - img.getHeight()) / 2;
	                g.drawImage(img, x, y, this);
	            }
	        }
		};
		canvasUndo.setBounds(745, 20, 29, 29);
		contentPane.add(canvasUndo);
		
		canvasUndo.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				if (edits.size() > 0)
				{
					if (edits.get(edits.size() - 1) == 0 && undoNewTests.size() > 0)
					{
						Test targetTest = undoNewTests.get(undoNewTests.size() - 1);
						
						redoNewTests.add(undoNewTests.get(undoNewTests.size() - 1));
						
						ArrayList<Test> newList = new ArrayList<Test>();
						
						for (int i = 0; i < undoNewTests.size() - 1; i++)
						{
							newList.add(undoNewTests.get(i));
						}
						
						undoNewTests = newList;
						
						int currSelectedRow = table.getSelectedRow();
						int row = 0;
						for (int i = 0; i < table.getRowCount(); i++)
						{
							if ((int)table.getModel().getValueAt(i,8) == targetTest.getID())
							{
								row = i;
								break;
							}
						}
						table.setRowSelectionInterval(row, row);
						deleteTest(false, true);
						if (currSelectedRow != -1)
						{
							table.setRowSelectionInterval(currSelectedRow, currSelectedRow);
						}
						
						redos.add(0);
						removeEdit();
						removeEdit();
					}
					else if (edits.get(edits.size() - 1) == 1 && undoDeletes.size() > 0)
					{
						Test deletedTest = undoDeletes.get(undoDeletes.size() - 1);

						ArrayList<Test> newList = new ArrayList<Test>();
						
						for (int i = 0; i < undoDeletes.size() - 1; i++)
						{
							newList.add(undoDeletes.get(i));
						}
						
						undoDeletes = newList;
						
						String subject = deletedTest.getSubject();
						boolean isMoreSubject = false;
						
						for (int i = 0; i < Test.globalSubjects.size(); i++)
						{
							if (Test.globalSubjects.get(i).equals(subject))
							{
								System.out.println("HERE***#*#*#**#*#*#*#*");
								isMoreSubject = true;
								break;
							}
						}
						
						if (!isMoreSubject)
						{
							System.out.println("HERE***#*#*#**#*#*#*#*");
							Test.globalSubjects.add(subject);
						}
						
						tests.add(deletedTest);
						
						redos.add(edits.get(edits.size() - 1));
						removeEdit();
					}
					else if (edits.get(edits.size() - 1) == 2 && undoEdits.size() > 0)
					{
						Test[] targetTestArr = undoEdits.get(undoEdits.size() - 1);
						Test oldTest = targetTestArr[0];
						Test edittedTest = targetTestArr[1];
						
						int index = -1;
						
						for (int i = 0; i < tests.size(); i++)
						{
							if (tests.get(i).getID() == oldTest.getID())
							{
								index = i;
							}
						}
						
						tests.set(index, oldTest);
						
						ArrayList<Test[]> newList = new ArrayList<Test[]>();
						
						for (int i = 0; i < undoEdits.size() - 1; i++)
						{
							newList.add(undoEdits.get(i));
						}
						
						undoEdits = newList;
						
						redos.add(edits.get(edits.size() - 1));
						removeEdit();
					}
					else if (edits.get(edits.size() - 1) == 3 && undoMocks.size() > 0)
					{
						//Test currTest = undoMocks.get(undoMocks.size() - 1);
						Test currTest = getTestUsingID(undoMocks.get(undoMocks.size() - 1));
						mockToggle(currTest, true, false, false);
						
						ArrayList<Integer> newList = new ArrayList<Integer>();
						
						for (int i = 0; i < undoMocks.size() - 1; i++)
						{
							newList.add(undoMocks.get(i));
						}
						
						undoMocks = newList;
						
						redos.add(edits.get(edits.size() - 1));
						removeEdit();
					}
					
					System.out.println("HEREADHAIHDAHSDI");
					resetSubjectFilter();
					buildTestList();
					drawChart();
					
				}
				
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {

			        int row = table.getSelectedRow();
			        int col = table.getSelectedColumn();
			        
			        if (table.getSelectedRow() != -1)
			        {
			        	System.out.println("Row: " + row + ", Column: " + col + ", " + table.getModel().getValueAt(table.getSelectedRow(),8));
			        }
			       
			        if (col == 6 && row != filteredTests.size())
			        {
			        	try {
							editTest();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
			        }
			        else if (col == 7 && row != filteredTests.size())
			        {
			        	deleteTest(false, false);
			        }
			        else if (col == 5 && row != filteredTests.size())
			        {
			        	int id = (int) table.getModel().getValueAt(table.getSelectedRow(),8);
			        	mockToggle(getTestUsingID((id)), false, false, false);
			        }

			        else if (row != filteredTests.size() && table.getSelectedRow() != -1)
			        {
			        	int id = (int) table.getModel().getValueAt(table.getSelectedRow(),8);
			        	Test selectedTest = getTestUsingID(id);
			        	openDetails(selectedTest);
			        }
			      }
			    });
		
		table.addFocusListener(new FocusAdapter() 
		{
		    @Override
		    public void focusLost(FocusEvent e) 
		    {
		        TableCellEditor tce = table.getCellEditor();
		        if(tce != null)
		        {
		        	tce.addCellEditorListener(table);
		        }
		    }
		});
		
		cmbSubjectFilter.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		
		cmbSort.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		
		cmbGraphType.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		
		cmbSort.setBackground(LIGHT_BLUE);
		cmbGraphType.setBackground(LIGHT_BLUE);
		
		loadData();
		
		Goals.loadData();
		
		Goals.resetGoals();
		
		resetSubjectFilter();
		
		buildTestList();
		
		drawChart();
	}
	
	//Code Source: https://stackoverflow.com/questions/9417356/bufferedimage-resize
	public static BufferedImage scale(BufferedImage src, int w, int h)
	{
	    BufferedImage img = 
	            new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    int x, y;
	    int ww = src.getWidth();
	    int hh = src.getHeight();
	    int[] ys = new int[h];
	    for (y = 0; y < h; y++)
	        ys[y] = y * hh / h;
	    for (x = 0; x < w; x++) {
	        int newX = x * ww / w;
	        for (y = 0; y < h; y++) {
	            int col = src.getRGB(newX, ys[y]);
	            img.setRGB(x, y, col);
	        }
	    }
	    return img;
	}
	
	
	@Override
	public void setVisible (boolean visible)
	{
		if (visible)
		{
			resetSubjectFilter();
			buildTestList();
			drawChart();
			saveData();
			
			if (isEditSave)
			{
				edits.add(edits.size(), 2);
				isEditSave = false;
				clearRedos();
			}
			else if (isNewSave)
			{
				edits.add(edits.size(), 0);
				isNewSave = false;
				if (!isNewRedo)
				{
					clearRedos();
				}
			}
		}
		
		super.setVisible(visible);
		
		Goals.resetGoals();
	}
	
	public void setVisible (boolean visible, Goals goals)
	{
		this.goals = goals;
		this.setVisible(visible);
	}
	
	public void setVisible (boolean visible, AnalysisWindow aw)
	{
		this.aw = aw;
		this.setVisible(visible);
	}
	
	public void resetSubjectFilter()
	{
		DefaultComboBoxModel DCMB = new DefaultComboBoxModel();
		DCMB.addElement("All Tests");
		
		for (int i = 0; i < Test.globalSubjects.size(); i++)
		{
			DCMB.addElement(Test.globalSubjects.get(i));
		}
		
		JTextField text = ((JTextField) cmbSubjectFilter.getEditor().getEditorComponent());
        text.setBackground(LIGHT_BLUE);
		
		cmbSubjectFilter.setModel(DCMB);
	}
	
	public void buildTestList()
	{
		
		if(cmbSubjectFilter.getEditor().getItem().toString().equals("All Tests"))
		{
			filteredTests = (ArrayList<Test>)tests.clone();
		}
		else
		{
			filteredTests = new ArrayList<Test>();
			String searchKey = cmbSubjectFilter.getEditor().getItem().toString().trim().toLowerCase();
			
			for (int i = 0; i < tests.size(); i++)
			{
				if (tests.get(i).toString().toLowerCase().contains(searchKey))
				{
					filteredTests.add(tests.get(i));
				}
			}
		}
		
		switch (cmbSort.getSelectedIndex())
		{
		case 0:
			boolean searching = true;
			
			while (searching)
			{
				searching = false;
				for (int i = 0; i < filteredTests.size()-1; i++)
				{
					if (filteredTests.get(i).testPercentage (false) < filteredTests.get(i + 1).testPercentage(false))
					{
						Test tempTest = filteredTests.get(i);
						filteredTests.set(i, filteredTests.get(i+1));
						filteredTests.set(i+1, tempTest);
						searching = true;
						
					}
				}
			}
			
			break;
			
		case 1:
			searching = true;
			
			while (searching)
			{
				searching = false;
				for (int i = 0; i < filteredTests.size()-1; i++)
				{
					if (filteredTests.get(i).testPercentage (false) > filteredTests.get(i + 1).testPercentage(false))
					{
						Test tempTest = filteredTests.get(i);
						filteredTests.set(i, filteredTests.get(i+1));
						filteredTests.set(i+1, tempTest);
						searching = true;
						
					}
				}
			}
			
			break;
			
		case 2:
			searching = true;
			
			while (searching)
			{
				searching = false;
				for (int i = 0; i < filteredTests.size()-1; i++)
				{
					int currChar = 0;
					while (true)
					{
						if (currChar < filteredTests.get(i).getTestName().length() && currChar < filteredTests.get(i+1).getTestName().length())
						{
							if (filteredTests.get(i).getTestName().toLowerCase().charAt(0) != filteredTests.get(i+1).getTestName().toLowerCase().charAt(currChar))
							{
								break;
							}
							else
							{
								currChar++;
							}
						}
						else
						{
							break;
						}
					}
					
					if (alphabet.indexOf(filteredTests.get(i).getTestName().toLowerCase().charAt(currChar)) > alphabet.indexOf(filteredTests.get(i+1).getTestName().toLowerCase().charAt(currChar)))
					{
						Test tempTest = filteredTests.get(i);
						filteredTests.set(i, filteredTests.get(i+1));
						filteredTests.set(i+1, tempTest);
						searching = true;
						
					}
				}
			}
			
			break;
			
		case 3:
			searching = true;
			
			while (searching)
			{
				searching = false;
				for (int i = 0; i < filteredTests.size()-1; i++)
				{
					int currChar = 0;
					while (true)
					{
						if (currChar < filteredTests.get(i).getTestName().length() && currChar < filteredTests.get(i+1).getTestName().length())
						{
							if (filteredTests.get(i).getTestName().toLowerCase().charAt(0) != filteredTests.get(i+1).getTestName().toLowerCase().charAt(currChar))
							{
								break;
							}
							else
							{
								currChar++;
							}
						}
						else
						{
							break;
						}
					}
					
					if (alphabet.indexOf(filteredTests.get(i).getTestName().toLowerCase().charAt(currChar)) < alphabet.indexOf(filteredTests.get(i+1).getTestName().toLowerCase().charAt(currChar)))
					{
						Test tempTest = filteredTests.get(i);
						filteredTests.set(i, filteredTests.get(i+1));
						filteredTests.set(i+1, tempTest);
						searching = true;
						
					}
				}
			}
			
			break;
			
		case 4:
			searching = true;
			
			while (searching)
			{
				searching = false;
				for (int i = 0; i < filteredTests.size()-1; i++)
				{
					int currChar = 0;
					while (true)
					{
						if (currChar < filteredTests.get(i).getSubject().length() && currChar < filteredTests.get(i+1).getSubject().length())
						{
							if (filteredTests.get(i).getSubject().toLowerCase().charAt(0) != filteredTests.get(i+1).getSubject().toLowerCase().charAt(currChar))
							{
								break;
							}
							else
							{
								currChar++;
							}
						}
						else
						{
							break;
						}
					}
					
					if (alphabet.indexOf(filteredTests.get(i).getSubject().toLowerCase().charAt(currChar)) > alphabet.indexOf(filteredTests.get(i+1).getSubject().toLowerCase().charAt(currChar)))
					{
						Test tempTest = filteredTests.get(i);
						filteredTests.set(i, filteredTests.get(i+1));
						filteredTests.set(i+1, tempTest);
						searching = true;
						
					}
				}
			}
			
			break;
			
		case 5:
			searching = true;
			
			while (searching)
			{
				searching = false;
				for (int i = 0; i < filteredTests.size()-1; i++)
				{
					int currChar = 0;
					while (true)
					{
						if (currChar < filteredTests.get(i).getSubject().length() && currChar < filteredTests.get(i+1).getSubject().length())
						{
							if (filteredTests.get(i).getSubject().toLowerCase().charAt(0) != filteredTests.get(i+1).getSubject().toLowerCase().charAt(currChar))
							{
								break;
							}
							else
							{
								currChar++;
							}
						}
						else
						{
							break;
						}
					}
					
					if (alphabet.indexOf(filteredTests.get(i).getSubject().toLowerCase().charAt(0)) < alphabet.indexOf(filteredTests.get(i+1).getSubject().toLowerCase().charAt(0)))
					{
						Test tempTest = filteredTests.get(i);
						filteredTests.set(i, filteredTests.get(i+1));
						filteredTests.set(i+1, tempTest);
						searching = true;
						
					}
				}
			}
			
			break;
			
		case 6:
			searching = true;
			
			while (searching)
			{
				searching = false;
				for (int i = 0; i < filteredTests.size()-1; i++)
				{
					if (filteredTests.get(i).getID() > filteredTests.get(i+1).getID())
					{
						Test tempTest = filteredTests.get(i);
						filteredTests.set(i, filteredTests.get(i+1));
						filteredTests.set(i+1, tempTest);
						searching = true;
						
					}
				}
			}
			
			break;
			
		case 7:
			searching = true;
			
			while (searching)
			{
				searching = false;
				for (int i = 0; i < filteredTests.size()-1; i++)
				{
					if (filteredTests.get(i).getID() < filteredTests.get(i+1).getID())
					{
						Test tempTest = filteredTests.get(i);
						filteredTests.set(i, filteredTests.get(i+1));
						filteredTests.set(i+1, tempTest);
						searching = true;
						
					}
				}
			}
			
			break;
		}
		
		Object[][] tableData = new Object[filteredTests.size()][9];
		
		for (int i = 0; i < filteredTests.size(); i++)
		{
			tableData[i][0] = filteredTests.get(i).getTestName();
			tableData[i][1] = filteredTests.get(i).getSubject();
			tableData[i][2] = filteredTests.get(i).getScore() + "";
			tableData[i][3] = filteredTests.get(i).getTotal() + "";
			tableData[i][4] = filteredTests.get(i).testPercentage(false) + "%";
			//tableData[i][5] = filteredTests.get(i).getReflection();
			tableData[i][5] = filteredTests.get(i) instanceof MockExam;
			tableData[i][6] = "✎";
			tableData[i][7] = "␡";
			tableData[i][8] = filteredTests.get(i).getID();
			
		}
		
		DefaultTableCellRenderer editRenderer = new DefaultTableCellRenderer() 
		{
		    Font font = new Font("SansSerif", Font.PLAIN, 20);;

		    @Override
		    public Component getTableCellRendererComponent(JTable table,
		            Object value, boolean isSelected, boolean hasFocus,
		            int row, int column) {
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
		                row, column);
		        setFont(font);
		        return this;
		    }

		};
		
		DefaultTableCellRenderer deleteRenderer = new DefaultTableCellRenderer() 
		{
		    Font font = new Font("SansSerif", Font.PLAIN, 40);;

		    @Override
		    public Component getTableCellRendererComponent(JTable table,
		            Object value, boolean isSelected, boolean hasFocus,
		            int row, int column) {
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
		                row, column);
		        setFont(font);
		        return this;
		    }

		};
		
		String[] colHeadings = {"Test", "Subject", "Score", "Total", "Percentage Score", "Mock", "", "", "ID"};
		
		DefaultTableModel model = new DefaultTableModel(tableData, colHeadings)
        {
            public Class getColumnClass(int column)
            {
            	return getValueAt(0, column).getClass();
            }
            
            @Override
            public boolean isCellEditable(int row, int column) 
            {
            	if (column == 5)
            	{
            		return false;
            	}
            	else
            	{
            		return false;
            	}
            }
        };
        
        
        table.setModel(model);

		
		table.getColumnModel().getColumn(0).setMinWidth(200);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setMaxWidth(70);
		table.getColumnModel().getColumn(3).setMaxWidth(70);
		table.getColumnModel().getColumn(4).setMinWidth(70);
		table.getColumnModel().getColumn(5).setMaxWidth(40);
		table.getColumnModel().getColumn(6).setMaxWidth(30);
		table.getColumnModel().getColumn(7).setMaxWidth(50);
		table.getColumnModel().getColumn(8).setMaxWidth(0);
		
		table.removeColumn(table.getColumnModel().getColumn(8));
		
		table.getColumnModel().getColumn(6).setCellRenderer(editRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(deleteRenderer);
		
		table.setBackground(LIGHT_BLUE);
		table.setGridColor(LIGHT_BLUE);
		table.setFillsViewportHeight(true);
		MatteBorder border = new MatteBorder(1, 1, 0, 0, DARK_BLUE);
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(DARK_BLUE);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setPreferredSize(
			     new Dimension(scrollPane.getWidth(), 30)
				);
		
		table.setBorder(border);
		scrollPane.setBorder(null);
		
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultRenderer(String.class, new BoardTableCellRenderer());
		
		table.setRowHeight(30);
		
		
		DefaultComboBoxModel DCMBGraph = new DefaultComboBoxModel();
		DCMBGraph.addElement("Bar Graph");
		if (filteredTests.size() > 1)
		{
			DCMBGraph.addElement("Line Graph");
		}
		cmbGraphType.setModel(DCMBGraph);
	}
	
	public void newTest() throws IOException
	{
		TestEditor TE = new TestEditor (this);
		TE.setVisible(true);
		this.setVisible(false);
	}
	
	public void storeTest (Test newTest)
	{
		tests.add(newTest);
	}
	
	public void deleteTest(boolean isUndo, boolean isRedo)
	{
		if (filteredTests.size() > 1)
		{
			Test targetTest = filteredTests.get(table.getSelectedRow());
			tests.remove(targetTest);
			
			String subject = targetTest.getSubject();
			boolean isMoreSubject = false;
			
			for (int i = 0; i < tests.size(); i++)
			{
				Test currTest = tests.get(i);
				
				if (currTest.getSubject().equals(subject))
				{
					isMoreSubject = true;
					break;
				}
			}
			
			if (!isMoreSubject)
			{
				Test.globalSubjects.remove(subject);
			}
			
			resetSubjectFilter();
			buildTestList();
			saveData();
			
			edits.add(edits.size(), 1);
			
			if (!isRedo)
			{
				undoDeletes.add(targetTest);
				clearRedos();
			}
		}
	}
	
	private void saveData()
	{
		try
		{
			FileOutputStream FOS = new FileOutputStream("TestsData.dat");
			ObjectOutputStream OOS = new ObjectOutputStream(FOS);
			OOS.writeObject(tests);
			OOS.writeObject(Test.getGlobalSubjects());
			OOS.writeObject(Test.getChangeLog());
			OOS.close();
			FOS.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadData()
	{
		try
		{
			File file = new File("TestsData.dat");
			FileInputStream FIS = new FileInputStream ("TestsData.dat");
			ObjectInputStream OIS = new ObjectInputStream(FIS);
			tests = (ArrayList<Test>)OIS.readObject();
			Test.setGlobalSubjects((ArrayList<String>)OIS.readObject());
			Test.setChangeLog((ArrayList<String>)OIS.readObject());
			FIS.close();
			OIS.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			tests.add(new Test("Demand and Supply", 10, "Forgot Graph Titles and Axis Titles'", 15, "Economics", 0, tests));
			tests.add(new Test("Calculus", 99, "Made a Careless Mistake", 100, "Maths", 0, tests));
			tests.add(new Test("Multivariable Calculus", 47, "Could have done Better'", 100, "Maths", 0, tests));
			tests.add(new Test("Quantum Mechanics", 35, "Forgot Units a Lot'", 50, "Physics", 0, tests));
			tests.add(new Test("Java", 9, "Forgot a Semicolon'", 10, "Computer Science", 0, tests));
			tests.add(new Test("C++", 7, "Not very Happy with the Result", 10, "Computer Science", 0, tests));
			tests.add(new MockExam("Python", 80, "Accidentally put Semicolons'", 100, "Computer Science", 0, tests));
			tests.add(new MockExam("Human Body", 100, "Very Happy with the Results", 100, "Biology", 0, tests));
			tests.add(new Test("Bonding", 4, "Test was Very Difficult", 10, "Chemistry", 0, tests));
			tests.add(new Test("Chemical Reactions", 5, "Slightly Better than Before", 10, "Chemistry", 0, tests));
		}
	}
	
	public void drawChart()
	{
		DefaultCategoryDataset datasetBar = new DefaultCategoryDataset();
		DefaultCategoryDataset datasetLine = new DefaultCategoryDataset();
		JFreeChart chart = null;
		
		for (int i = 0; i < filteredTests.size(); i++)
		{
			datasetLine.addValue(filteredTests.get(i).testPercentage(false), "series1", filteredTests.get(i).getTestName());
			datasetBar.addValue(filteredTests.get(i).testPercentage(false), filteredTests.get(i).getTestName(), "");
		}
		
		switch (cmbGraphType.getSelectedIndex())
		{
		case 0:
			
			chart = ChartFactory.createBarChart
					(
							"Test Results " + cmbSubjectFilter.getSelectedItem().toString(),
							"Tests",
							"Result",
							datasetBar,
							PlotOrientation.VERTICAL,
							false,
							false,
							false
					);
			
			break;
			
		case 1:
			
			chart = ChartFactory.createLineChart
					(
							"Test Results " + cmbSubjectFilter.getSelectedItem().toString(),
							"Tests",
							"Result",
							datasetLine,
							PlotOrientation.VERTICAL,
							false,
							false,
							false
					);
			break;
			
			 
		}
		
		
		
		pnlChart.removeAll();
		pnlChart.setLayout(new BorderLayout());
		ChartPanel chartPanel = new ChartPanel(chart)
		{
			@Override
			public Dimension getPreferredSize() {
			    // given some values of w & h
			    return new Dimension(pnlChart.getWidth(), pnlChart.getHeight());
			}
		};
		chartPanel.setBackground(new Color(0, 0, 51));
		
		chart.getTitle().setPaint(Color.WHITE);
		
		Plot plot = chart.getPlot();
		plot.setBackgroundPaint(new Color(1, 24, 61));
		
		chart.setBackgroundPaint(new Color(1, 24, 61));
		
		CategoryPlot cp = (CategoryPlot) plot;
		cp.getDomainAxis().setTickLabelsVisible(false);
		
		if (plot instanceof CategoryPlot) {
	        setAxisFontColor(((CategoryPlot) plot).getDomainAxis(), Color.WHITE);
	        setAxisFontColor(((CategoryPlot) plot).getRangeAxis(), Color.WHITE);
	    } else if (plot instanceof XYPlot) {
	        setAxisFontColor(((XYPlot) plot).getDomainAxis(), Color.WHITE);
	        setAxisFontColor(((XYPlot) plot).getRangeAxis(), Color.WHITE);
	    }
		
		pnlChart.add(chartPanel, BorderLayout.NORTH);
		this.repaint();
		
		super.setVisible(true);
	}
	
	private void setAxisFontColor(Axis axis, Color fontColor) 
	{
	    if (!fontColor.equals(axis.getLabelPaint()))
	    	axis.setLabelPaint(fontColor);
	    if (!fontColor.equals(axis.getTickLabelPaint()))
	        axis.setTickLabelPaint(fontColor);
	}
	
	public void editTest() throws IOException
	{
		//lblError.setVisible(false);
		Test targetTest = filteredTests.get(table.getSelectedRow());
		TestEditor TE = new TestEditor(this, targetTest);			
		TE.setVisible(true);
		this.setVisible(false);
	}
	
	public void mockToggle(Test oldTest, boolean isUndo, boolean isRedo, boolean isEdit)
	{
		Test newTest;
		
		if (oldTest instanceof MockExam)
		{
			newTest = new Test(oldTest.getTestName(), oldTest.getScore(), oldTest.getReflection(), oldTest.getTotal(), oldTest.getSubject(), oldTest.getID(), tests);
		}
		else
		{
			newTest = new MockExam(oldTest.getTestName(), oldTest.getScore(), oldTest.getReflection(), oldTest.getTotal(), oldTest.getSubject(), oldTest.getID(), tests);
		}
		
		tests.remove(oldTest);
		tests.add(newTest);
		
		buildTestList();
		drawChart();
		saveData();
		
		if (!isEdit)
		{
			if (isUndo && !isRedo)
			{
				redoMocks.add(newTest.getID());
			}
			
			if ((isRedo && !isUndo) || (!isUndo && !isRedo))
			{
				undoMocks.add(newTest.getID());
			}
			
			if (!isUndo && !isRedo)
			{
				clearRedos();
				System.out.println("HERERERERERERE");
				edits.add(edits.size(), 3);
				//undoMocks.add(newTest);
			}
		}
	}
	
	public void openGoals()
	{
		Goals goals = new Goals(this);
		
		this.setVisible(false);
		goals.setVisible(true);
	}
	
	public void openAnalyse()
	{
		AnalysisWindow aw = new AnalysisWindow(this);
		
		this.setVisible(false);
		aw.setVisible(true);
	}
	
	public ArrayList<Test> getTests()
	{
		return tests;
	}
	
	private void clearRedos()
	{
		redoDeletes.clear();
		redoEdits.clear();
		redoNewTests.clear();
		redoMocks.clear();
		redos.clear();
	}
	
	private Test getTestUsingID (int ID)
	{
		Test test = null;
		
		for (int i = 0; i < tests.size(); i++)
		{
			if (tests.get(i).getID() == ID)
			{
				test = tests.get(i);
				break;
			}
		}
		
		return test;
	}
	
	private void removeEdit()
	{
		ArrayList<Integer> newList = new ArrayList<Integer>();
		
		for (int i = 0; i < edits.size() - 1; i++)
		{
			newList.add(edits.get(i));
		}
		
		edits = newList;
	}
	
	private void removeRedo()
	{
		ArrayList<Integer> newList = new ArrayList<Integer>();
		
		for (int i = 0; i < redos.size() - 1; i++)
		{
			newList.add(redos.get(i));
		}
		
		redos = newList;
	}
	
	private void openDetails (Test selectedTest)
	{
		TestDetails td = new TestDetails (this, selectedTest);
		this.setVisible(false);
		td.setVisible(true);
	}
}



class BoardTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        return this;
    }
}
