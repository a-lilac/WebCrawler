package org.com.scandid.crawler;

/**
 * @author ajish
 * Product Crawler
 */

import java.util.regex.Pattern;
import org.com.scandid.core.ProductDetailParser;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class ProductCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith("http://www.amazon.in/")
				&& !href.contains("/help/")
				&& !href.contains("/customer/")
				&& !href.contains("/registry/");
	}
	
	
	@Override
	public void visit(Page page) {
		
		// get its own URL
		String url = page.getWebURL().getURL();
		
		//System.out.println("Adding URL to list: " + url);
		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            
            ProductDetailParser pdr = new ProductDetailParser(html);
            if (pdr.checkIfProductDetailsPage(url)) {
            	System.out.println("-------------------------------------------------------");
            	System.out.println("Product Name : " + pdr.getName());
            	System.out.println("Product Brand : " + pdr.getBrand());
            	System.out.println("Product Price : " + pdr.getPrice());
            	System.out.println("Product Rating : " + pdr.getRating());
            	System.out.println("Product Availability : " + pdr.getAvailability());
            	System.out.println("Product Seller : " + pdr.getSeller());
            }   
		}
	}
}
