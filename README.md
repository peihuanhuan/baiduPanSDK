## 百度云盘 Java SDK
已实现功能：
- [x] 授权，获取 accessToken、refreshToken
- [x] 上传文件
- [x] 分享文件
- [x] 查询文件列表
- [x] 查询文件详情

### 使用
1. Maven 引用，最新版本为 ![https://mvnrepository.com/artifact/net.peihuan/baidu-pan-starter](https://img.shields.io/maven-central/v/net.peihuan/baidu-pan-starter.svg)
```xml
<dependency>
    <groupId>net.peihuan</groupId>
    <artifactId>baidu-pan-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

2. 修改`application.yaml`文件，添加配置
```yaml
baidu-pan:
  client-id: "your client id"
  device-id: 1111111
  client-secret: "your secret"
  share-third-id: 111                   # 不需要分享则无需填写
  share-secret: "secret"                # 不需要分享则无需填写
  root-dir: "/apps/your/root/path/"     # 文件上传的根目录
```

3. 注入 `BaiduService` 后配置持久化方式，即可使用:
```java
@Component
public class BaiduTest {
    @Autowired
    private BaiduService baiduService;

    @PostConstruct
    public void init() {
        // 必须配置持久化，不配置默认为内存实现
        // TODO BaiduOps 需要用户基于 mysql or redis 来实现
        BaiduOps ops = new RedisOpsImpl();
        BaiduOAuthConfigStorage baiduOAuthConfigStorage = new BaiduOAuthConfigServiceImpl(ops);
        baiduService.setConfigStorage(baiduOAuthConfigStorage);
    }

    public void getAuthorizeUrl() {
        // 由于可能多个用户授权使用，state 可用于传递 userId
        System.out.println(baiduService.getAuthorizeUrl("http://your.redirct.com", "your state"));
    }

    public void getAuthorizeUrl(String code) {
        // 任何方法都需要传递 userId 来标记这个 code 属于哪个用户。
        // 如果该系统只有开发者一个人需要授权百度云盘，userId 填写任意一个固定值即可
        // 调用此方法后，内部会持久化 accessToken 和 refreshToken
        System.out.println(baiduService.getAccessTokenByCode("1", code, "http://your.redirct.com"));
    }

    public void upload(File file) {
        // 任何方法都需要传递 userId
        // userId 为 1 的用户上传一个文件至 /apps/your/root/path/object/path
        baiduService.getPanService().uploadFile("1", "object/path", file, RtypeEnum.OVERRIDE);
    }
}

```


其他：
如果你不用 Spring 框架，也可以自己 new 一个 baiduService 来使用：
```java
BaiduService baiduService = new BaiduServiceImpl(properties, okHttpClient);
// config your
baiduService.setConfigStorage(baiduOAuthConfigStorage);
```