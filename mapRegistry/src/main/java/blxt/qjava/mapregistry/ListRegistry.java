package blxt.qjava.mapregistry;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 注册表维护
 * @author OpenJialei
 * @date 2021年10月21日 14:19
 */
public class ListRegistry<T> {

    protected List<T> registryMaps = new ArrayList<>();

    public ListRegistry(){
        registryMaps = new ArrayList<>(16);
    }

    public ListRegistry(int mapSize){
        registryMaps = new ArrayList<>(mapSize);
    }

    public ListRegistry(List<T> registryMaps){
        this.registryMaps = registryMaps;
    }

    /**
     * 获取 注册表值
     * @param index
     * @return
     */
    public T getValue(int index){
        return registryMaps.get(index);
    }

    /**
     * 获取 注册表值 列表
     * @return
     */
    public List<T> getRegistryValues(){
        return registryMaps;
    }

    /**
     * 注册
     * @param nature  注册值
     */
    public void register(T nature){
        registryMaps.add(nature);
    }

    /**
     * 移除
     * @param key
     */
    public boolean remove(T key){
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

    /***
     * 判断是否存在
     * @param value
     * @return
     */
    public boolean containsKey(T value){
        AtomicBoolean res = new AtomicBoolean(false);
        registryMaps.forEach(k ->{
            if(value == k){
                res.set(true);
                return;
            }
            else if(value != null && value.equals(k)){
                res.set(true);
                return;
            }
        });
        return res.get();
    }
}
