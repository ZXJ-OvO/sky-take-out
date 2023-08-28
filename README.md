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



### RedisTemplate、SpringData

#### redis坐标

1、导入redis整合坐标

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### yaml

2、编写redis地址、端口

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

#### 配置序列化器

3、config包下创建redis配置类，指定redis序列化器

```java
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建redisTemplate模板对象
        RedisTemplate redisTemplate = new RedisTemplate();

        // 设置redisTemplate的连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置redis key-value的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}

```

#### 使用

如下使用包括SpringData（Java-redis操作客户端，底层基于lettuce）、命令

```java
 @Autowired
 private RedisTemplate<Object, Object> redisTemplate;

    //string类型 简单的key-value
    @Test
    public void testString() {
        //1.  setex 存 - 设置过期时间 60 S
        redisTemplate.opsForValue().set("name", "winnie pooh", 1, TimeUnit.MINUTES);

        //2. get 取
        System.out.println(redisTemplate.opsForValue().get("name"));

        //3. setnx 如果不存在再设置
        redisTemplate.opsForValue().setIfAbsent("name", "winnie pooh");

    }

    //list类型 特点：有序、可重复 适合做消息队列 栈  队列
    @Test
    public void testList() {
        //1. lpush
        redisTemplate.opsForList().leftPush("members", "winnie pooh");

        //2. rpush
        redisTemplate.opsForList().rightPush("members", "baby pig");

        //3. lrange
        redisTemplate.opsForList().range("members", 0, -1).forEach(System.out::println);

        //4. lpop
        redisTemplate.opsForList().leftPop("members");

        //5. rpop
        redisTemplate.opsForList().rightPop("members");

    }

    //set类型 特点：无序、不可重复
    @Test
    public void testSet() {
        //1. sadd 添加元素
        redisTemplate.opsForSet().add("hobby1", "eat", "sleep", "play", "box");
        redisTemplate.opsForSet().add("hobby2", "eat", "code", "play");

        //2. smembers 获取集合中的所有元素
        redisTemplate.opsForSet().members("hobby1").forEach(System.out::println);

        //3. scard 获取集合的长度
        System.out.println(redisTemplate.opsForSet().size("hobby2"));

        //4. sinter 获取多个集合的交集
        redisTemplate.opsForSet().intersect("hobby1", "hobby2").forEach(System.out::println);

        //5. sunion 获取多个集合的并集
        redisTemplate.opsForSet().union("hobby1", "hobby2").forEach(System.out::println);

        //6. sdiff 获取多个集合的差集
        redisTemplate.opsForSet().difference("hobby1", "hobby2").forEach(System.out::println);

        System.out.println("-------------------------------");
        //7. sinterstore 交集存储
        redisTemplate.opsForSet().intersectAndStore("hobby1", "hobby2", "hobby3");
        redisTemplate.opsForSet().members("hobby3").forEach(System.out::println);
        System.out.println("-------------------------------");
        //8. sunionstore 并集存储
        redisTemplate.opsForSet().unionAndStore("hobby1", "hobby2", "hobby4");
        redisTemplate.opsForSet().members("hobby4").forEach(System.out::println);
        System.out.println("-------------------------------");
        //9. sdiffstore 差集存储 参数一 减 参数二
        redisTemplate.opsForSet().differenceAndStore("hobby2", "hobby1", "hobby5");
        redisTemplate.opsForSet().members("hobby5").forEach(System.out::println);
    }

    //hash类型 特点：无序、不可重复 适合存储对象
    @Test
    public void testHash() {
        //1. hset 存
        redisTemplate.opsForHash().put("user", "name", "winnie pooh");

        //2. hmset 存多个
        redisTemplate.opsForHash().putAll("info", Map.of("name", "winnie pooh", "age", 18));

        //3. hget 取
        redisTemplate.opsForHash().get("user", "name");

        //4. hmget 取多个
        redisTemplate.opsForHash().multiGet("info", Arrays.asList("name", "age")).forEach(System.out::println);

        //5. hkeys 取所有的key
        redisTemplate.opsForHash().keys("info").forEach(System.out::println);

        //6. hvals 取所有的value
        redisTemplate.opsForHash().values("info").forEach(System.out::println);

        //7. hgetall 取所有的key-value
        redisTemplate.opsForHash().entries("info").forEach((k, v) -> System.out.println(k + " : " + v));


    }

    //ZSet类型 特点：有序、不可重复 适合排行榜
    @Test
    public void testZSet() {
        //1. zadd 添加元素 有序的 不能重复 有分数
        redisTemplate.opsForZSet().add("rank", "winnie pooh", 100);
        redisTemplate.opsForZSet().add("rank", "baby pig", 90);
        redisTemplate.opsForZSet().add("rank", "tigger", 80);
        redisTemplate.opsForZSet().add("rank", "eeyore", 70);

        //2. zrevrange  -- 大 ---> 小 0 -1 代表取所有 0 1 代表取前两个
        redisTemplate.opsForZSet().reverseRange("rank", 0, -1).forEach(System.out::println);
        System.out.println("😎");

        //3. zrange  ----> 小 ---> 大 0 -1 代表取所有 0 1 代表取前两个
        redisTemplate.opsForZSet().range("rank", 0, -1).forEach(System.out::println);
        System.out.println("😎");

        //4. 按分数区间获取  小 ---> 大 0 -1 代表取所有 0 1 代表取前两个
        redisTemplate.opsForZSet().rangeByScore("rank", 80, 90).forEach(System.out::println);
        System.out.println("😎");
        //5. 按分数区间获取  大 ---> 小 0 -1 代表取所有 0 1 代表取前两个
        redisTemplate.opsForZSet().reverseRangeByScore("rank", 80, 90).forEach(System.out::println);
        System.out.println("😎");
    }

    //通用操作
    @Test
    public void testCommon() {
        //1. 获取所有的key
        redisTemplate.keys("*").forEach(System.out::println);

        //2. 删除key
        redisTemplate.delete(redisTemplate.keys("*"));

    }

    //存对象
    @Test
    public void testObject() {
        String s = new String();
        s = "winnie pooh";
        redisTemplate.opsForValue().set("name", s);
    }

    @Test
    public void testObject2() {
        redisTemplate.opsForSet().add("张三", "李四", "王五", "赵六", "张三", "翠花");
        redisTemplate.opsForSet().add("李四", "王五", "麻子", "二狗", "翠花");

        // 张三和自己是好友？are you kidding me?
        System.out.println("计算张三的好友有几人 " + (redisTemplate.opsForSet().size("张三") - 1));

        System.out.println("计算张三和李四有哪些共同好友");
        redisTemplate.opsForSet().intersect("张三", "李四").forEach(System.out::println);

        // service层应该排除张三李四本人
        System.out.println("查询哪些人是张三的好友却不是李四的好友");
        redisTemplate.opsForSet().difference("张三", "李四").forEach(System.out::println);

        System.out.println("查询张三和李四的好友总共有哪些人");
        redisTemplate.opsForSet().union("张三", "李四").forEach(System.out::println);

        System.out.println("判断李四是否是张三的好友");
        System.out.println(redisTemplate.opsForSet().isMember("张三", "李四"));

        System.out.println("判断张三是否是李四的好友");
        System.out.println(redisTemplate.opsForSet().isMember("李四", "张三"));

        System.out.println("将李四从张三的好友列表中移除");
        redisTemplate.opsForSet().remove("张三", "李四");
    }

    @Test
    public void testObject3() {

        System.out.println("计算张三的好友有几人");
        //  scard 张三
        System.out.println("计算张三和李四有哪些共同好友");
        // sinter 张三 李四
        System.out.println("查询哪些人是张三的好友却不是李四的好友");
        // sdiff 张三 李四
        System.out.println("查询张三和李四的好友总共有哪些人");
        // sunion 张三 李四
        System.out.println("判断李四是否是张三的好友");
        // sismember 张三 李四
        System.out.println("判断张三是否是李四的好友");
        // sismember 李四 张三
        System.out.println("将李四从张三的好友列表中移除");
        // srem 张三 李四
    }

    @Test
    public void testObject4() {

        redisTemplate.delete(redisTemplate.keys("*"));
        ZSetOperations<Object, Object> ops = redisTemplate.opsForZSet();

        Set<ZSetOperations.TypedTuple<Object>> studentsWithScores = new HashSet<>();

        studentsWithScores.add(new DefaultTypedTuple<>("Jack", 85.0));
        studentsWithScores.add(new DefaultTypedTuple<>("Lucy", 89.0));
        studentsWithScores.add(new DefaultTypedTuple<>("Rose", 82.0));
        studentsWithScores.add(new DefaultTypedTuple<>("Tom", 95.0));
        studentsWithScores.add(new DefaultTypedTuple<>("Jerry", 78.0));
        studentsWithScores.add(new DefaultTypedTuple<>("Amy", 92.0));
        studentsWithScores.add(new DefaultTypedTuple<>("Miles", 76.0));
        studentsWithScores.add(new DefaultTypedTuple<>("Dawn", 87.0));
        studentsWithScores.add(new DefaultTypedTuple<>("Lee", 55.0));

        ops.add("students", studentsWithScores);

        System.out.println("删除Tom同学");
        ops.remove("students", "Tom");

        System.out.println("获取Amy同学的分数");
        System.out.println(ops.score("students", "Amy"));

        // +1
        System.out.println("获取Rose同学的排名");
        System.out.println(ops.reverseRank("students", "Rose"));

        System.out.println("查询80分以下有几个学生");
        System.out.println(ops.count("students", 0, 80));

        System.out.println("查出成绩前3名的同学");
        ops.reverseRange("students", 0, 2).forEach(System.out::println);

        System.out.println("查出成绩80分以下的所有同学");
        ops.rangeByScore("students", 0, 80).forEach(System.out::println);

    }


    @Test
    public void testObject5() {

        System.out.println("删除Tom同学");
        //   zrem students Tom

        System.out.println("获取Amy同学的分数");
        //   zscore students Amy

        System.out.println("获取Rose同学的排名");
        //   zrevrank students Rose

        System.out.println("查询80分以下有几个学生");
        //   zcount students 0 80

        System.out.println("查出成绩前3名的同学");
        //   zrevrange students 0 2

        System.out.println("查出成绩80分以下的所有同学");
        //   zrangebyscore students 0 80

    }


```

