package com.en_workshop.webcrawlerakka;

/**
 * @author Radu Ciumag
 */
public class WebCrawlerConstants {

    /* Actors names / paths */
    public static final String SYSTEM_NAME = "webCrawlerSystem";
    public static final String MASTER_ACTOR_NAME = "masterActor";

    public static final String DOMAIN_MASTER_ACTOR_NAME = "domainMasterActor";
    public static final String PROCESSING_MASTER_ACTOR_NAME = "processingMasterActor";
    public static final String PERSISTENCE_MASTER_ACTOR_NAME = "persistenceMasterActor";

    public static final String DOMAIN_ACTOR_PART_NAME = "domainActor_";
    public static final String DOWNLOAD_URL_ACTOR_PART_NAME = "downloadUrlActor_";

    /* HTTP related constants */
    public static final String HTTP_CUSTOM_HEADER_RESPONSE_CODE = "CRAWL-ResponseCode";

    public static final String HTTP_RESPONSE_CODE_NONE = "0";
    public static final String HTTP_RESPONSE_CODE_OK = "200";

    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    /* Other */
    public static final long DOMAINS_REFRESH_PERIOD = 5 * 60 * 1000; // 5 minutes
    public static final long DOMAIN_DEFAULT_COOLDOWN = 60 * 1000; // 60 seconds
    public static final String[] ACCEPTED_MIME_TYPES = new String[]{"text/html"}; // http://en.wikipedia.org/wiki/Internet_media_type
}
