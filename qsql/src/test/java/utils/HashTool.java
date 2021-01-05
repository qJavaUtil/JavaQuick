package utils;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/10/10 14:13
 */
public class HashTool {
    static int[] codeTable ;
    static final int BASE_NAMBER = 0x7F - 0x20 + 1;

    static {
        init();
    }

    private static void init(){
        codeTable = new int[0x7F];
        for(int i = 0; i < codeTable.length; i++ ){
            codeTable[i] =  i;
        }

        for(int i = 48; i <= 57; i++){
            char c = (char)codeTable[i];
            codeTable[i] = (codeTable[i] - '0');
         //   System.out.println(  c + ":" + codeTable[i]);
        }

        for(int i = 65; i <= 90; i++){
            char c = (char)codeTable[i];
            codeTable[i] = (codeTable[i] - 'A' + 10);
          //  System.out.println(  c + ":" + codeTable[i]);
        }

        for(int i = 97; i <= 122; i++){
            char c = (char)codeTable[i];
            codeTable[i] = (codeTable[i] - 'a' + 10);
            //System.out.println(  c + ":" + codeTable[i]);
        }
    }

    /**
     * 自定义hash计算,经验证,计算15字符长度100w次,比自带方法多耗时20ms左右,可以忽略不计
     * 增加离散度,大幅减少重复冲突
     * @param value
     * @return
     */
    public static int getcode(String value){
        int hashCode = 0;

        if (value.length() > 0) {
            char[] var2 = value.toCharArray();

            for(int var3 = 0; var3 < value.length(); ++var3) {
               // hashCode =   + ;
                char c = var2[var3];
                hashCode += Math.pow(codeTable[c], var3);
            }

        }

        return hashCode ;
    }

}
