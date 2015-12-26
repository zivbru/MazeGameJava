package algorithms.mazeGenerators;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * The Class Maze3d.
 */
public class Maze3d implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The maze. */
	private int[][][] maze;

	/** The width. */
	private int width;

	/** The height. */
	private int height;

	/** The length. */
	private int length;

	/** The Start position. */
	private Position StartPosition;

	/** The Goal position. */
	private Position GoalPosition;

	/**
	 * Instantiates a new maze3d.
	 *
	 * @param maze
	 * @param height
	 * @param length
	 * @param width
	 * @param startPosition
	 *            the start position
	 * @param GoalPosition
	 *            the goal position
	 */
	public Maze3d(int[][][] maze, int height, int length, int width,
			Position startPosition, Position GoalPosition) {
		super();
		this.maze = new int[height][length][width];
		this.width = width;
		this.height = height;
		this.length = length;
		this.StartPosition = startPosition;
		this.GoalPosition = GoalPosition;
	}

	/**
	 * Instantiates a new maze3d.
	 *
	 * @param height
	 *            the height
	 * @param length
	 *            the length
	 * @param width
	 *            the width
	 */
	public Maze3d(int height, int length, int width) {

		this.maze = new int[height][length][width];
		this.height = height;
		this.length = length;
		this.width = width;

	}

	public Maze3d(byte[] byteArray) {

		int [] start = new int[3];
		int [] goal = new int[3];
		int currentSize = start.length ;
		for (int i = 0; i < currentSize; i++) {
			start[i] = (int)byteArray[i];
		}
		currentSize = start.length + goal.length;
		int j=0;
		for (int i = 3; i < currentSize; i++) {
			goal[j] = (int) byteArray[i];
			j++;
		}
		currentSize = start.length + goal.length;

		this.GoalPosition = new Position(goal[0], goal[1], goal[2]);
		this.StartPosition = new Position(start[0], start[1], start[2]);

		this.height = (int) byteArray[currentSize++];
		this.length = (int) byteArray[currentSize++];
		this.width = (int) byteArray[currentSize++];

		this.maze = new int[this.height][this.length][this.width];

		for (int i = 0; i < this.height; i++) {
			for (int w = 0; w < this.length; w++) {
				for (int k = 0; k < this.width; k++) {
					this.maze[i][w][k] = byteArray[currentSize++];
				}
			}

		}
	}

	/**
	 * Gets the maze.
	 *
	 * @return the maze
	 */
	public int[][][] getMaze() {
		return maze;
	}

	/**
	 * Sets the maze.
	 *
	 * @param maze
	 *            the new maze
	 */
	public void setMaze(int[][][] maze) {
		this.maze = maze;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width
	 *            the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 *
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the length.
	 *
	 * @param length
	 *            the new length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * Gets the start position.
	 *
	 * @return the start position
	 */
	public Position getStartPosition() {
		return StartPosition;
	}

	/**
	 * Sets the start position.
	 *
	 * @param startPosition
	 *            the new start position
	 */
	public void setStartPosition(Position startPosition) {
		StartPosition = startPosition;
	}

	/**
	 * Gets the goal position.
	 *
	 * @return the goal position
	 */
	public Position getGoalPosition() {
		return GoalPosition;
	}

	/**
	 * Gets the goal.
	 *
	 * @return the goal
	 */
	public String getGoal() {
		return GoalPosition.toString();
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public String getStart() {
		return StartPosition.toString();
	}

	/**
	 * Sets the goal position.
	 *
	 * @param z
	 *            -height
	 * @param x
	 *            - length
	 * @param y
	 *            - width
	 */
	public void setGoalPosition(int z, int x, int y) {
		this.GoalPosition = new Position(z, x, y);
	}

	/**
	 * Sets the goal position.
	 *
	 * @param GoalPosition
	 *            the new goal position
	 */
	public void setGoalPosition(Position GoalPosition) {
		this.GoalPosition = GoalPosition;
	}

	/**
	 * Gets the cell value.
	 * 
	 * @param z
	 *            -floor
	 * @param x
	 *            - length
	 * @param y
	 *            - width
	 * @return the cell value
	 */
	public int getCellValue(int z, int x, int y) {
		if(outOfRange(z, x, y)){
		return this.maze[z][x][y];
		}
		else 
			return 1;
	}

	/**
	 * Sets the cell value.
	 *
	 * @param z
	 *            -height
	 * @param x
	 *            - length
	 * @param y
	 *            - width
	 * @param num
	 */
	public void setCellValue(int z, int x, int y, int num) {
		if(outOfRange(z, x, y))
		this.maze[z][x][y] = num;
	}

	/**
	 * Position to int.
	 *
	 * @param Position
	 * @return value
	 */
	public int positionToInt(Position p) {

		int[] arr = p.split();
		int z = arr[0];
		int x = arr[1];
		int y = arr[2];

		return this.maze[z][x][y];
	}

	/**
	 * Generate walls.
	 */
	public void generateWalls() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				for (int k = 0; k < maze[i][j].length; k++) {
					maze[i][j][k] = 1;
				}
			}
		}
	}

	/**
	 * Out of range validation
	 *
	 * @param z
	 *            -height
	 * @param x
	 *            - length
	 * @param y
	 *            - width
	 * @return the boolean
	 */
	public Boolean outOfRange(int z, int x, int y) {

		if (z >= 0 && x >= 0 && y >= 0 && z < this.maze.length
				&& x < this.maze[0].length && y < this.maze[0][0].length) {
			return true;
		} else
			return false;
	}

	/**
	 * Rand.
	 *
	 * @return the int[]
	 */
	public int[] rand() {

		int[] temp = new int[3];

		temp[0] = (int) (Math.random() * (this.height));
		temp[1] = (int) (Math.random() * (this.length));
		temp[2] = (int) (Math.random() * (this.width));
		return temp;

	}

	/**
	 * Int to string.
	 *
	 * @param z
	 *            the z
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the string
	 */
	public String intToString(int z, int x, int y) {

		Position p = new Position(z, x, y);
		return p.toString();

	}

	/**
	 * Int to position.
	 *
	 * @param z
	 *            -height
	 * @param x
	 *            - length
	 * @param y
	 *            - width
	 * @return the position
	 */
	public Position intToPosition(int z, int x, int y) {

		Position p = new Position(z, x, y);
		return p;

	}

	/**
	 * Available move.
	 *
	 * @param z
	 *            -height
	 * @param x
	 *            - length
	 * @param y
	 *            - width
	 * @return if the move is available
	 */
	public Boolean availableMove(int z, int x, int y) {
		int count = 0;

		if (z + 1 < height) {
			if (this.maze[z + 1][x][y] == 0)
				count++;
		}
		if (z - 1 > 0) {
			if (this.maze[z - 1][x][y] == 0)
				count++;
		}
		if (x + 1 < length) {
			if (this.maze[z][x + 1][y] == 0)
				count++;
		}
		if (x - 1 > 0) {
			if (this.maze[z][x - 1][y] == 0)
				count++;
		}
		if (y + 1 < width) {
			if (this.maze[z][x][y + 1] == 0)
				count++;
		}
		if (y - 1 > 0) {
			if (this.maze[z][x][y - 1] == 0)
				count++;
		}
		return count <= 1;
	}

	/**
	 * Gets the possible moves.
	 *
	 * @param current
	 *            Position
	 * @return the possible moves
	 */
	public ArrayList<Position> getPossibleMoves(Position current) {

		int[] temp = current.split();
		int z = temp[0];
		int x = temp[1];
		int y = temp[2];

		ArrayList<Position> moves = new ArrayList<Position>();
		// Up dimension

		if (this.outOfRange(z + 1, x, y) && this.maze[z + 1][x][y] == 0) {

			Position p = new Position(z + 1, x, y);
			moves.add(p);
		}

		// Down dimension
		if (this.outOfRange(z - 1, x, y) && this.maze[z - 1][x][y] == 0) {

			Position p = new Position(z - 1, x, y);
			moves.add(p);

		}

		// Forward
		if (this.outOfRange(z, x + 1, y) && this.maze[z][x + 1][y] == 0) {

			Position p = new Position(z, x + 1, y);
			moves.add(p);

		}

		// Backwards
		if (this.outOfRange(z, x - 1, y) && this.maze[z][x - 1][y] == 0) {

			Position p = new Position(z, x - 1, y);
			moves.add(p);

		}

		// Right
		if (this.outOfRange(z, x, y + 1) && this.maze[z][x][y + 1] == 0) {

			Position p = new Position(z, x, y + 1);
			moves.add(p);

		}

		// Left
		if (this.outOfRange(z, x, y - 1) && this.maze[z][x][y - 1] == 0) {

			Position p = new Position(z, x, y - 1);
			moves.add(p);

		}

		return moves;

	}

	public boolean equals(Maze3d maze) {
		if(this.getStartPosition().equals(maze.getStartPosition())&&this.getGoalPosition().equals(maze.getGoalPosition()))
			if(this.getHeight()==maze.getHeight()&&this.getLength()==maze.getLength()&&this.getWidth()==maze.getWidth())
				for (int i = 0; i < this.maze.length; i++) {
					for (int j = 0; j < this.maze.length; j++) {
						for (int k = 0; k < this.maze.length; k++) {
							if(this.maze[i][j][k]==maze.maze[i][j][k])
								return true;
								
						}
						
					}
					
				}
		return false;
	}

	/**
	 * Gets the cross section by z.
	 *
	 * @param index
	 * @return the cross section by z
	 * @throws IndexOutOfBoundsException
	 *             the index out of bounds exception
	 */
	public int[][] getCrossSectionByZ(int index)
			throws IndexOutOfBoundsException {
		if (index < 0 || index > this.height)
			throw new IndexOutOfBoundsException("Error");
		int[][] maze2d = new int[this.getWidth()][this.getLength()];

		for (int i = 0; i < maze2d.length; i++) {
			for (int j = 0; j < maze2d[0].length; j++) {
				maze2d[i][j] = this.maze[index][i][j];
			}

		}
		return maze2d;
	}

	/**
	 * Gets the cross section by x.
	 *
	 * @param index
	 * @return the cross section by x
	 * @throws IndexOutOfBoundsException
	 *             the index out of bounds exception
	 */
	public int[][] getCrossSectionByX(int index)
			throws IndexOutOfBoundsException {
		if (index < 0 || index > this.length)
			throw new IndexOutOfBoundsException("Error");
		int[][] maze2d = new int[this.getHeight()][this.getLength()];

		for (int i = 0; i < maze2d.length; i++) {
			for (int j = 0; j < maze2d[0].length; j++) {
				maze2d[i][j] = this.maze[i][index][j];
			}

		}
		return maze2d;
	}

	/**
	 * Gets the cross section by y.
	 *
	 * @param index
	 * @return the cross section by y
	 * @throws IndexOutOfBoundsException
	 *             the index out of bounds exception
	 */
	public int[][] getCrossSectionByY(int index)
			throws IndexOutOfBoundsException {
		if (index < 0 || index > this.width)
			throw new IndexOutOfBoundsException("Error");
		int[][] maze2d = new int[this.getLength()][this.getWidth()];

		for (int i = 0; i < maze2d.length; i++) {
			for (int j = 0; j < maze2d[0].length; j++) {
				maze2d[i][j] = this.maze[i][j][index];
			}

		}
		return maze2d;
	}

	/**
	 * Prints the maze
	 */
	public void print() {

		for (int i = 0; i < maze.length; i++) {
			System.out.println();
			for (int j = 0; j < maze[i].length; j++) {
				System.out.println();
				for (int k = 0; k < maze[i][j].length; k++) {
					System.out.print(maze[i][j][k] + " ");
					if (k + 1 == maze[i][j].length)
						System.out.println();

				}
			}

		}
		System.out.println();
	}

	public byte[] toByteArray() throws IOException{


		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		int [] array1= this.getStartPosition().split();
		for (int i = 0; i < array1.length; i++) {
			dos.write(array1[i]);	
		}

		int []array2= this.getGoalPosition().split();
		for (int j = 0; j < array2.length; j++) {
			dos.write(array2[j]);
		}
		dos.write(height);
		dos.write(length);
		dos.write(width);

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.length; j++) {
				for (int k = 0; k < this.width; k++) {
					dos.write(maze[i][j][k]);
				}
			}
		}

		byte [] retVal = baos.toByteArray();


		return retVal;
	}
}

