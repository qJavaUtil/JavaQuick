
import blxt.qjava.utils.network.IPTools;
import blxt.qjava.utils.network.bean.NetworkBasic;


/**
 * @author OpenJialei
 * @date 2021年10月14日 14:07
 */
public class TestNetworkAnalysis {

    public static void main(String[] args) {
        NetworkBasic networkBasic = IPTools.getNetworkBasic();
        System.out.println("解析后的Java对象：\r\n" + networkBasic);
    }


}

