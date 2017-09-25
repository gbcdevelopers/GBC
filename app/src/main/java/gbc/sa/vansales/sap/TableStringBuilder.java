package gbc.sa.vansales.sap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/**
 * Created by Rakshit on 31-Jan-17.
 */
public class TableStringBuilder {

    HashMap<String,String>map = new HashMap<>();
    StringBuilder builder = new StringBuilder();
    private final List<String> columnNames;
    private final List<String> dataArray;

    private TableStringBuilder(HashMap<String,String>map){
        this.map = map;
        columnNames = new ArrayList<String>();
        dataArray = new ArrayList<String>();
    }

    void addColumn(String columnName)
    {
        columnNames.add(columnName);
       // stringFunctions.add((p) -> (String.valueOf(fieldFunction.apply(p))));
    }



    private void createTable(){
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String column = iterator.next();
            columnNames.add(column);
            String data = map.get(column);
            dataArray.add(data);
        }
    }
}
