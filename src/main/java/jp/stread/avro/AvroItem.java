package jp.stread.avro;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AvroItem extends TreeItem<String> {
	
	private final Node listIcon = 
	        new ImageView(new Image(ClassLoader.getSystemResourceAsStream("images/list_packages.gif")));
	private Node itemIcon = null;
	private String nodeValue = null;
	private LinkedHashMap<?,?> nodeMap = null;
	public LinkedHashMap<?, ?> getNodeMap() {
		return nodeMap;
	}

	public String getNodeValue() {
		return nodeValue;
	}

	private int nodeType = 0;
	
	public int getNodeType() {
		return nodeType;
	}

	public final static int NODE_TYPE_STRING = 1;
	public final static int NODE_TYPE_MAP = 2;

	public AvroItem(String name, String value, Node graphic) {
		super(name, graphic);
		nodeType = NODE_TYPE_STRING;
		nodeValue = value;
	}
	public AvroItem(String name, LinkedHashMap<String, ?> map) {
		nodeType = NODE_TYPE_MAP;
		nodeMap = map;
		this.setValue(name);
		this.setGraphic(listIcon);
		this.addAll(map);
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addAll(LinkedHashMap<String,?> map) {
    		for(String key : map.keySet()) {
    			if(map.get(key) instanceof Map) {
    				//nodeMap = (LinkedHashMap)map.get(key);
    				this.getChildren().add(new AvroItem(key, (LinkedHashMap)map.get(key)));
    			}
    			else {
    				itemIcon = new ImageView(new Image(ClassLoader.getSystemResourceAsStream("images/icon_package.gif")));
    				if(map.get(key) instanceof Integer) {
        				nodeValue = String.valueOf(map.get(key));
    				}
    				else {
        				nodeValue = (String)map.get(key);
    				}
    				this.getChildren().add(new AvroItem(key, nodeValue, itemIcon));
    			}
    		}
        return true;
    }
}