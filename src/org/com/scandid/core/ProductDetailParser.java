package org.com.scandid.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProductDetailParser {
	public static final String brandPattern = "a[id=brand]";
	public static final String nonAsciiPattern = "\\P{Print}";
	public static final String salePricePattern = "span[id=priceblock_saleprice]";
	public static final String ourPricePattern = "span[id=priceblock_ourprice]";
	public static final String dealPricePattern = "span[id=priceblock_dealprice]";
	public static final String productNamePattern = "span[id=productTitle]";
	public static final String productAvgRating = "span.crAvgStars";
	public static final String productAvailability = "div[id=availability]";
	public static final String productSeller = "div[id=merchant-info]";
	
	Document document;
	
	public ProductDetailParser(String html) {
		document = Jsoup.parseBodyFragment(html);
	}
	
	public String getPrice() {
		String price = "NA";
        Elements priceElements;
        if ((priceElements = document.select(salePricePattern)).size() > 0) {
        	price = priceElements.get(0).text().trim();
        } else if ((priceElements = document.select(ourPricePattern)).size() > 0) {
        	price = priceElements.get(0).text().trim();
        } else {
        	priceElements = document.select(dealPricePattern);
        	price = priceElements.get(0).text().trim();
        }
        return price.replaceAll(nonAsciiPattern, "");
	}
	
	public String getBrand() {
		String brand = "NA"; 
		brand = document.select(brandPattern).get(0).text();
        return brand.replaceAll(nonAsciiPattern, "");
	}
	
	public String getName() {
		String title = "NA";
		title = document.select(productNamePattern).get(0).text();
		return title.replaceAll(nonAsciiPattern, "");
	}
	
	public boolean checkIfProductDetailsPage(String url) {
		boolean retVal = false;
		Elements ele = document.select("[name=ASIN]");
		
		if((ele == null) || (ele.isEmpty())){
			return false;
		}
		
		String ASIN = "NA";
		
		// Find the first occurrence of the attribute "value" with the attribute "name=ASIN"
		for(int i = 0; i < ele.size(); i++){
			if(ele.get(i).hasAttr("value")){
				ASIN = ele.first().attr("value");
				i = ele.size();
			}
		}
		
		if (url.contains("/dp/"+ASIN) || url.contains("/gp/"+ASIN)) {
			return true;
		}
		
		return retVal;
	}
	
	public String getRating() {
		Element starRating = document.select(productAvgRating).first();
		
		if(starRating == null){
			return "NA";
		}
		
		String ratingInfo = starRating.html();	
		String ratingSecondDelimiter = "out of 5 stars";
		
		int ratingLocation = ratingInfo.indexOf(ratingSecondDelimiter);
		String ratingVal = ratingInfo.substring(ratingLocation - 4, ratingLocation - 1);
		return ratingVal;
	}
	
	public String getAvailability() {
		String retVal = "Unavailable";
		Element availability = document.select(productAvailability).first();
		if(availability == null) {
			return retVal;
		}
		
		retVal = availability.text();
		return retVal;
	}
	
	private String getSubstring(String beginDelim, String endDelim, String searchString){
		int stringLocation = searchString.indexOf(beginDelim);
		
		if(stringLocation == -1){
			return "";
		}
		
		// Cut out the specified substring given by the two delimiter words
		String resultVal = searchString.substring(stringLocation + beginDelim.length());
		stringLocation = resultVal.indexOf(endDelim);
		
		if(stringLocation == -1){
			return "";
		}
		
		resultVal = resultVal.substring(0, stringLocation);
		
		return resultVal;
	}
	
	public String getSeller() {
		String seller = "NA";
		String firstDelim = "<b>";
		String secondDelim = "</b>";
		Element sellerInfo = document.select(productSeller).first();
		if(sellerInfo == null) {
			return seller;
		}
		seller = sellerInfo.html();
		//System.out.println("Seller : " + seller);
		
		if(seller.contains("a href")){
			firstDelim = "\">";
			secondDelim = "</a>";
		}
		
		seller = getSubstring(firstDelim, secondDelim, seller);
		
		return seller;
	}
}
