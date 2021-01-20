package wastewaterTeam7.user;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class users {
	
	private IntegerProperty operatorProperty, developerProperty, adminProperty;
	private StringProperty fnameProperty, lnameProperty, userNamePorperty, passwordPorperty, accountProperty;
	
	public users() {
		this.operatorProperty = new SimpleIntegerProperty();
		this.developerProperty = new SimpleIntegerProperty();
		this.adminProperty = new SimpleIntegerProperty();
		this.fnameProperty = new SimpleStringProperty();
		this.lnameProperty = new SimpleStringProperty();
		this.userNamePorperty = new SimpleStringProperty();
		this.passwordPorperty = new SimpleStringProperty();
		this.accountProperty = new SimpleStringProperty();
	}
	//first name
	public String getFName() {
		return fnameProperty.get();
	}
	public void setFName(String firstName) {
		this.fnameProperty.set(firstName);
	}
	public StringProperty getFirstName() {
		return fnameProperty;
	}
	//last name
	public String getLName() {
		return lnameProperty.get();
	}
	public void setLName(String lastName) {
		this.lnameProperty.set(lastName);
	}
	public StringProperty getLastName() {
		return lnameProperty;
	}
	//user name
	public String getUserName() {
		return userNamePorperty.get();
	}
	public void setUserName(String userNmae) {
		this.userNamePorperty.set(userNmae);
	}
	public StringProperty getUsrName() {
		return userNamePorperty;
	}

	//password 
	public String getPassword() {
		return passwordPorperty.get();
	}
	public void setPassword(String pswd) {
		this.passwordPorperty.set(pswd);
	}
	public StringProperty getPasswrd() {
		return passwordPorperty;
	}
	//operator access 
	public Integer getOpInt() {
		return operatorProperty.get();
	}
	public void setOpInt(int op) {
		this.operatorProperty.set(op);
		if(op == 1)
			setAccountType("Operator");
	}
	public IntegerProperty getOperator() {
		return operatorProperty;
	}
	//Developer access 
	public Integer getDevInt() {
		return developerProperty.get();
	}
	public void setDevInt(int de) {
		this.developerProperty.set(de);
		if(de == 1)
			setAccountType("Developer");
	}
	public IntegerProperty getDeveloper() {
		return developerProperty;
	}
	//admin access 
	public Integer getAdInt() {
		return adminProperty.get();
	}
	public void setAdInt(int ad) {
		this.adminProperty.set(ad);
		if(ad == 1)
			setAccountType("Admin");
	}
	public IntegerProperty getAdmin() {
		return adminProperty;
	}
	//account type
	public StringProperty getAccount() {
		return accountProperty;
	}
	private void setAccountType(String acntType) {
		this.accountProperty.set(acntType);
	}
	public String getAcntType() {
		return accountProperty.get();
	}

}
