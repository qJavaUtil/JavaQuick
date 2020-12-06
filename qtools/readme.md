
## java常用工具类 

### 类型转换工具 
> Converter 类包含常用类型转换,如 16进制字符串转 toBytesByFormat(String content, String split) ,详细使用,见源码 

### 参数校验工具  
> 非空校验,有效值校验  
``` java 
  // 如果空值,就抛出异常, 这里的Obj是泛型,支持常见类:string,file等
  CheckUtils.objectCheckNull(Obj, "协议根路径不得为空," , "1001", null);
  // 如果非空值,就抛出异常, 这里的Obj是泛型,支持常见类:string,file等
  CheckUtils.objectCheckNotNull(Obj, "协议根路径必须为空," , "1002", null);

  // 邮箱校验 
  CheckUtils.STR.isEmailCheck("123@11.com", "邮箱格式错误", "1001", null);
  // 手机号校验 
  CheckUtils.STR.isEmailCheck("12123", "手机号格式错误", "1001", null);
  // 校验文件
  CheckUtils.FILE.checkFile("E:/xxx", "不是文件或文件不存在", "1001", null);
  // 校验文件夹
  CheckUtils.FILE.checkFile("E:/xxx", "不是文件夹或文件夹不存在", "1001", null);
```

### 网络工具
> 获取本地ip, 测试远端ip和端口 
``` java 
// 获取本地所有ipv4地址 
IPTools.getLocalHostIpsIPv4()
// 判断远端主机ip是否存在
IPTools.checkHostIP("127.0.0.1",  3000);
// 测试远端端口是否开放 
IPTools.checkHostIP("centos.zhangjialei.cn", 26, 3000);
``` 