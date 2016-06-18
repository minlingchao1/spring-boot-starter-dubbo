项目使用gradle构建,配置文件使用properties文件配置
# 配置文件
相对应的config类可以查看dubbo源码
- 自动扫描包(AnnotationBean)
- 应用信息(ApplicationConfig)
- 注册中心(RegistryConfig)
- 协议配置(ProtocolConfig)
- 监控(MonitorConfig)
- api provider(ProviderConfig)

Config实现类

```
@Configuration
@ConditionalOnClass(Exporter.class)
public class DubboAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * 包的扫描
	 * 
	 * @param packages
	 * @return
	 */
	@Bean
	public AnnotationBean annotationBean(@Value("${spring.dubbo.basePackages}") String packages) {

		AnnotationBean annotationBean = new AnnotationBean();
		annotationBean.setPackage(packages);
		annotationBean.setApplicationContext(applicationContext);
		return annotationBean;
	}

	/**
	 * 应用信息
	 * 
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.dubbo.application")
	public ApplicationConfig requestApplicationConfig() {
		return new ApplicationConfig();
	}

	/**
	 * 注册中心
	 * 
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.dubbo.registry")
	public RegistryConfig requestRegistryConfig() {
		return new RegistryConfig();
	}

	/**
	 * 协议配置
	 * 
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.dubbo.protocol")
	public ProtocolConfig requestProtocolConfig() {
		return new ProtocolConfig();
	}

	/**
	 * 监控
	 * 
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.dubbo.monitor")
	public MonitorConfig requestMonitorConfig() {
		return new MonitorConfig();
	}

	/**
	 * api提供者
	 * 
	 * @param applicationConfig
	 * @param registryConfig
	 * @param protocolConfig
	 * @param monitorConfig
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.dubbo.provider")
	public ProviderConfig providerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig,
			ProtocolConfig protocolConfig, MonitorConfig monitorConfig) {
		ProviderConfig providerConfig = new ProviderConfig();
		providerConfig.setApplication(applicationConfig);
		providerConfig.setRegistry(registryConfig);
		providerConfig.setProtocol(protocolConfig);
		providerConfig.setMonitor(monitorConfig);
		return providerConfig;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}

```


provider端application.properties中添加相关的配置信息，配置实例如下：

```
#####扫描包配置##########
spring.dubbo.basePackages=com.yunbei.app.dubbo


#####应用信息##########

##项目拥有者
spring.dubbo.application.owner=yunbei
##项目名称
spring.dubbo.application.name=spring-boot-dubbo-provider

#####协议配置##########
##协议名称
spring.dubbo.protocol.name=dubbo
##开放端口
spring.dubbo.protocol.port=20880
##日志是否开启
spring.dubbo.protocol.accessLog=true

######注册中心配置#######

##zookeeper地址
spring.dubbo.registry.address=zookeeper://127.0.0.1:2181

##是否注册
spring.dubbo.registry.register=true
##是否开启订阅
spring.dubbo.registry.subscribe=true

##监控配置
spring.dubbo.monitor.protocol=registry

#########提供者信息########

##超时时间配置
spring.dubbo.provider.timeout=3000
##重试次数
spring.dubbo.provider.retries=1

##是否延迟注册
spring.dubbo.provider.delay=-1
```

consumer端配置与provider相似，只是不需要配置provider的信息
