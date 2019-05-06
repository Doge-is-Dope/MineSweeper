package test;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Test_1 
{
	public static void main(String[] args)
	{
		JFrame testFrame = new JFrame("Test");
		JButton btn = new JButton("Confirm");
		btn.setText("^");
		btn.setBackground(Color.RED);
		//btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setOpaque(true);
		
		testFrame.add(BorderLayout.CENTER, btn);   
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.pack();
		testFrame.setVisible(true);
		testFrame.setAlwaysOnTop(true);
		testFrame.setLocationRelativeTo(null);
	}
}
