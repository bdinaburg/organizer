package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil 
{
	public static enum Months {JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER};
	
	/**
	 * expected date format mm/dd/yyyy
	 * @param date
	 * @return
	 */
	public static Date getDateObject(String date)
	{
		try {
			String month = date.substring(0,  date.indexOf("/"));
			int month_index = date.indexOf("/", date.indexOf("/") + 1);
			String day = date.substring(date.indexOf("/") + 1,  month_index);
			String year = date.substring(month_index + 1, date.length());
			if(year.length() == 2)
			{
				year = "19" + year;
			}
			return getDateObject(month, day, year);
		} catch (Exception e) {
			return null;
		} 
	}
	
	public static Date getDateObject(Months month, String day, String fourDigitYear) throws Exception
	{
		return getDateObject(month, day, fourDigitYear, "00", "00", "00");
	}
	
	public static Date getDateObject(String month, String day, String fourDigitYear) throws Exception
	{
		return getDateObject(fromStringToEnum(month), day, fourDigitYear, "00", "00", "00");
	}

	
	public static Date getDateObject(Months month, String day, String fourDigitYear, String hour, String minute, String second) throws Exception
	{
		if(hour.length() == 1)
		{
			hour = "0" + hour;
		}
		
		if(minute.length() == 1)
		{
			minute = "0" + minute;
		}
		
		if(second.length() == 1)
		{
			second = "0" + second;
		}


		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy/hh:mm:ss");
		//String strDate = formatter.format(new Date());

		String strMonth = fromEnumToString(month);
		
		Date pDate = null;
		try 
		{
			pDate = formatter.parse(day+"/" + strMonth + "/" + fourDigitYear + "/" + hour+":"+minute+":"+second);
		} 
		catch (ParseException e) 
		{ 
									
			e.printStackTrace();
		}
		return pDate;
	}

	
	public static Months fromStringToEnum(String month)
	{
		month = month.toUpperCase();
		switch (month)
		{
		case "01": case "1": case "JAN": case "JANUARY":
			return Months.JANUARY;
		case "02": case "2": case "FEB": case "FEBRUARY":
			return Months.FEBRUARY;
		case "03": case "3": case "MAR": case "MARCH":
			return Months.MARCH;
		case "04": case "4": case "APR": case "APRIL":
			return Months.APRIL;
		case "05": case "5": case "MAY": 
			return Months.MAY;
		case "06": case "6": case "JUN": case "JUNE":
			return Months.JUNE;
		case "07": case "7": case "JUL": case "JULY":
			return Months.JULY;
		case "08": case "8": case "AUG": case "AUGUST":
			return Months.AUGUST;
		case "09": case "9": case "SEP": case "SEPTEMBER":
			return Months.SEPTEMBER;
		case "10": case "OCT": case "OCTOBER":
			return Months.OCTOBER;
		case "11": case "NOV": case "NOVEMBER":
			return Months.NOVEMBER;
		case "12": case "DEC": case "DECEMBER":
			return Months.DECEMBER;
		default:
			//should never happen
		}
		return null;
	}

	
	private static String fromEnumToString(Months month)
	{
		switch (month)
		{
		case JANUARY:
			return "01";
		case FEBRUARY:
			return "02";
		case MARCH:
			return "03";
		case APRIL:
			return "04";
		case MAY:
			return "05";
		case JUNE:
			return "06";
		case JULY:
			return "07";
		case AUGUST:
			return "08";
		case SEPTEMBER:
			return "09";
		case OCTOBER:
			return "10";
		case NOVEMBER:
			return "11";
		case DECEMBER:
			return "12";
		default:
			//should never happen
		}
		return null;
	}
}
