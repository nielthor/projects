package wastewaterTeam7.Equipment;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class equipment {
	private IntegerProperty plantIDProperty;
	private StringProperty processProperty, eqTagProperty, plcIDProperty;
	
	public equipment() {
		this.plantIDProperty = new SimpleIntegerProperty();
		this.plcIDProperty = new SimpleStringProperty();		
		this.processProperty = new SimpleStringProperty();
		this.eqTagProperty = new SimpleStringProperty();

	}
	//process name
	public String getPName() {
		return processProperty.get();
	}
	public void setPName(String name) {
		this.processProperty.set(name);
	}
	public StringProperty getProcessName() {
		return processProperty;
	}
	//eq tag
	public String getEQ() {
		return eqTagProperty.get();
	}
	public void setEQ(String eq) {
		this.eqTagProperty.set(eq);
	}
	public StringProperty getEqTag() {
		return eqTagProperty;
	}
	//plc id
	public String getPLC() {
		return plcIDProperty.get();
	}
	public void setPLC(String pl) {
		this.plcIDProperty.set(pl);
	}
	public StringProperty getPLCID() {
		return plcIDProperty;
	}

	//plant Id
	public Integer getPID() {
		return plantIDProperty.get();
	}
	public void setPID(int id) {
		this.plantIDProperty.set(id);

	}
	public IntegerProperty getPantID() {
		return plantIDProperty;
	}

}
