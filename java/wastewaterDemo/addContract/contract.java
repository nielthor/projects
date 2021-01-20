package wastewaterTeam7.addContract;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import wastewaterTeam7.Main;

public class contract {
	private IntegerProperty plantIDProperty, contractProperty, startProperty, endProperty;
	private StringProperty contractOwerProperty, subContractorProperty;
	
	public contract() {
		this.plantIDProperty = new SimpleIntegerProperty();
		this.contractProperty = new SimpleIntegerProperty();
		this.startProperty = new SimpleIntegerProperty();
		this.endProperty = new SimpleIntegerProperty();
		this.contractOwerProperty = new SimpleStringProperty();
		this.subContractorProperty = new SimpleStringProperty();

	}
	//ower name
	public String getOw() {
		return contractOwerProperty.get();
	}
	public void setOw(String ow) {
		this.contractOwerProperty.set(ow);
	}
	public StringProperty getOwner() {
		return contractOwerProperty;
	}
	//sub contrator name
	public String getSb() {
		return subContractorProperty.get();
	}
	public void setSb(String sub) {
		this.subContractorProperty.set(sub);
	}
	public StringProperty getSubcontractor() {
		return subContractorProperty;
	}

	//plant access 
	public Integer getPID() {
		return plantIDProperty.get();
	}
	public void setPID(int op) {
		this.plantIDProperty.set(op);

	}
	public IntegerProperty getPlantID() {
		return plantIDProperty;
	}
	//Developer access 
	public Integer getCo() {
		return contractProperty.get();
	}
	public void setCo(int de) {
		this.contractProperty.set(de);
	}
	public IntegerProperty getContractNum() {
		return contractProperty;
	}
	//admin access 
	public Integer getSt() {
		return startProperty.get();
	}
	public void setSt(int st) {
		this.startProperty.set(st);
	}
	public IntegerProperty getStart() {
		return startProperty;
	}
	//stop access 
	public Integer getEd() {
		return endProperty.get();
	}
	public void setEd(int ed) {
		this.endProperty.set(ed);
	}
	public IntegerProperty getEnd() {
		return endProperty;
	}


}
