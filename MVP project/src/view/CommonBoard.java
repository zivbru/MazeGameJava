package view;

import java.util.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;

import algorithm.generic.Solution;



/**
 * The Class CommonBoard. All the commons of a board is implemented in this class, taking care of the key listiner and applying direction. 
 */
public abstract class CommonBoard extends Composite implements Board {

	/** The timer. */
	Timer timer;

	/** The won. */
	boolean won=false;

	/** The board. */
	CommonTile[][] board;

	/** The board rows x. */
	int boardRowsX; 

	/** The board cols y. */
	int boardColsY;

	/** The character. */
	CommonCharacter character=null;

	/** The check dragged. */
	boolean checkDragged=false; 


	/**
	 * Instantiates a new common board.
	 *
	 * @param parent the parent
	 * @param style the style
	 */
	public CommonBoard(Composite parent, int style) {
		super(parent, style);
		addPaintListener(new PaintListener() { 

			@Override
			public void paintControl(PaintEvent arg0) {
				drawBoard(arg0);
			}
		});

		this.addKeyListener(new KeyListener(){	
			@Override
			public void keyPressed(KeyEvent e) { //each of those codes represents a key on the keyboard in our case up down right left arrows


				if (e.keyCode == SWT.ARROW_DOWN && hasPathForward(character.getCellX(),character.getCellY())){
					applyInputDirection((Direction.FORWARD));
					//forward
				} 
				else 
					if (e.keyCode == SWT.ARROW_RIGHT && hasPathRight(character.getCellX(),character.getCellY())) {
						applyInputDirection((Direction.RIGHT));
						//right
					} 
					else 
						if (e.keyCode == SWT.ARROW_LEFT &&  hasPathLeft(character.getCellX(),character.getCellY())) {
							applyInputDirection(Direction.LEFT);
							//left
						} 
						else
							if (e.keyCode == SWT.ARROW_UP  && hasPathBackward(character.getCellX(),character.getCellY())) {
								applyInputDirection(Direction.BACKWARD);
								//backward
							} 
							else
								if( e.keyCode == SWT.PAGE_UP && hasPathUp(character.getCellX(),character.getCellY())){
									applyInputDirection(Direction.UP);
								}
								else
									if( e.keyCode == SWT.PAGE_DOWN && hasPathDown(character.getCellX(),character.getCellY())){
										applyInputDirection(Direction.DOWN);
									}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}
		});
		//allows us to set the size of window by pressing ctrl + scrolling
		this.addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseScrolled(MouseEvent arg0) {
				if((arg0.stateMask& SWT.CTRL)!=0){ //if control is pressed
					if(arg0.count>0){ //and we scroll up
						//up zoom in										//Bonus!!
						setSize(getSize().x+30, getSize().y+30);
					}
					if(arg0.count<0){ //and we scroll down
						setSize(getSize().x-30, getSize().y-30);
						//down zoom out	
					}
				}	
			}
		});		
	}

	/* (non-Javadoc)
	 * @see view.Board#applyInputDirection(view.Direction)
	 */
	@Override
	public abstract void  applyInputDirection(Direction direction);

	/* (non-Javadoc)
	 * @see view.Board#hasPathUp(int, int)
	 */
	@Override
	public abstract boolean hasPathUp(int characterRow,int characterCol);
	
	/* (non-Javadoc)
	 * @see view.Board#hasPathRight(int, int)
	 */
	@Override
	public abstract boolean hasPathRight(int characterRow,int characterCol);
	
	/* (non-Javadoc)
	 * @see view.Board#hasPathDown(int, int)
	 */
	@Override
	public abstract boolean hasPathDown(int characterRow,int characterCol);
	
	/* (non-Javadoc)
	 * @see view.Board#hasPathLeft(int, int)
	 */
	@Override
	public abstract boolean hasPathLeft(int characterRow,int characterCol);
	
	/* (non-Javadoc)
	 * @see view.Board#hasPathForward(int, int)
	 */
	@Override
	public abstract boolean hasPathForward(int characterRow,int characterCol);
	
	/* (non-Javadoc)
	 * @see view.Board#hasPathBackward(int, int)
	 */
	@Override
	public abstract boolean hasPathBackward(int characterRow,int characterCol);
	
	/* (non-Javadoc)
	 * @see view.Board#displayProblem(java.lang.Object)
	 */
	@Override
	public abstract void displayProblem(Object o);
	
	/* (non-Javadoc)
	 * @see view.Board#displaySolution(algorithm.generic.Solution)
	 */
	@Override
	public abstract <T> void displaySolution(Solution<T> s);
	
	/* (non-Javadoc)
	 * @see view.Board#destructBoard()
	 */
	@Override
	public abstract void destructBoard();

	/**
	 * Sets the user asked for solution.
	 *
	 * @param userAskedForSolution the new user asked for solution
	 */
	public abstract void setUserAskedForSolution(boolean userAskedForSolution);



}
