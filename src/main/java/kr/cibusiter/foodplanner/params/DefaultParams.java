package kr.cibusiter.foodplanner.params;

import java.util.Map;

/**
 * Created by whydda on 2017-03-03.
 */
public class DefaultParams {
    private Map<String, Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void put(String key, Object value){
        this.map.put(key, value);
    }

    public Object get(String key){
        return map.get(key);
    }

    public String getString(String key) {
        return String.valueOf(map.get(key));
    }

    @Override
    public String toString() {
        return this.map.toString();
    }
}
