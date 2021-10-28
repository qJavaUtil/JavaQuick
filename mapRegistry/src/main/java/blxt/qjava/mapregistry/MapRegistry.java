package blxt.qjava.mapregistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册表维护
 * @author OpenJialei
 * @date 2021年10月21日 14:19
 */
public class MapRegistry<K, T> {
    /** 注册表维护  */
    Map<K, T> registryMaps = new HashMap<>(16);

    /**
     * 获取 注册表值
     * @param key
     * @return
     */
    public T getValue(K key){
        return registryMaps.get(key);
    }

    /**
     * 获取 注册表值 列表
     * @return
     */
    public List<T> getRegistryValues(){
        return  new ArrayList<T>(registryMaps.values());
    }

    /**
     * 获取 注册列表
     * @return
     */
    public Map<K, T>  getRegistrys(){
        return registryMaps;
    }

    /**
     * 注册
     * @param key     注册key
     * @param nature  注册值
     */
    public void register(K key,T nature){
        registryMaps.put(key, nature);
    }

}
