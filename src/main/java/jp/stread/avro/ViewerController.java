package jp.stread.avro;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class ViewerController {
	
	private final Node rootIcon = 
	        new ImageView(new Image(ClassLoader.getSystemResourceAsStream("images/page_package.gif")));
	private String rootName = null;
	
	final FileChooser fileChooser = new FileChooser();
	
	@FXML private Button openButton;
	@FXML private TreeView<String> tree1;
	
	@FXML private TableView<TableRecord> contentTable;
	@FXML private TableColumn<TableRecord,String> nameColumn;
	@FXML private TableColumn<TableRecord,String> valueColumn;

	@FXML private Label statusText;

	private ObservableList<TableRecord> tableRecord = FXCollections.observableArrayList();
	
	//private final static String initialDir = "/works/eclipse-oxygen/AvroViewer/src/test/resources";
	
	public void initialize() {
		setTable();
	}
    public void setTable() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<TableRecord, String>("name"));
		valueColumn.setCellValueFactory(new PropertyValueFactory<TableRecord, String>("value"));
		tableRecord.add(new TableRecord("", ""));
		contentTable.setItems(tableRecord);
    }

	private List<LinkedHashMap<String,String>> openAvro(File file) {
		AvroReader reader = new AvroReader(file);
		rootName = reader.getRootName();
		List<LinkedHashMap<String,String>> items = reader.read();
		return items;
	}
	
    public void buildTree(List<LinkedHashMap<String,String>> list) {
        final TreeItem<String> rootNode = new TreeItem<>("root", rootIcon);
        int i = 0;
    		for(LinkedHashMap<String,?> recordMap : list) {
    			i++;
    			rootNode.getChildren().add(new AvroItem(rootName+"["+i+"]", recordMap));
    		}
    		rootNode.setExpanded(true);
        tree1.setRoot(rootNode);
        tree1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(
                    ObservableValue<? extends TreeItem<String>> observable,
                    TreeItem<String> old_val, TreeItem<String> new_val) {
            			AvroItem selectedItem = (AvroItem)new_val;
                itemSelected(selectedItem);
            }
        });
    }

    @FXML
    public void onbtnOpenClicked(ActionEvent event) {
    		//File init = new File(initialDir);
    		//fileChooser.setInitialDirectory(init);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
	    		List<LinkedHashMap<String,String>> items = openAvro(file);
	    		buildTree(items);
        }
    }
	
    public void itemSelected(AvroItem target) {
    		tableRecord.clear();
		nameColumn.setCellValueFactory(new PropertyValueFactory<TableRecord, String>("name"));
		valueColumn.setCellValueFactory(new PropertyValueFactory<TableRecord, String>("value"));
		if(target.getNodeType()==AvroItem.NODE_TYPE_MAP) {
			statusText.setText("");
		    	for (Map.Entry<?,?> map : target.getNodeMap().entrySet()) {
    				if(map.getValue() instanceof Integer) {
    					tableRecord.add(new TableRecord((String)map.getKey(), String.valueOf(map.getValue())));
    				}
    				else if(map.getValue() instanceof String) {
    		    			tableRecord.add(new TableRecord((String)map.getKey(), (String)map.getValue()));
    				}
		    	}
	    		contentTable.setItems(tableRecord);
		}
		else if(target.getNodeType()==AvroItem.NODE_TYPE_STRING) {
			statusText.setText(target.getNodeValue());
		}
    }
	

}
