package wastewaterTeam7.plantInfo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class plant {
	
	private IntegerProperty plantIDProperty;
	private StringProperty nameProperty, operationsProperty, locationPorperty, addressProperty, phoneProperty, emailProperty, friendlyTag;
	
	public plant() {
		this.plantIDProperty = new SimpleIntegerProperty();
		this.nameProperty = new SimpleStringProperty();
		this.operationsProperty = new SimpleStringProperty();
		this.locationPorperty = new SimpleStringProperty();
		this.addressProperty = new SimpleStringProperty();
		this.addressProperty = new SimpleStringProperty();		
		this.phoneProperty = new SimpleStringProperty();
		this.emailProperty = new SimpleStringProperty();
		this.friendlyTag = new SimpleStringProperty();
	}
	
	//plant name
	public String getName() {
		return nameProperty.get();
	}
	public void setName(String name) {
		this.nameProperty.set(name);
	}
	public StringProperty getPlantName() {
		return nameProperty;
	}
	//plant operations
	public String getOp() {
		return operationsProperty.get();
	}
	public void setOp(String opType) {
		this.operationsProperty.set(opType);
	}
	public StringProperty getOperationType() {
		return operationsProperty;
	}	
	//plant location
	public String getLoc() {
		return locationPorperty.get();
	}
	public void setLoc(String loc) {
		this.locationPorperty.set(loc);
	}
	public StringProperty getPlantLocation() {
		return locationPorperty;
	}	
	//plant address
	public String getAd() {
		return addressProperty.get();
	}
	public void setAd(String ad) {
		this.addressProperty.set(ad);
	}
	public StringProperty getPlantAddress() {
		return addressProperty;
	}
	
	//plant phone
	public String getPh() {
		return phoneProperty.get();
	}
	public void setPh(String ph) {
		this.phoneProperty.set(ph);
	}
	public StringProperty getPlantPhone() {
		return phoneProperty;
	}
	//plant email
	public String getEm() {
		return emailProperty.get();
	}
	public void setEm(String em) {
		this.emailProperty.set(em);
	}
	public StringProperty getPlantEmail() {
		return emailProperty;
	}
	
	//plant email
	public String getFT() {
		return friendlyTag.get();
	}
	public void setFT(String em) {
		this.friendlyTag.set(em);
	}
	public StringProperty getFriendlyTag() {
		return friendlyTag;
	}
	//plant id
	public Integer getPID() {
		return plantIDProperty.get();
	}
	public void setPID(int pid) {
		this.plantIDProperty.set(pid);
	}
	public IntegerProperty getPlantID() {
		return this.plantIDProperty;
	}
}
