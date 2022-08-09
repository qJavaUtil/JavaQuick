package blxt.qjava.mapregistry;

import blxt.qjava.utils.SnowflakeIdUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册表维护
 * @author OpenJialei
 * @date 2021年10月21日 14:19
 */
public class MapRegistry<K, T> {
    /** 雪花id生成器 */
    SnowflakeIdUtils idWorker = new SnowflakeIdUtils(4, 1);

    /** 注册表维护  */
    Map<K, T> registryMaps = null;

    public MapRegistry(){
        registryMaps = new ConcurrentHashMap<>(16);
    }

    public MapRegistry(int mapSize){
        registryMaps = new ConcurrentHashMap<>(mapSize);
    }

    public MapRegistry(Map<K, T> registryMaps){
        this.registryMaps = registryMaps;
    }

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
    public List<T> getRegistryKeys(){
        return new ArrayList<T>((Collection<? extends T>) registryMaps.keySet());
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

    /**
     * 移除
     * @param key
     */
    public boolean remove(K key){
        registryMaps.remove(key);
        return true;
    }

    /**
     * 数量
     * @return
     */
    public int size(){
        return registryMaps.size();
    }

    /**
     * 清理
     */
    public void clear(){
        registryMaps.clear();
    }

    /** 获取id */
    public long getId(){
        return idWorker.nextId();
    }

    /** 获取id */
    public String getStringId(){
        return String.valueOf(idWorker.nextId());
    }

    /***
     * 判断是否存在
     * @param key
     * @return
     */
    public boolean containsKey(K key){
        return registryMaps.containsKey(key);
    }
}
