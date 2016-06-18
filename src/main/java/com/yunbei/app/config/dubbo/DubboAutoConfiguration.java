package com.yunbei.app.config.dubbo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.rpc.Exporter;

/**
 * @author: mlc
 * @dat: 2016年6月11日
 * @Description: TODO
 */

@Configuration
@ConditionalOnClass(Exporter.class)
public class DubboAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	//
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

	@Bean
	@ConfigurationProperties(prefix = "spring.dubbo.monitor")
	public MonitorConfig requestMonitorConfig() {
		return new MonitorConfig();
	}

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
