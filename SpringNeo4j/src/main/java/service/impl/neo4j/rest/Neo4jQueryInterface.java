package service.impl.neo4j.rest;

public interface Neo4jQueryInterface {

	String REL_PART_OF = "PART_OF";
	String REL_LINKS_TO = "LINKS_TO";

	String LINK_NAME = "lname";
	String LINK_URL = "lurl";
	String COOL_DOWN_PERIOD = "coolDownPeriod";

	String DOMAIN_URL = "durl";
	String DOMAIN_NAME = "dname";

	String PARAM_SKIP = "skip";
	String PARAM_LIMIT = "limit";

	String LINK_STATUS = "status";
	String LINK_LAST_UPDATE = "lastUpdate";
	String LINK_ERROR_COUNT = "errorCount";

	String CREATE_DOMAIN = "CREATE (n:Domain { " + DOMAIN_NAME + " : {"
			+ DOMAIN_NAME + "}, " + DOMAIN_URL + ":{" + DOMAIN_URL + "}, "
			+ COOL_DOWN_PERIOD + ":{" + COOL_DOWN_PERIOD + "} })";

	String GET_DOMAINS = "match (n:Domain) return n." + DOMAIN_NAME + ", n."
			+ DOMAIN_URL + ", n." + COOL_DOWN_PERIOD + " skip {" + PARAM_SKIP
			+ "} limit {" + PARAM_LIMIT + "}";

	String REMOVE_DOMAIN = "MATCH (n:Domain{" + DOMAIN_URL + ":{" + DOMAIN_URL
			+ "}}) OPTIONAL MATCH (n)<-[r1:" + REL_PART_OF + "]-(t)-[r2:"
			+ REL_LINKS_TO + "]-() DELETE r1,r2,n,t";

	String ADD_DOMAIN_LINK = "MATCH (d:Domain { " + DOMAIN_URL + ": {"
			+ DOMAIN_URL + "} }) CREATE UNIQUE (d)<-[:" + REL_PART_OF
			+ "]-(l:Link { " + LINK_NAME + ":{" + LINK_NAME + "}, " + LINK_URL
			+ ":{" + LINK_URL + "}, " + LINK_STATUS + ":{" + LINK_STATUS
			+ "}, " + LINK_LAST_UPDATE + ":{" + LINK_LAST_UPDATE + "}, "
			+ LINK_ERROR_COUNT + ":{" + LINK_ERROR_COUNT + "} })";
}
