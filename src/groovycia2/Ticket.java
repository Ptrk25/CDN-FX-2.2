package groovycia2;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class Ticket implements Serializable {
	public enum Type {ESHOP,DLP,DEMO,UPDATE,DLC,DSIWARE,DSISYSAPP,DSISYSDAT,SYSTEM,MYSTERY,NONE}

	private StringProperty name, region, serial, titleid, consoleid;
	private String titlekey;
	private int commonKeyIndex;
	private byte[] data;
	private StringProperty type;
	private BooleanProperty download;

	public Ticket(){
		this.name = new SimpleStringProperty("");
		this.region = new SimpleStringProperty("");
		this.serial = new SimpleStringProperty("");
		this.type = new SimpleStringProperty("");
		this.titleid = new SimpleStringProperty("");
		this.consoleid = new SimpleStringProperty("");
		this.data = null;
		this.commonKeyIndex = 0;
		this.download = new SimpleBooleanProperty(false);
		this.titlekey = "";
	}

	public Ticket(String name, String region, String serial, String titleid){
		this.name = new SimpleStringProperty(name);
		this.region = new SimpleStringProperty(region);
		this.type = new SimpleStringProperty("");
		this.serial = new SimpleStringProperty(serial);
		this.titleid = new SimpleStringProperty(titleid);
		this.consoleid = new SimpleStringProperty("");
		this.download = new SimpleBooleanProperty(false);
		this.titlekey = "";

	}

	public Ticket(byte[] data, String titleid, String consoleid, int commonKeyIndex){
		this.data = data;
		this.name = new SimpleStringProperty("");
		this.region = new SimpleStringProperty("");
		this.type = new SimpleStringProperty("");
		this.serial = new SimpleStringProperty("");
		this.titleid = new SimpleStringProperty(titleid);
		this.consoleid = new SimpleStringProperty(consoleid);
		this.download = new SimpleBooleanProperty(true);
		this.commonKeyIndex = commonKeyIndex;
		this.titlekey = "";
	}

	public Ticket(String titleID, String titleKey){
		this.name = new SimpleStringProperty("");
		this.region = new SimpleStringProperty("");
		this.serial = new SimpleStringProperty("");
		this.type = new SimpleStringProperty("");
		this.titleid = new SimpleStringProperty(titleID);
		this.consoleid = new SimpleStringProperty("");
		this.data = null;
		this.commonKeyIndex = 0;
		this.download = new SimpleBooleanProperty(false);
		this.titlekey = titleKey;
	}

	public String getName(){
		return this.name.get();
	}

	public String getRegion(){
		return this.region.get();
	}

	public String getSerial(){
		return this.serial.get();
	}

	public String getType(){
		return type.get();
	}

	public String getTitleID(){
		return this.titleid.get();
	}

	public String getTitleKey(){
		return this.titlekey;
	}

	public String getConsoleID(){
		return this.consoleid.get();
	}

	public Boolean getDownload(){
		return this.download.get();
	}

	public int getCommonKeyIndex(){
		return this.commonKeyIndex;
	}

	public byte[] getData(){
		return this.data;
	}

	public void setName(String name){
		this.name.set(name);
	}

	public void setRegion(String region){
		this.region.set(region);
	}

	public void setSerial(String serial){
		this.serial.set(serial);
	}

	public void setType(Type type){
		if(type == Type.ESHOP)
			this.type.set("eShopApp");
		if(type == Type.DLP)
			this.type.set("DLP");
		if(type == Type.DEMO)
			this.type.set("Demo");
		if(type == Type.UPDATE)
			this.type.set("UpdatePatch");
		if(type == Type.DLC)
			this.type.set("DLC");
		if(type == Type.DSIWARE)
			this.type.set("DSiWare");
		if(type == Type.DSISYSAPP)
			this.type.set("DSiSysApp");
		if(type == Type.DSISYSDAT)
			this.type.set("DSiSysDat");
		if(type == Type.SYSTEM)
			this.type.set("System");
		if(type == Type.MYSTERY)
			this.type.set("Mystery");
		if(type == Type.NONE)
			this.type = null;
	}

	public void setTitleID(String titleid){
		this.titleid.set(titleid);
	}

	public void setTitleKey(String titleKey){
		this.titlekey = titleKey;
	}

	public void setConsoleID(String consoleid){
		this.consoleid.set(consoleid);
	}

	public void setDownload(Boolean download){
		this.download.set(download);
	}

	public void setCommonKexIndex(int commonKeyIndex){
		this.commonKeyIndex = commonKeyIndex;
	}

	public void setData(byte[] data){
		this.data = data;
	}

	public StringProperty nameProperty(){
		return name;
	}

	public StringProperty regionProperty(){
		return region;
	}

	public StringProperty serialProperty(){
		return serial;
	}

	public StringProperty typeProperty(){
		return type;
	}

	public StringProperty titleidProperty(){
		return titleid;
	}

	public StringProperty consoleidProperty(){
		return consoleid;
	}

	public BooleanProperty downloadProperty(){
		return download;
	}
}
