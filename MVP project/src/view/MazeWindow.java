package view;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import algorithm.generic.Solution;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import boot.Run;
import boot.RunCLI;
import boot.RunGUI;
import boot.WritePropertiesGUI;
import generic.LoadPrefernces;
import generic.Preferences;
import presenter.Command;


/**
 * The Class MazeWindow. A spesific implementation of the game window, designing the window as we wish and implementing the View interface in order to use the GUI.
 * In order to use CLI @see MyCliView.
 */
public class MazeWindow extends BasicWindow implements View {

	/** The commands. */
	protected HashMap<String, Command>  commands;

	/** The Last user command. */
	protected Command LastUserCommand =null;

	/** The board widget. */
	CommonBoard boardWidget;

	/** The preferences. */
	Preferences preferences;


	/** The maze name. */
	String mazeName=null;

	/** The data recieved. */
	Maze3d dataRecieved=null; 

	/** The input. */
	MazeProperties input;

	/**
	 * Instantiates a new maze window.
	 *
	 * @param display the display
	 * @param shell the shell
	 * @param title the title
	 * @param width the width
	 * @param height the height
	 */
	public MazeWindow(Display display,Shell shell,String title, int width, int height) {
		super(display,shell,title,width,height);
	}

	/**
	 * Instantiates a new maze window.
	 *
	 * @param title the title
	 * @param width the width
	 * @param height the height
	 */
	public MazeWindow(String title, int width, int height) {
		super(title, width, height);

	}

	/* (non-Javadoc)
	 * @see view.BasicWindow#initWidgets()
	 */
	@Override
	void initWidgets() {
		shell.addListener(SWT.Close,new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				exit();
				display.dispose();
				setUserCommand(commands.get("exit"));
			}

		});
		//sets the background image to white
		shell.setBackground(new Color(null,255,255,255));
		shell.setLayout(new GridLayout(2,false));
		shell.setText("Maze Game"); //sets the text of window
		//creates a tool bar
		Menu menuBar = new Menu(shell, SWT.BAR);
		//creates a file category in toolbar
		MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeFileMenu.setText("&File");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeFileMenu.setMenu(fileMenu);


		//creates a help category in toolbar
		MenuItem cascadeHelpMenu = new MenuItem(menuBar, SWT.CASCADE);
		cascadeHelpMenu.setText("&Help");
		Menu HelpMenu = new Menu(shell, SWT.DROP_DOWN);
		cascadeHelpMenu.setMenu(HelpMenu);


		//add item to file menu open properties
		MenuItem item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Open Properties");
		item.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {


			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fd=new FileDialog(shell,SWT.OPEN); //opens a dialog box in which we can select a xml file and load it
				fd.setText("open");
				fd.setFilterPath("C:\\");
				String[] filterExt = { "*.xml"};
				fd.setFilterExtensions(filterExt);
				String filename=fd.open(); //choose the file
				if(filename!=null){
					setProperties(filename);
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							switch(preferences.getUi())
							{
							case CLI: //if the properties calls for CLI
								exit(); //dispose all data
								display.dispose(); //dispose display
								setUserCommand(commands.get("exit"));
								RunCLI demo=new RunCLI(); //call for a function that works with cli
								demo.startProgram(getPreferences());
								break;
							case GUI:
								exit();// dispose all and close timer task
								display.dispose();
								setUserCommand(commands.get("exit"));
								RunGUI demoG = new RunGUI(); //calls for a function that recreates a gui window
								demoG.start(getPreferences());
								break;
							default:
								return;	
							}
						}
					});
				}
			}

		});
		//new item to file menu
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Write Properties");
		item.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				display.asyncExec(new Runnable() {

					@Override
					public void run() {//this function works on the same basis as open Properties the only difference is the source of the Properties data here we recieve it directly from the user
						WritePropertiesGUI guiProp=new WritePropertiesGUI();
						if(guiProp.writeProperties(shell)!=-1)
						{
							preferences=Run.readPreferences();
							switch(preferences.getUi())
							{
							case CLI:
								exit();
								display.dispose();
								LastUserCommand= commands.get("exit");
								setChanged();
								notifyObservers();
								RunCLI demo=new RunCLI();
								demo.startProgram(getPreferences());
								break;
							case GUI:
								exit();
								display.dispose();
								LastUserCommand = commands.get("exit");
								setChanged();
								notifyObservers();
								RunGUI demoG = new RunGUI();
								demoG.start(getPreferences());
								break;
							default:
								return;	
							}
						}
					}
				});


			}

		});
		//new item for file menu
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Exit");
		item.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {


			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				exit();
				display.dispose();
				setUserCommand(commands.get("exit"));	
			}

		});

		item = new MenuItem(HelpMenu, SWT.PUSH);
		item.setText("About");
		item.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MessageBox messageBox = new MessageBox(shell,SWT.ICON_INFORMATION|SWT.OK);
				messageBox.setText("Information");
				messageBox.setMessage("This entire software was created by eran and ziv Enjoy It!");
				messageBox.open();

			}

		});
		shell.setMenuBar(menuBar);



		//buttons for generate maze
		Button generateButton=new Button(shell,SWT.PUSH);
		generateButton.setText("Generate Maze");
		generateButton.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		//button that solves the maze
		//creates an instance of boardWidget
		boardWidget=new MazeBoard(shell, SWT.NONE);// CommonBoard f(x)
		boardWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true,1,4));
		Button solveMaze = new Button (shell ,SWT.PUSH);
		solveMaze.setText("Solve the maze I give up");
		solveMaze.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		Button saveMaze = new Button (shell ,SWT.PUSH);
		saveMaze.setText("Save maze");
		saveMaze.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));

		Button loadMaze = new Button (shell ,SWT.PUSH);
		loadMaze.setText("Load maze");
		loadMaze.setLayoutData(new GridData(SWT.FILL,SWT.None,false,false,1,1));

		generateButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boardWidget.won=false;
				boardWidget.getShell().setBackgroundImage(null);
				//boardWidget.setVisible(true);
				dataRecieved=null;
				ClassInputDialog dlg = new ClassInputDialog(shell,MazeProperties.class);
				MazeProperties tempInput=(MazeProperties)dlg.open();

				if(tempInput!=null)
				{
					input=tempInput;
					mazeName=input.getMazeName();
					String z = "" +input.getFloors();
					String x = "" + input.getRows();
					String y = "" + input.getColumns();


					String [] args= {"generate", "maze", "3d",input.getMazeName(), z, x, y};
					Command command = commands.get("generate");
					command.setArguments(args);
					setUserCommand(command);
				}



				boardWidget.forceFocus();



			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});


		solveMaze.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {



			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {



				if(mazeName!=null && !boardWidget.won){

					String [] args= {"solve",mazeName};
					Command command= commands.get("solve");
					command.setArguments(args);
					setUserCommand(command);

				}
				else{ //if there is no maze to solve
					MessageBox messageBox = new MessageBox(shell,SWT.ICON_INFORMATION|SWT.OK);
					messageBox.setText("Information");
					messageBox.setMessage("No maze to solve");
					messageBox.open();

				}
			}

		});	

		saveMaze.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {



			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {


				try{

					String [] args= {"save", "maze" ,mazeName,mazeName+".txt"};
					Command command= commands.get("save");
					command.setArguments(args);
					setUserCommand(command);
					boardWidget.forceFocus();


				}
				catch (NullPointerException e) {
					MessageBox messageBox = new MessageBox(shell,SWT.ICON_INFORMATION|SWT.OK);
					messageBox.setText("Information");
					messageBox.setMessage("There is no maze name");
					messageBox.open();

					boardWidget.forceFocus();
				}
			}
		});	

		loadMaze.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {



			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {


				try{
					ClassInputDialog dlg = new ClassInputDialog(shell,LoadPrefernces.class);
					LoadPrefernces tempInput=(LoadPrefernces)dlg.open();
					mazeName=tempInput.mazeName;
					String [] args= {"load", "maze" ,tempInput.fileName,tempInput.mazeName};
					Command command= commands.get("load");
					command.setArguments(args);
					setUserCommand(command);
					boardWidget.forceFocus();



				}
				catch (NullPointerException e) {
					MessageBox messageBox = new MessageBox(shell,SWT.ICON_INFORMATION|SWT.OK);
					messageBox.setText("Information");
					messageBox.setMessage("Can not load maze");
					messageBox.open();
					boardWidget.forceFocus();
				}
			}
		});	

	}

	/**
	 * Sets the properties.
	 *
	 * @param filename the new properties
	 */
	protected void setProperties(String filename) {

		FileInputStream in;
		try {
			XMLDecoder d;
			in = new FileInputStream(filename);
			d=new XMLDecoder(in);
			preferences=(Preferences)d.readObject();
			d.close();
		} catch (FileNotFoundException e) {
			displayString("Error Loading Properties");
		}
	}

	/* (non-Javadoc)
	 * @see view.View#dirCommand(java.lang.String)
	 */
	@Override
	public void dirCommand(String fileName) {
		// not relevant for this view

	}
	
	/* (non-Javadoc)
	 * @see view.View#displayModel(view.Drawable)
	 */
	@Override
	public <T> void displayModel(Drawable<T> draw) {
		boardWidget.won = false;

		boardWidget.setVisible(true);
		boardWidget.setUserAskedForSolution(false);
		boardWidget.displayProblem(draw);

	}
	
	/* (non-Javadoc)
	 * @see view.View#displayCrossSectionBy(view.Drawable)
	 */
	@Override
	public <T> void displayCrossSectionBy(Drawable<T> draw) {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see view.View#displaySolution(algorithm.generic.Solution)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> void displaySolution(Solution<T> solution) {
		boardWidget.displaySolution((Solution<Position>)solution);

	}
	
	/* (non-Javadoc)
	 * @see view.View#setCommands(java.util.HashMap)
	 */
	@Override
	public void setCommands(HashMap<String, Command> commands) {
		this.commands=commands;

	}
	
	/* (non-Javadoc)
	 * @see view.View#displayString(java.lang.String)
	 */
	@Override
	public void displayString(String toPrint) {
		if(!boardWidget.isDisposed()){
			MessageBox messageBox = new MessageBox(shell,SWT.ICON_INFORMATION|SWT.OK);
			messageBox.setText("Information");
			messageBox.setMessage(toPrint);
			messageBox.open();
		}
	}
	
	/* (non-Javadoc)
	 * @see view.View#getUserCommand()
	 */
	@Override
	public Command getUserCommand() {

		return LastUserCommand;
	}
	
	/* (non-Javadoc)
	 * @see view.View#setUserCommand(presenter.Command)
	 */
	@Override
	public void setUserCommand(Command command) {
		LastUserCommand= command;
		setChanged();
		notifyObservers("New command"); 

	}
	
	/* (non-Javadoc)
	 * @see view.View#exit()
	 */
	@Override
	public void exit() {
		if(boardWidget!=null){

			boardWidget.destructBoard();
			boardWidget.dispose();
		}

	}
	
	/* (non-Javadoc)
	 * @see view.View#start()
	 */
	@Override
	public void start() {
		this.run();

	}

	/**
	 * Gets the preferences.
	 *
	 * @return the preferences
	 */
	public Preferences getPreferences() {
		return preferences;
	}

	/**
	 * Sets the preferences.
	 *
	 * @param preferences the new preferences
	 */
	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}
}




