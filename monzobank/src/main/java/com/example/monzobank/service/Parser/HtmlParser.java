package com.example.monzobank.service.Parser;

import com.example.monzobank.service.Parser.parsingStrategy.ParsingStrategy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser extends Parser {
    
    private static final int TIMEOUT_MS = 20000; // 20 seconds
    private static final int MAX_RETRIES = 3;
    private static final int INITIAL_RETRY_DELAY_MS = 1000; // 1 second
    
    public HtmlParser(ParsingStrategy parsingStrategy) {
        super(parsingStrategy);
        this.parseType = "html";
    }

    @Override
    public List<String> extractLinks(String urlString) {
        List<String> links = new ArrayList<>();
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                Document doc = Jsoup.connect(urlString)
                        .timeout(TIMEOUT_MS)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .followRedirects(true)
                        .ignoreHttpErrors(true)
                        .maxBodySize(0)
                        .referrer("https://www.google.com")
                        .get();
                
                Elements linkElements = doc.select("a[href]");
                
                for (Element link : linkElements) {
                    String href = link.absUrl("href");
                    if (href != null && !href.isEmpty()) {
                        links.add(href);
                    }
                }
                
                System.out.println("Successfully extracted " + links.size() + " links from: " + urlString);
                break; // Success, exit retry loop
                
            } catch (java.net.SocketTimeoutException e) {
                System.err.println("Timeout on attempt " + attempt + "/" + MAX_RETRIES + " for: " + urlString);
                if (attempt < MAX_RETRIES) {
                    int delay = INITIAL_RETRY_DELAY_MS * (int) Math.pow(2, attempt - 1);
                    System.out.println("Retrying in " + delay + "ms...");
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    System.err.println("All retries exhausted for: " + urlString);
                }
            } catch (Exception e) {
                System.err.println("Error extracting links from HTML: " + urlString + " - " + e.getMessage());
                break; // Don't retry on other errors
            }
        }
        
        return links;
    }
}
