# 前置技术

## Git

### Git全局设置

```bash
-- 设置用户名
git config --global user.name "itcast"

-- 设置用户邮箱
git config --global user.email "0604@1989.com"

-- 查看配置信息
git config --list
```

### 拉取项目

```bash
git clone 项目url
```

### 本地仓库

- 版本库：.git文件夹
- 工作区：包含.git文件夹的目录就是工作区
  - 工作区的两种状态
    1. untracked  未跟踪  未被纳入版本控制
    2. tracked 已跟踪  被纳入版本控制
       1. Unmodified 未修改状态
       2. Modified 已修改状态
       3. Staged 已暂存状态
- 暂存区：.git文件夹中有个index文件就是暂存区，是临时保存修改文件的地方



| 常用命令                  | 说明                                          |
| ------------------------- | --------------------------------------------- |
| git  add  文件            | 将文件的修改加入暂存区                        |
| git  commit  -m  日志信息 | 将暂存区的文件修改提交到版本空                |
| git  status               | 查看文件状态                                  |
| git  log                  | 查看当前所有日志                              |
| git  --no-pager  log      | 不分页查看日志                                |
| git  reflog               | 查看所有历史版本日志                          |
| git  reset                | 将暂存区的文件取消暂存、或者切换到指定版本git |
| git  reset  --hard  uuid  | 回退到指定的uuid版本                          |

- 关于代码提交的规范

  ![image-20230824213527551](https://raw.githubusercontent.com/ZXJ-OvO/picgo-img/master/202308242135599.png)



#### 远程仓库

| 常用命令                      | 说明                                           |
| ----------------------------- | ---------------------------------------------- |
| git  clone  仓库url           | 从远程仓库克隆                                 |
| git  remote                   | 查看远程仓库                                   |
| git  push                     | 推送到远程仓库（首次需要托管平台的账号、密码） |
| git  pull                     | 从远程仓库拉取                                 |
| git  pull  origin  master     | 拉取最新版本合并到本地仓库                     |
| git  remote  add  origin  url | 添加一个新远程仓库的地址                       |
| git  remote  -v               | 列出远程仓库的详细信息                         |
|                               |                                                |
|                               |                                                |



#### 分支操作

- git branch 		列出所有本地分支
- git branch -r 	列出所有远程分支
- git branch -a 	列出所有本地分支和远程分支



- 创建分支命令格式：git branch 分支名称



- 切换分支命令格式：git checkout 分支名称



- 推送至远程仓库分支命令格式：git push 远程仓库简称 分支命令



- 合并分支就是将两个分支的文件进行合并处理，命令格式：git merge 分支名

  > 要注意分支合并的方向，在master上时执行git merge b1 就是把b1合并到master分支



#### 标签操作

- git  tag                                             查看标签
- git  tag  [标签名]                                  创建标签
- git  push  [远程仓库简称]  [标签名]           将标签推送至远程仓库
- git  checkout  -b  [分支名]  [标签名]            检出标签



#### .gitignore

```bash
.git
logs
rebel.xml
target/
!.mvn/wrapper/maven-wrapper.jar
log.path_IS_UNDEFINED
.DS_Store
offline_user.md
*.class

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr
```





## Redis

- redis的端口：6379

- 客户端连接命令：redis-cli



### 数据类型

key-value，key为字符串，value有5种常用的数据类型：

- string：字符串

- hash：哈希，类似于Java的HashMap

- list：列表，可以有重复元素，类似于Java中的LinkedList

- set：无序集合，没有重复元素，类似于Java中的HashSet

- sorted set ：有序集合，不重复，每个元素关联一个分数，根据分数排序

- json

- stream



### 常用命令

#### String

- SET key value                 设置指定key的值

- GET key                         获取指定key的值

- SETEX key 时间值单位秒 value 设置指定key的值，并将 key 的过期时间设为 指定 秒

- SETNX key value              只有在 key 不存在时设置 key 的值



#### Hash

Redis  hash 是一个String类型的  field  value 的映射表，hash特别适合用于存储 对象  field相当于又是一个key-value

HSET  key  field value  将哈希表 key 中的字段 field 的值设为 value

HGET  key  field          获取存储在哈希表中指定字段的值

HDEL  key  field          删除存储在哈希表中的指定字段

HKEYS  key               获取哈希表中所有字段

HVALS  key                获取哈希表中所有值



#### List

Redis 列表是简单的字符串列表，按照插入顺序排序，常用命令：

LPUSH key value1 [value2]  将一个或多个值插入到列表头部

LRANGE key start stop  获取列表指定范围内的元素

RPOP key [数量] 移除并获取列表最后一个元素（或者指定移除的数量）

LLEN key  获取列表长度



#### Set

Redis set 是string类型的无序集合。集合成员是唯一的，集合中不能出现重复的数据，常用命令：

SADD key member1 [member2]  向集合添加一个或多个成员

SMEMBERS key  返回集合中的所有成员

SCARD key  获取集合的成员数

SINTER key1 [key2]  返回给定所有集合的交集

SUNION key1 [key2]  返回所有给定集合的并集

SREM key member1 [member2]  删除集合中一个或多个成员



#### ZSet

Redis有序集合是string类型元素的集合，且不允许有重复成员。每个元素都会关联一个double类型的分数。

ZADD key score1 member1 [score2 member2]  向有序集合添加一个或多个成员

ZRANGE key start stop [WITHSCORES]  通过索引区间返回有序集合中指定区间内的成员

ZINCRBY key increment member  有序集合中对指定成员的分数加上增量 increment

ZREM key member [member ...]  移除有序集合中的一个或多个成员



#### 通用

- 客户端

Redis 的通用客户端命令是不分数据类型的，都可以使用的命令：

keys  pattern  			查找所有符合给定模式( pattern)的 key 

exists  key  				检查给定 key 是否存在

type  key  				  返回 key 所储存的值的类型

del  key  					该命令用于在 key 存在是删除 key

expire  key timeout 	设置指定key的超时时间

ttl  key 						查看key的剩余生存时间(time to live)

persist  key 				移除key的生存时间，转换成一个持久的key



- 服务端

Redis 的通用服务端命令是用来查看redis服务器相关信息的:

dbsize 获取当前数据库中key的数量

select db_number 切换到指定的数据库，数据库索引号 index 用数字值指定，以 0 作为起始索引值

flushdb 清空当前数据库的所有的key

flushall 清空所有数据库的所有的key

info 获取redis服务器的各种信息及统计数据

monitor 实时打印出redis服务器接收到的命令(调试用)



# 苍穹外卖

> 本专栏用于记录开发过程中的疑难杂症和重要事项以及心得体会

## 登录

### 注意点

#### 缓存穿透

1. 缓存null
2. 布隆过滤器

#### 缓存击穿

1. 热点数据预加载
2. 互斥锁

#### 缓存雪崩

1. 设定随机TTL

```java
// 8、设置redis缓存
String tokenValue = JSONUtil.toJsonStr(employeeLoginVO);
String tokenKey = RedisConstant.LOGIN_USER_KEY + token;
long ttl = RandomUtil.randomLong(-5, 5) + RedisConstant.LOGIN_USER_TTL;
ops.set(tokenKey, tokenValue);
stringRedisTemplate.expire(tokenKey, ttl, TimeUnit.MINUTES);
```



#### JWT令牌

> 使用Hutool工具类

1. JwtUtil：核心工具类

   

#### MD5加密

```java
DigestUtils.md5DigestAsHex(password.getBytes());
```



#### 账号锁定

> 使用Hutool工具类

1. ServletUtil：核心工具类’

   ```java
   // 参数传递 HttpServletRequest
   String ip = ServletUtil.getClientIP(httpServletRequest);
   ```

2. 该工具类会尽可能获取代理ip下的真实ip，弊端是会得到ipv4、ipv6两个ip

```java

    ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

    // TODO: 2023/8/27 期望：用户密码输入3次，锁定账号一个小时  实际：由于ipv4、6的缘故，无法合并统计，因此试错次数是2的倍数
    // 思路：同ip下失败3次直接禁止该ip的登录操作1小时，即锁定账号的操作和IP绑定而不是和账号绑定，防止恶意试错误封他人账号
    // 如果客户端采用了代理ip，直接放行，因为代理服务商提供的ip数量有限，并且下方的工具类中已经尽可能的考虑了代理ip的问题

    // 1、只要redis中有该ip的key，并且value的值为3，就说明该ip已经被锁定了，直接抛出异常，不用继续往后走
    String clientIP = ServletUtil.getClientIP(httpServletRequest);
    String identifier;
    if (clientIP.contains(":")) {
        identifier = "IPv6-" + clientIP;
    } else {
        identifier = "IPv4-" + clientIP;
    }
    String wrongTime = ops.get(identifier);
    if (wrongTime != null && Integer.parseInt(wrongTime) >= 3) {
        throw new LoginFailedException(MessageConstant.ACCOUNT_LOCKED);
    }

    // 4、校验密码
    String dtoPwd = DigestUtils.md5DigestAsHex(password.getBytes());
    String dbPwd = employeeEntity.getPassword();
    if (!dbPwd.equals(dtoPwd)) {
        // 5、密码错误，redis中的value值+2，如果value值大于3，就设置该ip的key的过期时间为30分钟
        if (wrongTime == null) {
            ops.set(identifier, "1");
            stringRedisTemplate.expire(identifier, 30, TimeUnit.MINUTES);
        } else if (Integer.parseInt(wrongTime) <= 2) {
            ops.increment(identifier);
            ops.increment(identifier);
            stringRedisTemplate.expire(identifier, 30, TimeUnit.MINUTES);
        } else {
            stringRedisTemplate.expire(identifier, 30, TimeUnit.MINUTES);
        }
        throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
    }
```

#### 校验参数

1. 传统校验
2. JSR303

### 泛型约束

```java
@PostMapping("/login")
public Result<EmployeeLoginVO> login() {
    // 略
}
```

>  相比传统的直接返回R，使用泛型约束R<EmployeeLoginVO>，使得更加规范

### Builder取代G/Setter构造对象

```java
EmployeeLoginVO employeeLoginVO = new EmployeeLoginVO().builder()
        .id(employeeEntity.getId())
        .userName(username)
        .name(employeeEntity.getName())
        .token(token)
        .build();
```

前提是vo中使用了@Build注解，这个注解由lombok提供

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工登录返回的数据格式")
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("主键值")
    private Long id;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("jwt令牌")
    private String token;
}

```

### 枚举常量取代魔法值

例如，MessageConstant

### 读取配置文件中的值

```java
String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
```

1、书写配置文件中的自定义值

```yaml
sky:
  jwt:
    #管理端jwt信息
    admin-secret-key: itcast           # 设置jwt签名加密时使用的秘钥
    admin-ttl: 1296000000              # 设置jwt过期时间 15天
    admin-token-name: token            # 设置前端传递过来的令牌名称
    #移动端jwt信息
    user-secret-key: itheima
    user-token-name: authentication
    user-ttl: 1296000000
```

2、创建属性配置类读取配置文件

1. 必须使用@Component加入IoC容器
2. 使用@ConfigurationProperties(prefix = "sky.jwt") 读取文件的属性的前缀
3. 添加@Data注解、
4. 在需要使用的地方通过DI注入拿到对象，使用对象拿到元素

```java
@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}

```

```java
@Resource
private JwtProperties jwtProperties;

String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
```

