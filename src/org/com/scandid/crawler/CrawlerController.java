package org.com.scandid.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 *  
 * @author ajish
 * A Web Crawler using crawler4j
 */

public class CrawlerController {
	public static void startCrawler() {
		CrawlConfig config = new CrawlConfig();
		
		// folder where intermediate crawl data is stored.
		config.setCrawlStorageFolder("crawlerData");
		
		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		config.setPolitenessDelay(1000);
		
		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		config.setResumableCrawling(false);
		config.setMaxDepthOfCrawling(0);
		
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		try {
			CrawlController controller = new CrawlController(config,
					pageFetcher, robotstxtServer);
			// starting page
			controller.addSeed("http://www.amazon.in/gp/product/B00OK305S0/ref=amb_link_184809367_3?pf_rd_m=A1VBAL9TL5WCBF&pf_rd_s=merchandised-search-top-1&pf_rd_r=0PZSY6ZG43VY5ZK4NGS0&pf_rd_t=101&pf_rd_p=586504867&pf_rd_i=5520975031");
			//controller.addSeed("http://www.amazon.in/Philips-HP8100-Hair-Dryer/dp/B005YVU672");
			/*
			 * Start the crawl. This is a blocking operation.
			 */
			controller.startNonBlocking(ProductCrawler.class, 1);
			
			//Thread.sleep(10 * 1000);
			//controller.shutdown();
		    //controller.waitUntilFinish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
