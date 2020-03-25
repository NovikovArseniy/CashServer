import java.util.HashMap;

public class ServerRepository {

    private HashMap<Integer, String> dataMap;
    public ServerRepository(){
        dataMap = new HashMap<>();
    }

    public String insertData(int id, String data){
        if (dataMap.containsKey(id)){
            dataMap.put(id, dataMap.get(id).concat(", ".concat(data)));
        } else {
            dataMap.put(id, data);
        }
        return "Data inserted";
    }

    public String getData(int id){
        if (dataMap.containsKey(id)) return dataMap.get(id);
        else return "ID doesn't exist";
    }

    public String deleteData(int id){
        dataMap.remove(id);
        return "Data deleted";
    }
}
