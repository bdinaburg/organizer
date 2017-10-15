package transientPojos;

import java.util.Date;

public class ExifData {
	Double dblLatitude;
	Double dblLongitude;
	
	String strAddress;
	
	Date createDate;

	public Double getDblLatitude() {
		return dblLatitude;
	}

	public void setDblLatitude(Double dblLatitude) {
		this.dblLatitude = dblLatitude;
	}

	public Double getDblLongitude() {
		return dblLongitude;
	}

	public void setDblLongitude(Double dblLongitude) {
		this.dblLongitude = dblLongitude;
	}

	public String getStrAddress() {
		return strAddress;
	}

	public void setStrAddress(String strAddress) {
		this.strAddress = strAddress;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String toString()
	{
		String str = "EXIF DATA: \n";
		str += "latitude: " + this.dblLatitude +"\n";
		str += "longitude: " + this.dblLongitude +"\n";
		str += "create date: " + this.getCreateDate() +"\n"; 
		str += "location: " + this.getStrAddress() + "\n";
		return str;
		
	}
	
	
}
