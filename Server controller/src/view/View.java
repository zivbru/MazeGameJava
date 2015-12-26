package view;

import java.util.concurrent.ConcurrentHashMap;

import presenter.RemoteControlCommand;

/**
 * The Interface View. This class is an interface of the view as
 *part of the MVP Structure later to be implemented
 *by other classess
 */
public interface View {


	/**
	 * Gets the command.
	 *
	 * @return the command
	 */
	public RemoteControlCommand getCommand();

	/**
	 * Sets the commands.
	 *
	 * @param commandMap the command map
	 */
	public void setCommands(ConcurrentHashMap<String, RemoteControlCommand> commandMap);

	/**
	 * Display.
	 *
	 * @param msg the msg
	 */
	public void Display(String msg);

	/**
	 * Save data.
	 *
	 * @param data the data
	 */
	public void saveData(String data);

	/**
	 * Adds the client.
	 *
	 * @param Client the client
	 */
	public void addClient(String Client);

	/**
	 * Removes the client.
	 *
	 * @param Client the client
	 */
	public void removeClient(String Client);

	/**
	 * Sets the user command.
	 *
	 * @param userCommand the new user command
	 */
	public void setUserCommand(RemoteControlCommand userCommand);

	/**
	 * Display status.
	 *
	 * @param msg the msg
	 */
	void DisplayStatus(String msg);

}
