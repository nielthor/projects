package wastewaterTeam7.PIO;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class pio {
	private IntegerProperty plantIDProperty, rackProperty, slotProperty, channelProperty;
	private StringProperty plcIDProperty, rschProperty, eqTagPorperty, eqPioPorperty;
	
	public pio() {
		this.plantIDProperty = new SimpleIntegerProperty();
		this.rackProperty = new SimpleIntegerProperty();
		this.slotProperty = new SimpleIntegerProperty();
		this.channelProperty = new SimpleIntegerProperty();
		
		this.plcIDProperty = new SimpleStringProperty();
		this.rschProperty = new SimpleStringProperty();
		this.eqTagPorperty = new SimpleStringProperty();
		this.eqPioPorperty = new SimpleStringProperty();
	}
	//plc id name
	public String getPID() {
		return plcIDProperty.get();
	}
	public void setPID(String id) {
		this.plcIDProperty.set(id);
	}
	public StringProperty getPLCID() {
		return plcIDProperty;
	}
	//rsch name
	public String getRS() {
		return rschProperty.get();
	}
	public void setRS(String rs) {
		this.rschProperty.set(rs);
	}
	public StringProperty getRSCH() {
		return rschProperty;
	}
	//eq name
	public String getEQ() {
		return eqTagPorperty.get();
	}
	public void setEQ(String eq) {
		this.eqTagPorperty.set(eq);
	}
	public StringProperty getEQTage() {
		return eqTagPorperty;
	}

	//pio 
	public String getPio() {
		return eqPioPorperty.get();
	}
	public void setPio(String pi) {
		this.eqPioPorperty.set(pi);
	}
	public StringProperty getDevicePIO() {
		return eqPioPorperty;
	}
	// plant id
	public Integer getPlt() {
		return plantIDProperty.get();
	}
	public void setPlt(int id) {
		this.plantIDProperty.set(id);
	}
	public IntegerProperty getPlantID() {
		return plantIDProperty;
	}
	//Developer access 
	public Integer getRk() {
		return rackProperty.get();
	}
	public void setRK(int rk) {
		this.rackProperty.set(rk);

	}
	public IntegerProperty getRack() {
		return rackProperty;
	}
	//slot 
	public Integer getST() {
		return slotProperty.get();
	}
	public void setST(int st) {
		this.slotProperty.set(st);

	}
	public IntegerProperty getSlot() {
		return slotProperty;
	}

	//channel 
	public Integer getCH() {
		return channelProperty.get();
	}
	public void setCH(int ch) {
		this.channelProperty.set(ch);

	}
	public IntegerProperty getChannel() {
		return channelProperty;
	}
}
