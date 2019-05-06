package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * 
 * 
 */
public class ButtonView extends JButton implements Observer {

    private static final long serialVersionUID = 1L;
    private JButton button;
    private Field field;
    private Controller controller;
    

    /**
     * Creates the neccassary Buttons
     *
     * @param field 
     */
    public ButtonView(Field field) 
    {
    	//Border border = BorderFactory.createDashedBorder(Color.lightGray); 
        this.field = field;
        this.button = new JButton("");
        this.button.setPreferredSize(new Dimension(50, 50));
        this.button.setBackground(Color.WHITE);
        this.controller = new Controller(field);
        this.button.addMouseListener(controller);
        this.field.addObserver(this);
    }

    public JButton getButton() {
        return this.button;
    }

    /**
     * Makes a Button Invisible
     */
    public void setUnvisible() {
        this.button.setVisible(false);
        this.field.reveal();
    }

    /**
     * Update button view
     * 
     * @param obs 
     * @param o
     */
    @Override
    public void update(Observable obs, Object o) 
    {
    	Border border = BorderFactory.createDashedBorder(Color.lightGray); 
        if (field.getRevealed() == true) 
        {

            if (this.field.getField_id() == 9) 
            {
                this.button.setBackground(new Color(255,128,128));
                this.button.setText("X");
                this.button.setFont(new Font("Arial", Font.PLAIN, 20));
                this.button.setBorder(border);
                //this.button.setBorder(null);
                this.button.setOpaque(true);
            } 
            else 
            {
                //this.button.setBackground(new Color(204, 204, 204));
                this.button.setFont(new Font("Arial", Font.PLAIN, 20));
                this.button.setBorder(border);
                if (this.field.getField_id() == 0) 
                {
                    this.button.setText("");
                } 
                else 
                {
                    this.button.setText(Integer.toString(this.field.getField_id()));
                }
            }
        }
        if (field.isFlag())
        {
            this.button.setText("▲");
            this.button.setBackground(new Color(255,255,153));
            this.button.setFont(new Font("Arial", Font.PLAIN, 20));
            this.button.setBorder(border);
            this.button.setOpaque(true);
        }
        if (!field.isFlag() && !field.getRevealed()) 
        {
        	this.button.setText("");
            this.button.setBackground(new Color(204, 204, 204));
        }
    }

}
