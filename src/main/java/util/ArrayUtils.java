package util;

public class ArrayUtils {
	
	/**
	 * Used to add an item to a full array
	 * @param intArray
	 * @param intItem
	 * @return
	 */
	public static int[] addItemToArray(int[] intArray, int intItem)
	{
		int[] newIntArray = new int[intArray.length + 1];
		System.arraycopy(intArray, 0, newIntArray, 0, intArray.length);
		newIntArray[newIntArray.length -1] = intItem;
		
		return newIntArray;
	}

}
