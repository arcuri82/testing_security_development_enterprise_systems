package org.tsdes.advanced.rest.cache

import net.sf.ehcache.Ehcache
import org.apache.http.client.HttpClient
import org.apache.http.client.cache.HttpCacheStorage
import org.apache.http.impl.client.cache.CacheConfig
import org.apache.http.impl.client.cache.CachingHttpClientBuilder
import org.apache.http.impl.client.cache.ehcache.EhcacheHttpCacheStorage
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

/**
 * Created by arcuri82 on 30-Aug-18.
 */
@Configuration
@EnableCaching // needed for auto-configure the cache beans
class EhCacheForRestTemplateConfig {

    @Bean
    fun cacheConfig(): CacheConfig {
        //HTTP cache
        return CacheConfig.custom()
                .setMaxCacheEntries(2_000)
                .setMaxObjectSize(500_000)
                .build()
    }

    @Bean
    fun poolingHttpClientConnectionManager(): PoolingHttpClientConnectionManager {
        return PoolingHttpClientConnectionManager().apply { maxTotal = 20 }
    }

    @Value("#{cacheManager.getCache('httpClient')}")
    private lateinit var httpClientCache: Cache

    @Bean
    fun httpCacheStorage(): HttpCacheStorage {
        val ehcache = this.httpClientCache.nativeCache as Ehcache
        return EhcacheHttpCacheStorage(ehcache)
    }

    @Bean
    fun httpClient(
            poolingHttpClientConnectionManager: PoolingHttpClientConnectionManager,
            cacheConfig: CacheConfig,
            httpCacheStorage: HttpCacheStorage
    ): HttpClient {

        return CachingHttpClientBuilder
                .create()
                .setCacheConfig(cacheConfig)
                .setHttpCacheStorage(httpCacheStorage)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build()
    }

    @Bean
    fun restTemplate(httpClient: HttpClient): RestTemplate {
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpClient
        return RestTemplate(requestFactory)
    }
}