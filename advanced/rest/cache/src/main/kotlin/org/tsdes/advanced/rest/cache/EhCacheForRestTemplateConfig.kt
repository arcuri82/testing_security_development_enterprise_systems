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
 * Here, we want to create a bean for RestTemplate which is configured
 * to have a EhCache when dealing with cacheable HTTP responses.
 * There are quite a few low level details to handle to configure the
 * low level HTTP libraries used by Spring.
 *
 * Note: it is important to have ehcache.xml in the resources, otherwise
 * Spring will not configure it.
 *
 * Note: EhCache can also be used as a general cache, eg to cache the
 * results of JPA queries (ie accesses to databases).
 *
 * Created by arcuri82 on 30-Aug-18.
 */
@Configuration
@EnableCaching // needed for auto-configure the cache beans
class EhCacheForRestTemplateConfig {

    /*
        This is the actual bean that we want, and that we will @Autowire
        in our service beans when doing HTTP calls.
     */

    @Bean
    fun restTemplate(httpClient: HttpClient): RestTemplate {
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpClient
        return RestTemplate(requestFactory)
    }

    //---------------------------------------------------------------------

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
        // this cast will fail if ehcache.xml if missing
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

}