import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * main
 */
public class Game implements Consts
{
    private JFrame window;
    private JDialog chooser;
    private JSpinner[] spinners;
    private JButton face;
    private JPanel boardPanel;
    private JButton[][] buttons;
    private Grid grid;
    private int width, height, mines;

    public Game()
    {
        this.reset();
    }

    private void reset()
    {
        try
        {
            this.window.setVisible(false);
        }
        catch (NullPointerException e)
        {}
        finally
        {
            this.window = new JFrame("Mine Sweeper");
        }

        this.chooser = this.createChooser();
        this.chooser.setLocationRelativeTo(null);
        this.chooser.setVisible(true);
    }

    private void createWindow()
    {
        try
        {
            this.grid = new Grid(this.mines, this.width, this.height);
        }
        catch (UnableToCreateGrid e)
        {
            System.out.println("Error creating grid: " + e.getMessage());
            System.exit(0);
        }



        this.width = this.grid.getSize()[0];
        this.height = this.grid.getSize()[1];

        ClassLoader cl = this.getClass().getClassLoader();
        this.face = new JButton(new ImageIcon(cl.getResource("img/face-smile.png")));
        this.face.addActionListener(new FaceManager());


        this.boardPanel = this.createBoard();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(this.face, BorderLayout.NORTH);
        mainPanel.add(this.boardPanel, BorderLayout.CENTER);

        this.window.setContentPane(mainPanel);
        this.window.pack();
        this.window.setLocationRelativeTo(null);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setVisible(true);
    }

    private JDialog createChooser()
    {

        final JDialog chooser = new JDialog(this.window, "Choose size");

        @SuppressWarnings("unused")
        Container container = chooser.getContentPane();

        this.spinners = new JSpinner[3];

        if (this.mines != 0)
            this.spinners[0] = new JSpinner(new SpinnerNumberModel(this.mines, 3, 300, 1)); //mines
        else
            this.spinners[0] = new JSpinner(new SpinnerNumberModel(10, 3, 300, 1)); //mines

        if (this.width != 0)
            this.spinners[1] = new JSpinner(new SpinnerNumberModel(this.width, 3, 25, 1));  //width
        else
            this.spinners[1] = new JSpinner(new SpinnerNumberModel(10, 3, 25, 1));  //width

        if (this.height != 0)
            this.spinners[2] = new JSpinner(new SpinnerNumberModel(this.height, 3, 25, 1));  //height
        else
            this.spinners[2] = new JSpinner(new SpinnerNumberModel(10, 3, 25, 1));  //height


        final String[] labels = {"Mines: ", "Width: ", "Height: "};

        JPanel parent = new JPanel();
        parent.setLayout(new GridLayout(3, 2));


        for (int i = 0; i < spinners.length; i++)
        {
            parent.add(new JLabel(labels[i]));
            parent.add(spinners[i]);
        }

        JButton okButton = new JButton("OK");

        chooser.add(parent, BorderLayout.CENTER);
        chooser.add(okButton, BorderLayout.SOUTH);

        chooser.setSize(200, 130);
        chooser.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        okButton.addActionListener(new ChooserHandler());



        return chooser;
    }

    class ChooserHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            mines = (Integer) spinners[0].getValue();
            width = (Integer) spinners[1].getValue();
            height = (Integer) spinners[2].getValue();

            chooser.setVisible(false);
            createWindow();

        }

    }


    private JPanel createBoard()
    {
        this.buttons = new JButton[this.height][this.width];
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(this.height, this.width));


        for (int i = 0; i < this.height; i++)
        {
            for (int j = 0; j < this.width; j++)
            {
                this.buttons[i][j] = new JButton();
                this.buttons[i][j].setPreferredSize(new Dimension(50, 50));
                this.buttons[i][j].setFont(new Font("Arial", Font.BOLD, 25));
                this.buttons[i][j].setBackground(Color.WHITE);
                panel.add(this.buttons[i][j]);

                this.buttons[i][j].addMouseListener(new ButtonManager(i, j));
            }
        }

        return panel;
    }

    private void uncover(int row, int col)
    {
        try
        {
            //load images
            ClassLoader cl = this.getClass().getClassLoader();
            Icon exploded = new ImageIcon(cl.getResource("img/bang.png"));
            Icon cool     = new ImageIcon(cl.getResource("img/face-cool.png"));
            Icon sad      = new ImageIcon(cl.getResource("img/face-sad.png"));

            if (!this.buttons[row][col].isEnabled() && !this.checkWon())
            {
                //reset face icon
                this.face.setIcon(cool);
                return;
            }

            try
            {
                this.grid.reveal(row, col);
            }
            catch (SquareHasFlag e)
            {
                // if button is flagged, do nothing
                return;
            }
            catch (SquareAlreadyRevealed e)
            {
                // if button has already been revealed, just continue
            }

            this.buttons[row][col].setEnabled(false);


            //add the revealed image to button
            switch (this.grid.getMarkAt(row, col))
            {
                case MINE:
                    this.buttons[row][col].setIcon(exploded);
                    this.buttons[row][col].setDisabledIcon(exploded);
                    this.face.setIcon(sad);
                    this.lost(row, col);
                    break;

                case BLANK:
                    //propagate for blank squares
                    this.uncover(row-1, col-1);
                    this.uncover(row  , col-1);
                    this.uncover(row+1, col-1);
                    this.uncover(row+1, col  );
                    this.uncover(row+1, col+1);
                    this.uncover(row  , col+1);
                    this.uncover(row-1, col+1);
                    this.uncover(row-1, col  );
                    this.face.setIcon(cool);
                    if (this.checkWon())
                    {
                        won();
                    }
                    break;

                default:
                    this.buttons[row][col].setText(this.grid.getMarkAt(row, col)+"");
                    this.face.setIcon(cool);
                    if (this.checkWon())
                    {
                        won();
                    }
            }
        }
        catch (SquareDoesNotExist e)
        {
            return;
        }
        catch (IndexOutOfBoundsException e)
        {
            return;
        }


    }

    private void lost(int row, int col)
    {
        this.showAllMines(row, col);
    }

    private void showAllMines(int row, int col)
    {
        //show all mines EXCEPT for the one at row,col

        ClassLoader cl = this.getClass().getClassLoader();
        Icon mine = new ImageIcon(cl.getResource("img/mine.png"));
        Icon badFlag = new ImageIcon(cl.getResource("img/warning.png"));

        for (int i = 0; i < this.height; i++)
        {
            for (int j = 0; j < this.width; j++)
            {
                try
                {
                    this.buttons[i][j].setEnabled(false);

                    if (this.grid.getMarkAt(i, j) == MINE && ( i != row && j != col) && !this.grid.hasFlag(i, j))
                    {
                        //if has a mine and not a flag and is not the exploded mine
                        this.buttons[i][j].setIcon(mine);
                        this.buttons[i][j].setDisabledIcon(mine);
                    }
                    else if (this.grid.getMarkAt(i, j) != MINE && this.grid.hasFlag(i, j))
                    {
                        //if a flag was misplaced
                        this.buttons[i][j].setIcon(badFlag);
                        this.buttons[i][j].setDisabledIcon(badFlag);
                    }
                }
                catch (SquareDoesNotExist e) {}
            }
        }
    }

    private boolean checkWon()
    {
        for (int i = 0; i < this.height; i++)
        {
            for (int j = 0; j < this.width; j++)
            {
                try
                {
                    if (this.buttons[i][j].isEnabled() && this.grid.getMarkAt(i, j) != MINE)
                    {
                        return false;
                    }
                }
                catch (SquareDoesNotExist e)
                {
                    continue;
                }
            }
        }
        return true;
    }

    private boolean checkLost()
    {
        for (int i = 0; i < this.height; i++)
        {
            for (int j = 0; j < this.width; j++)
            {
                try
                {
                    if (!this.buttons[i][j].isEnabled() && this.grid.getMarkAt(i, j) == MINE)
                    {
                        return true;
                    }
                }
                catch (SquareDoesNotExist e)
                {
                    continue;
                }
            }
        }
        return false;
    }

    private void won()
    {
        ClassLoader cl = this.getClass().getClassLoader();
        Icon mine = new ImageIcon(cl.getResource("img/mine.png"));

        Icon win = new ImageIcon(cl.getResource("img/face-win.png"));
        this.face.setIcon(win);

        for (int i = 0; i < this.height; i++)
        {
            for (int j = 0; j < this.width; j++)
            {
                this.buttons[i][j].setEnabled(false);

                try
                {
                    if (!this.grid.hasFlag(i, j) && this.grid.getMarkAt(i, j) == MINE)
                    {
                        this.buttons[i][j].setIcon(mine);
                        this.buttons[i][j].setDisabledIcon(mine);
                    }
                }
                catch (SquareDoesNotExist e)
                {
                    continue;
                }
            }
        }
    }

    private void toggleFlag(int row, int col)
    {
        try
        {
            this.grid.toggleFlag(row, col);
        }
        catch (SquareDoesNotExist e) {}
        catch (SquareAlreadyRevealed e) {}
    }

    class FaceManager implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Game.this.reset();
        }

    }

    class ButtonManager implements MouseListener
    {
        private int row;
        private int col;

        public ButtonManager(int row, int col)
        {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (checkLost() || checkWon())
            {
                return;
            }

            if (e.getButton() == MouseEvent.BUTTON1)
            {
                //right mouse button uncovers that button

                if (DEBUG)
                    System.out.println("Left mouse button pressed at coords " + this.row + "," + this.col);

                Game.this.uncover(this.row, this.col);
            }

            else if (e.getButton() == MouseEvent.BUTTON2)
            {
                //middle mouse button uncovers unflagged buttons next to pressed button

                if (DEBUG)
                    System.out.println("Middle mouse button pressed at coords " + this.row + "," + this.col);

                try
                {
                    if (grid.flagsAround(row, col) == grid.minesAround(row, col) && grid.isRevealed(row, col))
                    {
                        uncover(row-1, col-1);
                        uncover(row  , col-1);
                        uncover(row+1, col-1);
                        uncover(row+1, col  );
                        uncover(row+1, col+1);
                        uncover(row  , col+1);
                        uncover(row-1, col+1);
                        uncover(row-1, col  );
                    }
                }
                catch (DirDoesNotExist e1) {}
                catch (SquareDoesNotExist e1) {}
            }

            else if (e.getButton() == MouseEvent.BUTTON3)
            {
                //right mouse button toggles flag

                if (DEBUG)
                    System.out.println("Right mouse button pressed at coords " + this.row + "," + this.col);

                //if button is disabled, do nothing
                if (!buttons[row][col].isEnabled())
                {
                    return;
                }

                Game.this.toggleFlag(this.row, this.col);

                if (grid.hasFlag(this.row, this.col))
                {
                    ClassLoader cl = this.getClass().getClassLoader();
                    buttons[row][col].setIcon(new ImageIcon(cl.getResource("img/flag.png")));
                    buttons[row][col].setDisabledIcon(new ImageIcon(cl.getResource("img/flag.png")));
                }
                else
                {
                    buttons[row][col].setIcon(null);
                }
            }

            if (!buttons[row][col].isEnabled() && !checkWon())
            {
                ClassLoader cl = this.getClass().getClassLoader();
                //reset face icon
                face.setIcon(new ImageIcon(cl.getResource("img/face-cool.png")));
                return;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e)
        {
            if (checkLost() || checkWon())
            {
                return;
            }

            if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON2)
            {
                ClassLoader cl = this.getClass().getClassLoader();
                Icon worried = new ImageIcon(cl.getResource("img/face-worried.png"));

                face.setIcon(worried);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {}
    }

    public static void main(String args[])
    {
        try
        {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}

        new Game();
    }
}
