package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import entity.DomainURL;
import entity.SimpleURL;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import repo.DomainUrlRepo;
import repo.SimpleUrlRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/spring-appContext.xml"})
//@Transactional
public class TestUrlService {

	@Autowired
	DomainUrlRepo domainRepo;
	
	@Autowired
	SimpleUrlRepo simpleUrlRepo;
	
	@Autowired
	UrlService urlService;
	
	@Before
	public void setUp() {
        urlService.removeAllDomains();
	}

    @After
    public void tearDown() {
        urlService.removeAllDomains();
    }

	@Test
	public void testDomainSave(){
		DomainURL domain = urlService.addDomainUrl("Domain_1", "www.domain_1.com", 1000);
		
		Assert.assertNotNull(domain);
		Assert.assertEquals(1, domainRepo.count());

	}
	
	@Test
	public void testSaveSimpleUrl(){
		DomainURL domain = urlService.addDomainUrl("Domain_1", "www.domain_1.com", 1000);
		urlService.addSimpleUrl("page1", "www.domain_1.com/page_1", "NEW", domain.getAddress(), null);
		urlService.addSimpleUrl("page2", "www.domain_1.com/page_2", "NEW", domain.getAddress(), null);
		urlService.addSimpleUrl("page3", "www.domain_1.com/page_3", "NEW", domain.getAddress(), null);

		Assert.assertEquals(3, simpleUrlRepo.count());
	}

	@Test
	public void testSaveSimpleUrlWithoutDomain(){
        String address = "www.domain_1.com";
		urlService.addSimpleUrl("page1", "www.domain_1.com/page_1", "NEW", address, null);
		urlService.addSimpleUrl("page2", "www.domain_1.com/page_2", "NEW", address, null);
		urlService.addSimpleUrl("page3", "www.domain_1.com/page_3", "NEW", address, null);

		Assert.assertEquals(3, simpleUrlRepo.count());
		Assert.assertEquals(1, domainRepo.count());

        Collection<SimpleURL> urls = urlService.findURLs(address, "NEW", 0, 100);
        Assert.assertEquals(3, urls.size());
	}

	@Test
	public void testRemoveSimpleUrl(){
		DomainURL domain = urlService.addDomainUrl("Domain_1", "www.domain_1.com", 1000);
		SimpleURL simpleURL = urlService.addSimpleUrl("page1", "www.domain_1.com/page_1", "NEW", domain.getAddress(), null);

		Assert.assertNotNull(simpleURL);
		Assert.assertEquals(1, simpleUrlRepo.count());

        urlService.removeSimpleUrl(simpleURL.getUrl());
		Assert.assertEquals(0, simpleUrlRepo.count());
	}

	@Test
	public void testRemoveDomain(){
		DomainURL domain = urlService.addDomainUrl("Domain_1", "www.domain_1.com", 1000);
		urlService.addSimpleUrl("page1", "www.domain_1.com/page_1", "NEW", domain.getAddress(), null);

		urlService.removeDomainUrl(domain.getAddress());
		Assert.assertEquals(0, domainRepo.count());
	}

	@Test
	public void testMultipleDomains(){
		DomainURL domain_1 = urlService.addDomainUrl("Domain_1", "www.domain_1.com", 1000);
		SimpleURL simpleURL_1 = urlService.addSimpleUrl("page1", "www.domain_1.com/page_1", "NEW", domain_1.getAddress(), null);

		DomainURL domain_2 = urlService.addDomainUrl("Domain_2", "www.domain_2.com", 1000);
		urlService.addSimpleUrl("page2", "www.domain_2.com/page_2", "NEW", domain_2.getAddress(), null);

		Assert.assertEquals(2, simpleUrlRepo.count());
		
		urlService.removeSimpleUrl(simpleURL_1.getUrl());
		
		urlService.removeDomainUrl(domain_1.getAddress());

        Assert.assertEquals(1, simpleUrlRepo.count());
		Assert.assertEquals(1, domainRepo.count());
	}

    @Test
    @Ignore
    public void testDomainFind_STRESS() {
        urlService.addDomainUrl("Domain_23", "www.domain_23.com", 1000);
        for (int i = 0; i < 1000; i++) {
            long t0 = System.currentTimeMillis();
            SimpleURL url = urlService.addSimpleUrl("link_" + i, "/into23.txt" + i, "NEW", "www.domain_23.com", null);
            long t1 = System.currentTimeMillis();
            System.out.println(url.getUrl() + " in " + (t1-t0));
        }

    }

    @Test
	public void testDomainFind(){
		urlService.addDomainUrl("Domain_23", "www.domain_23.com", 1000);
        urlService.addSimpleUrl("link_23", "/into23.txt", "NEW", "www.domain_23.com", null);
		urlService.addDomainUrl("Domain_8", "www.domain_8.com", 1000);
        urlService.addSimpleUrl("link_1", "/into1.txt", "NOT_VISITED", "www.domain_8.com", null);
        urlService.addSimpleUrl("link_2", "/into2.txt", "NOT_VISITED", "www.domain_8.com", null);
        urlService.addSimpleUrl("link_3", "/into3.txt", "VISITED", "www.domain_8.com", null);

        Collection<SimpleURL> urls = urlService.findURLs("www.domain_8.com", "NOT_VISITED", 0, 1); // TODO does neo4j guarantee the order?
        Assert.assertTrue(urls.iterator().hasNext());
		Assert.assertEquals("/into1.txt", urls.iterator().next().getUrl());

        urls = urlService.findURLs("www.domain_8.com", "NOT_VISITED", 1, 1);
        Assert.assertTrue(urls.iterator().hasNext());
        Assert.assertEquals("/into2.txt", urls.iterator().next().getUrl()); // TODO does neo4j guarantee the order?

        urls = urlService.findURLs("www.domain_8.com", "VISITED", 1, 1);
        Assert.assertFalse(urls.iterator().hasNext());
	}

    @Test
    public void testAddExternalLink(){
        urlService.addDomainUrl("Domain_From", "www.domain_From.com", 1000);
        urlService.addDomainUrl("Domain_To", "www.domain_To.com", 1000);
        urlService.addSimpleUrl("link_23", "/into23.txt", "NEW", "www.domain_To.com", "www.domain_From.com");
        urlService.addSimpleUrl("link_3", "/into3.txt", "NEW", "www.domain_To.com", null);

        Collection<SimpleURL> urls = urlService.findURLs("www.domain_To.com", "NEW", 0, 10);
        Assert.assertEquals(2, urls.size());
        urls = urlService.findExternalURLs("www.domain_From.com", "NEW", 0, 10);
        Assert.assertEquals(1, urls.size());
        Assert.assertEquals("/into23.txt", urls.iterator().next().getUrl());
    }

    @Test
    public void testAddExternalLinks(){
        urlService.addDomainUrl("Domain_From", "www.domain_From.com", 1000);
        urlService.addDomainUrl("Domain_To", "www.domain_To.com", 1000);
        urlService.addSimpleUrls(Arrays.asList("/into23.txt", "/into3.txt"), "NEW", "www.domain_To.com", "www.domain_From.com");

        Collection<SimpleURL> urls = urlService.findURLs("www.domain_To.com", "NEW", 0, 10);
        Assert.assertEquals(2, urls.size());
        urls = urlService.findExternalURLs("www.domain_From.com", "NEW", 0, 10);
        Assert.assertEquals(2, urls.size());
    }

    @Test
    public void testAddExternalLinksSTRESS(){
        urlService.addDomainUrl("Domain_From", "www.domain_From.com", 1000);
        urlService.addDomainUrl("Domain_To", "www.domain_To.com", 1000);
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
//            urls.add("http://www.archeus.ro/lingvistica/CautareDex?query=bun&lang=ro&seed="+ i);
            urls.add("http://www.archeus.ro/lingvistica/CautareDex?query=bun&lang=ro&seed="+ i);

        }
        urlService.addSimpleUrls(urls, "NOT_VISITED", "www.domain_To.com", "www.domain_From.com");

        Collection<SimpleURL> actual = urlService.findURLs("www.domain_To.com", "NOT_VISITED", 0, 1000);
        Assert.assertEquals(500, actual.size());
        actual = urlService.findExternalURLs("www.domain_From.com", "NOT_VISITED", 0, 1000);
        Assert.assertEquals(500, actual.size());
    }

    @Test
    public void testAddLinksSTRESS(){
        urlService.addDomainUrl("Domain_To", "www.domain_To.com", 1000);
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
//            urls.add("http://www.archeus.ro/lingvistica/CautareDex?query=bun&lang=ro&seed="+ i);
            urls.add("http://www.archeus.ro/lingvistica/CautareDex?query=bun&lang=ro&seed="+ i);

        }
        urlService.addSimpleUrls(urls, "NOT_VISITED", "www.domain_To.com", null);

        Collection<SimpleURL> actual = urlService.findURLs("www.domain_To.com", "NOT_VISITED", 0, 1000);
        Assert.assertEquals(1000, actual.size());
        actual = urlService.findExternalURLs("www.domain_From.com", "NOT_VISITED", 0, 1000);
        Assert.assertEquals(0, actual.size());
    }

    @Test
    public void testAddlLinks(){
        urlService.addDomainUrl("Domain_From", "www.domain_From.com", 1000);
        urlService.addDomainUrl("Domain_To", "www.domain_To.com", 1000);
        urlService.addSimpleUrls(Arrays.asList("/into23.txt", "/into3.txt"), "NEW", "www.domain_To.com", null);

        Collection<SimpleURL> urls = urlService.findURLs("www.domain_To.com", "NEW", 0, 10);
        Assert.assertEquals(2, urls.size());
        urls = urlService.findExternalURLs("www.domain_From.com", "NEW", 0, 10);
        Assert.assertEquals(0, urls.size());
    }

    @Test
    public void testUpdateStatus(){
        urlService.addDomainUrl("Domain_8", "www.domain_8.com", 1000);
        urlService.addSimpleUrl("link_1", "/into1.txt", "NOT_VISITED", "www.domain_8.com", null);

        Collection<SimpleURL> urls = urlService.findURLs("www.domain_8.com", "NOT_VISITED", 0, 10);
        Assert.assertTrue(urls.iterator().hasNext());
        Assert.assertEquals("/into1.txt", urls.iterator().next().getUrl());
        urls = urlService.findURLs("www.domain_8.com", "VISITED", 0, 10);
        Assert.assertFalse(urls.iterator().hasNext());

        urlService.updateSimpleUrlStatus("/into1.txt", "VISITED");


        urls = urlService.findURLs("www.domain_8.com", "VISITED", 0, 10);
        Assert.assertTrue(urls.iterator().hasNext());
        Assert.assertEquals("/into1.txt", urls.iterator().next().getUrl());
        urls = urlService.findURLs("www.domain_8.com", "NOT_VISITED", 0, 10);
        Assert.assertFalse(urls.iterator().hasNext());
    }

    @Test
    public void testUpdateErrorCount(){
        urlService.addDomainUrl("Domain_8", "www.domain_8.com", 1000);
        SimpleURL url = urlService.addSimpleUrl("link_1", "/into1.txt", "NOT_VISITED", "www.domain_8.com", null);

        urlService.updateSimpleUrlErrorStatus("/into1.txt", 1);

        List<SimpleURL> modified = simpleUrlRepo.findByUrl("/into1.txt");
        Assert.assertFalse(modified.isEmpty());
        for (SimpleURL crt : modified) {
            Assert.assertEquals(1, crt.getErrorCount());
        }
    }

    @Test
    public void testDomainCoolDownPeriod(){
        urlService.addDomainUrl("Domain_C", "www.domain_C.com", 1500);

        Page<DomainURL> domainsPage = urlService.findDomains(new PageRequest(0, 1));

        List<DomainURL> domains = domainsPage.getContent();
        Assert.assertNotNull(domains);
        Assert.assertEquals(1500, domains.get(0).getCoolDownPeriod());
    }

	private void populateDatabase(){
		DomainURL wikiDomain = new DomainURL();
        wikiDomain.setAddress("www.wikipedia.org");
        wikiDomain.setName("wikipedia");

		SimpleURL wikiSimpleURL = new SimpleURL("http://en.wikipedia.org/wiki/Main_Page", "Wiki English", "NEW", 0, System.currentTimeMillis());
		wikiDomain.addInternalUrl(wikiSimpleURL);
		simpleUrlRepo.save(wikiSimpleURL);
		
		wikiSimpleURL = new SimpleURL("http://it.wikipedia.org/wiki/Pagina_principale", "Wiki Italiano", "NEW", 0, System.currentTimeMillis());
		wikiDomain.addInternalUrl(wikiSimpleURL);
		simpleUrlRepo.save(wikiSimpleURL);
		
		wikiSimpleURL = new SimpleURL("http://de.wikipedia.org/wiki/Wikipedia:Hauptseite", "Wiki Deutsch", "NEW", 0, System.currentTimeMillis());
		wikiDomain.addInternalUrl(wikiSimpleURL);
		simpleUrlRepo.save(wikiSimpleURL);
		
		//save
		domainRepo.save(wikiDomain);
	}
}
