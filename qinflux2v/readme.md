
### java快速开发工具 - sql帮助

* 现在支持: 
1. redis  
2. influxdb 
3. postgresql 

### 基本用法 

* redis 
``` java 
// 创建基础连接实体
/** 连接地址 主机192.168.0.118 */
String host = redisConfiguration.getHost();
/** 密码 */
String pwd = redisConfiguration.getPassword();
/** 端口 */
int port = redisConfiguration.getPort();
/** 数据分区一般是0-16 */
int database = redisConfiguration.getDatabase();
/** 默认key有效时间 */
int expxTime = redisConfiguration.getExpxtime();
int maxActive = redisConfiguration.getMaxActive();
int maxIdle = redisConfiguration.getMaxIdle();
int maxWait = redisConfiguration.getMaxWait();
int minIdle = redisConfiguration.getMinIdle();
int redisTimeout = redisConfiguration.getRedis_timeout();

RedisBean redisBean = new RedisBean(host, port, pwd, database, expxTime,
		maxActive, maxIdle, maxWait, minIdle, redisTimeout);
// 创建连接管理器 		
RedisConnetionManager redisConnetionManager = RedisConnetionManager.newInstance(redisBean);		
// 创建新连接
RedisConnetion redisConnetion = redisConnetionManager.add(Constant.REDIS_NAME, redisBean);

// 使用
redisConnetion = RedisConnetionManager.getInstance().get("MQTT.Publish.device.state.change");
```

* influx 
``` java 
// 创建基础连接实体
String host = configuration.getHost();
int port = configuration.getPort();
String database = configuration.getDatabase();
String authUname = configuration.getAuthUname();
String authPwd = configuration.getAuthPwd();
// 保存策略名
String retentionPolicy = configuration.getReplication();

// 创建连接管理器 
InfluxConnectionManager influxConnectionManager =
		InfluxConnectionManager.newInstance(authUname, authPwd,
				"http://" + host + ":" + port, database, retentionPolicy);
// 创建新连接
// 默认创建一个设备-用户表,如果连接不上,那就说明数据库没有连上
InfluxConnection connection = influxConnectionManager.add("system.user-dev");
// 使用
connection = influxConnectionManager.get("system.user-dev");
``` 

* Postgresql 
``` java
// 创建连接信息
/** 连接地址 主机192.168.0.118 */
String host = postgresqlConfiguration.getHost();
/** 密码 */
String uname = postgresqlConfiguration.getUsername();
/** 密码 */
String pwd = postgresqlConfiguration.getPassword();
/** 端口 */
int port = postgresqlConfiguration.getPort();
/** 数据分区一般是0-16 */
String database = postgresqlConfiguration.getDatabase();

// 创建连接管理器
PostgreBean postgreBean = new PostgreBean(host, port, database, uname, pwd);
PostgreConnetionManager postgreConnetionManager = PostgreConnetionManager
		.newInstance(postgreBean);
if(postgreConnetionManager == null){
	return false;
}
// 添加连接
postgreConnetionManager.add(POSTGRESQL_NAME, postgreBean);
// 使用
PostgreConnetionBase connect = postgreConnetionManager.get(POSTGRESQL_NAME, postgreBean);

```