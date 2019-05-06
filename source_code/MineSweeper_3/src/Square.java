/**
 * Class to represent a sqare in a Minesweeper grid
 */
class Square implements Consts
{
	private char mark;
	private boolean hidden;
	private boolean flagged;
	
	public void fill(char mark)
	{
		this.mark = mark;
	}
	
	public boolean isHidden()
	{
		return this.hidden;
	}
	
	public boolean isRevealed()
	{
		return !(this.hidden);
	}
	
	public char reveal()
	{
		this.hidden = false;
		return this.mark;
	}
	
	public void hide()
	{
		this.hidden = true;
	}
	
	public char getMark()
	{
		return this.mark;
	}
	
	public void toggleFlag()
	{
		if (this.flagged)
		{
			if (DEBUG)
				System.out.println("Unflagging...");
			this.flagged = false;
		}
		else 
		{
			if (DEBUG)
				System.out.println("Flagging...");
			this.flagged = true;
		}
	}
	
	
	public boolean hasFlag()
	{
		return this.flagged;
	}
	
	/**
	 * Constructor
	 * @param mark
	 * @param hidden
	 */
	public Square(char mark, boolean hidden)
	{
		this.fill(mark);
		
		if (hidden)
			this.hide();
		else
			this.reveal();
		
		this.flagged = false;
	}
	
	/**
	 * Default constructos
	 * Initializes the Square to mark=BLANK and hidden=true
	 */
	public Square()
	{
		this(NONE, true);
	}
	
}
