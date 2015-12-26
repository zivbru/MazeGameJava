package view;

import org.eclipse.swt.graphics.ImageData;


/**
 * The Interface Character.Represent a characther in the Board {@see Board}
 */
public interface Character {

	/**
	 * Draw character.
	 */
	public void drawCharacter();

	/**
	 * Gets the character images array.
	 *
	 * @return the character images array
	 */
	public ImageData[] getCharacterImagesArray();

	/**
	 * Sets the character image array.
	 *
	 * @param image the new character image array
	 */
	public void setCharacterImageArray(ImageData[] image);

	/**
	 * Gets the character image index.
	 *
	 * @return the character image index
	 */
	public int getCharacterImageIndex();

	/**
	 * Sets the character image index.
	 *
	 * @param index the new character image index
	 */
	public void setCharacterImageIndex(int index);

}
