package com.ubercool.cache.configuration;

import static com.hazelcast.config.InMemoryFormat.OBJECT;
import static com.hazelcast.config.MaxSizeConfig.MaxSizePolicy.PER_NODE;
import static com.hazelcast.config.MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE;

import com.google.common.collect.ImmutableMap;
import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryConfig;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.MemberAttributeConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import com.hazelcast.spring.context.SpringManagedContext;
import java.util.Collections;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.provider.hazelcast.HazelcastLockProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class CacheConfiguration {

  private static Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);
  private static final String DISCOVERY_STRATEGY_CLASS_NAME =
      "com.hazelcast.kubernetes.HazelcastKubernetesDiscoveryStrategy";

  public static final String GIT_COMMIT_ID_PROPERTY_NAME = "git.commit.id.abbrev";
  public static final String HZ_SCHEDULED_EXECUTOR_NAME = "hzScheduledExecutorService";


  @Value("${" + GIT_COMMIT_ID_PROPERTY_NAME + ":unknown}")
  private String currentNodeVersion;

  @Value("${cache.hazelcast.cloud.enabled}")
  private boolean isHazelcastCloudEnabled;

  @Value("${cache.hazelcast.local.network.enabled}")
  private boolean isHazelcastLocalNetworkEnabled;

  @Value("${cache.hazelcast.local.ip}")
  private String hazelcastLocalIp;

  @Value("${cache.hazelcast.cloud.service-name}")
  private String hazelcastCloudServiceName;

  @Value("${cache.hazelcast.cloud.namespace}")
  private String hazelcastCloudNamespace;

  @Bean
  @Autowired
  public HazelcastInstance hazelcastInstance(Config hazelcastConfig) {
    return Hazelcast.newHazelcastInstance(hazelcastConfig);
  }

  @Bean
  @Primary
  @Autowired
  public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
    return new HazelcastCacheManager(hazelcastInstance);
  }

  @Bean
  public HazelcastLockProvider lockProvider(HazelcastInstance hazelcastInstance) {
    return new HazelcastLockProvider(hazelcastInstance);
  }

  @Bean
  public SpringManagedContext managedContext() {
    return new SpringManagedContext();
  }

  @Bean
  @Autowired
  public Config hazelcastConfig(SpringManagedContext managedContext) {
    Config config = new Config()
        .setProperties(createHazelcastProperties())
        .setGroupConfig(createGroupConfig())
        .setNetworkConfig(createNetworkConfig());

    addMapConfigs(config);

    config.setMemberAttributeConfig(memberAttributes());

    config.setManagedContext(managedContext);

    logger.debug("Using hazelcast config: {}", config);
    return config;
  }

  private void addMapConfigs(Config config) {
    config
        .addMapConfig(operationsCacheMap())
        .addMapConfig(tasksCacheMap())
        .addMapConfig(workflowCacheMap());
  }

  private MapConfig genericMapConfig(String name, int maxIdleSeconds,
      int size, MaxSizeConfig.MaxSizePolicy maxSizePolicy, EvictionPolicy evictionPolicy) {

    MaxSizeConfig maxSizeConfig = new MaxSizeConfig();
    maxSizeConfig.setSize(size);
    maxSizeConfig.setMaxSizePolicy(maxSizePolicy); // maximum number of entities in a cache: 50Mb

    MapConfig result = new MapConfig(name);
    return result.setTimeToLiveSeconds(4 * 60 * 60) // Max TTL - 4 hours
        .setMaxIdleSeconds(maxIdleSeconds) // Max idle time after the last touch - 30 minutes
        .setMaxSizeConfig(maxSizeConfig)
        .setEvictionPolicy(evictionPolicy)
        .setInMemoryFormat(OBJECT);
  }

  private MemberAttributeConfig memberAttributes() {
    MemberAttributeConfig attributes = new MemberAttributeConfig();
    attributes.setStringAttribute(GIT_COMMIT_ID_PROPERTY_NAME, currentNodeVersion);
    logger.info("Hazelcast node version set as '{}'", currentNodeVersion);
    return attributes;
  }

  private MapConfig operationsCacheMap() {
    return genericMapConfig("operations", 60 * 60, 5000, PER_NODE, EvictionPolicy.LRU);
  }

  private MapConfig tasksCacheMap() {
    return genericMapConfig("tasks", 60 * 60, 5000, PER_NODE, EvictionPolicy.LRU);
  }

  private MapConfig workflowCacheMap() {
    return genericMapConfig("workflows", 30 * 60, 25, USED_HEAP_SIZE, EvictionPolicy.LRU);
  }

  private Properties createHazelcastProperties() {
    Properties properties = new Properties();
    if (isHazelcastCloudEnabled) {
      properties.setProperty("hazelcast.discovery.enabled", "true");
    }
    return properties;
  }

  private GroupConfig createGroupConfig() {
    return new GroupConfig(
        hazelcastCloudServiceName,
        hazelcastCloudServiceName
    );
  }

  private NetworkConfig createNetworkConfig() {
    if (isHazelcastCloudEnabled) {
      logger.info("Running hazelcast in CLOUD MODE.");
      return createHazelcastCloudNetworkConfig();
    } else if (isHazelcastLocalNetworkEnabled) {
      logger.info("Running hazelcast in LOCAL (non-cloud) mode with network on");
      return createHazelcastLocalNetworkOnConfig();
    } else {
      logger.info("Running hazelcast in LOCAL (non-cloud) mode with network off");
      return createHazelcastLocalNetworkOffConfig();
    }
  }

  private NetworkConfig createHazelcastCloudNetworkConfig() {
    DiscoveryConfig discoveryConfig = new DiscoveryConfig();
    discoveryConfig.addDiscoveryStrategyConfig(new DiscoveryStrategyConfig(
        DISCOVERY_STRATEGY_CLASS_NAME,
        ImmutableMap.of(
            "service-name", hazelcastCloudServiceName,
            "namespace", hazelcastCloudNamespace,
            "resolve-not-ready-addresses", true
        ))
    );
    return new NetworkConfig()
        .setJoin(new JoinConfig()
            .setMulticastConfig(new MulticastConfig()
                .setEnabled(false))
            .setTcpIpConfig(new TcpIpConfig()
                .setEnabled(false))
            .setDiscoveryConfig(discoveryConfig));
  }

  private NetworkConfig createHazelcastLocalNetworkOnConfig() {
    return new NetworkConfig()
        .setJoin(new JoinConfig()
            .setMulticastConfig(new MulticastConfig()
                .setEnabled(false))
            .setTcpIpConfig(new TcpIpConfig()
                .setEnabled(true)
                .setMembers(Collections.singletonList(hazelcastLocalIp))
            ))
        .setInterfaces(new InterfacesConfig()
            .setEnabled(true)
            .addInterface(hazelcastLocalIp));
  }

  private NetworkConfig createHazelcastLocalNetworkOffConfig() {
    return new NetworkConfig()
        .setJoin(new JoinConfig()
            .setMulticastConfig(new MulticastConfig()
                .setEnabled(false))
            .setTcpIpConfig(new TcpIpConfig()
                .setEnabled(false)
            ));
  }

}
