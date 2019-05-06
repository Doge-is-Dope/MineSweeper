package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MineSweeper2 extends JFrame 
{
	private static JFrame frame;
	private final JLabel lblDifficulty;
	private final JButton btnConfirm;
	private final JPanel mainPanel;
	private final JComboBox<String> combolDifficulty;
	private String[] difficulty = {"Beginner","intermediate", "Advanced"};
	
	public MineSweeper2 ()
	{   
	    combolDifficulty = new JComboBox<String>(difficulty);
	    btnConfirm = new JButton("Confirm");
	    mainPanel = new JPanel();
	    lblDifficulty = new JLabel("Please choose the difficuly");
	    EventHandler handler = new EventHandler();
	    mainPanel.add(lblDifficulty);
	    mainPanel.add(combolDifficulty);
	    mainPanel.add(btnConfirm);
	    combolDifficulty.addActionListener(handler);
	    btnConfirm.addActionListener(handler);
	    add(BorderLayout.CENTER, mainPanel);    
	}
	
	private class EventHandler implements ActionListener
	{
		Model model;
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String btnName = e.getActionCommand();
			//ComboBox<String> combo = (JComboBox<String>) e.getSource();
	        if (btnName.equals("Confirm"))
	        {
	        	if(combolDifficulty.getSelectedItem().equals(difficulty[0]))
	        	{
	        		System.out.println(difficulty[0]);
	        		model = new Model(9, 9, 10);
	        	}
	        	else if(combolDifficulty.getSelectedItem().equals(difficulty[1]))
	        	{
	        		System.out.println(difficulty[1]);
	        		model = new Model(16, 16, 40);
	        	}
	        	else
	        	{
	        		System.out.println(difficulty[2]);
	        		model = new Model(30, 16, 99);
	        	}
	        	//Creating the View
	        	View view = new View(model);
	        	JFrame gameframe = new JFrame("Minesweeper");
	        	gameframe.setContentPane(view);
	        	gameframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        	gameframe.pack();
	        	gameframe.setVisible(true);
	        	gameframe.setAlwaysOnTop(true);
	        	gameframe.setLocationRelativeTo(null);
	        }
		}
	}
	
	public static void main(String[] args)
	{
		frame = new MineSweeper2();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 90);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
	}
}
