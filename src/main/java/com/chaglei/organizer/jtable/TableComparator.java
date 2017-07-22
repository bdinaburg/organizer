package com.chaglei.organizer.jtable;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public class TableComparator implements Comparator<Object> {

    protected boolean isSortAsc;

    public TableComparator(boolean sortAsc) {
        isSortAsc = sortAsc;
    }

	@Override
	public int compare(Object obj1, Object obj2) {
		if (obj1 instanceof Date && obj2 instanceof Date) {
			Date date1 = (Date) obj1;
			Date date2 = (Date) obj2;
			int result = date1.compareTo(date2);
			if (!isSortAsc) {
				result = -result;
			}
			return result;
		}
		if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
			BigDecimal number1 = (BigDecimal) obj1;
			BigDecimal number2 = (BigDecimal) obj2;
			int result = number1.compareTo(number2);
			if (!isSortAsc) {
				result = -result;
			}
			return result;
		}
		
		if(obj1 == null)
		{
			obj1 = new String("");
		}
		if(obj2 == null)
		{
			obj2 = new String("");
		}
		
		return obj1.toString().compareToIgnoreCase(obj2.toString());
	}
}