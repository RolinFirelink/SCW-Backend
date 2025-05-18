package com.rolin.orangesmart.cache.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rolin.orangesmart.cache.ICacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Optional;

/**
 * RedisTemplate的配置类
 */
@Component
public class RedisConfig {

    @Value("${redis.key.prefix:rolin}")
    protected String keyPrefix; // 默认生成的key前缀：rolin_

    public String getUkPrfex(String key) {
        key = Optional.ofNullable(key).orElse("");
        return key.startsWith(keyPrefix + ICacheService.KEY_SEPARATOR) ? key : (keyPrefix + ICacheService.KEY_SEPARATOR + key);
    }

    @Bean
//    @ConditionalOnProperty(name = "redis.model", havingValue = "jedis", matchIfMissing = false)
    public RedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties) {
        //连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        jedisPoolConfig.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());
        jedisPoolConfig.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
        jedisPoolConfig.setTestOnBorrow(true);

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder = JedisClientConfiguration.builder();
        jedisClientConfigurationBuilder.readTimeout(redisProperties.getTimeout());
        jedisClientConfigurationBuilder.connectTimeout(redisProperties.getConnectTimeout());
        JedisClientConfiguration configuration = jedisClientConfigurationBuilder.usePooling().poolConfig(jedisPoolConfig).build();

        JedisConnectionFactory connectionFactory = null;
        if (redisProperties.getSentinel() != null) {
            RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration(redisProperties.getSentinel().getMaster(),
                    new HashSet<>(redisProperties.getSentinel().getNodes()));
            sentinelConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
            if(StringUtils.hasText(redisProperties.getSentinel().getPassword())) {
                sentinelConfiguration.setSentinelPassword(RedisPassword.of(redisProperties.getSentinel().getPassword()));
            }
            connectionFactory = new JedisConnectionFactory(sentinelConfiguration, configuration);
        } else if (redisProperties.getCluster() != null) {
            RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
            clusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
            clusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
            connectionFactory = new JedisConnectionFactory(clusterConfiguration, configuration);
        } else {
            RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
            standaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
            connectionFactory = new JedisConnectionFactory(standaloneConfiguration, configuration);
        }
        return connectionFactory;
    }

//    @Bean
//    @ConditionalOnProperty(name = "redis.model", havingValue = "lettuce", matchIfMissing = true)
//    public RedisConnectionFactory lettuceConnectionFactory(RedisProperties redisProperties) {
//        //连接池配置
//        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
//        genericObjectPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
//        genericObjectPoolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
//        genericObjectPoolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
//        genericObjectPoolConfig.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
//
//        //redis客户端配置
//        LettucePoolingClientConfigurationBuilder lettucePoolingClientConfigurationBuilder = LettucePoolingClientConfiguration.builder();
//        lettucePoolingClientConfigurationBuilder.commandTimeout(redisProperties.getTimeout());
//        lettucePoolingClientConfigurationBuilder.shutdownTimeout(redisProperties.getLettuce().getShutdownTimeout());
//        LettucePoolingClientConfiguration lettuceClientConfiguration = lettucePoolingClientConfigurationBuilder.poolConfig(genericObjectPoolConfig).build();
//
//        LettuceConnectionFactory lettuceConnectionFactory = null;
//        if (redisProperties.getSentinel() != null) {
//            RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration(redisProperties.getSentinel().getMaster(),
//                    new HashSet<>(redisProperties.getSentinel().getNodes()));
//            sentinelConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
//            if(StringUtils.hasText(redisProperties.getSentinel().getPassword())) {
//                sentinelConfiguration.setSentinelPassword(RedisPassword.of(redisProperties.getSentinel().getPassword()));
//            }
//            lettuceConnectionFactory = new LettuceConnectionFactory(sentinelConfiguration, lettuceClientConfiguration);
//        } else if (redisProperties.getCluster() != null) {
//            RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
//            clusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
//            clusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
//            lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfiguration, lettuceClientConfiguration);
//        } else {
//            RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
//            standaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
//            lettuceConnectionFactory = new LettuceConnectionFactory(standaloneConfiguration, lettuceClientConfiguration);
//        }
//        return lettuceConnectionFactory;
//    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory jedisConnectionFactory) {
        // 设置序列化
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// 忽略json转bean时，json中多余的属性
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(objectMapper, Object.class);
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}