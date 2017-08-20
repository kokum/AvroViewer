package jp.stread.avro;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableRecord {
	public StringProperty name;
	public StringProperty value;

	public StringProperty nameProperty(){return name;}
	public StringProperty valueProperty(){return value;}

	public TableRecord(String _name, String _value) {
		this.name = new SimpleStringProperty(_name);
		this.value = new SimpleStringProperty(_value);
	}
}
