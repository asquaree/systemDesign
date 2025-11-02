package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.Parser;
import com.example.monzobank.service.UrlValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * BFS (Breadth-First Search) parsing strategy for web crawling.
 * Supports both sequential and parallel execution modes.
 * Processes URLs level by level, ensuring all URLs at the same depth
 * are processed before moving to the next level.
 */
public class BFSParsingStrategy implements ParsingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(BFSParsingStrategy.class);
    private static final int MAX_LINKS = 100;

    private final Executor executor;
    private Parser parser;

    // Shared state for thread-safe crawling
    private Set<String> visited;
    private Map<String, Url> urlMap;
    private AtomicInteger visitedCount;

    public BFSParsingStrategy() {
        this(null);
    }

    public BFSParsingStrategy(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Url parse(String urlString, Parser parser, int maxDepth) {
        validateInputs(urlString, parser, maxDepth);
        initialize(parser, urlString);
        logParsingStart(urlString, maxDepth);

        Url rootUrl = urlMap.get(urlString);
        crawlBreadthFirst(urlString, maxDepth);

        logParsingComplete();
        return rootUrl;
    }

    // ==================== Initialization ====================

    private void validateInputs(String urlString, Parser parser, int maxDepth) {
        if (urlString == null || urlString.trim().isEmpty()) {
            throw new IllegalArgumentException("URL string cannot be null or empty");
        }
        if (parser == null) {
            throw new IllegalArgumentException("Parser cannot be null");
        }
        if (maxDepth < 0) {
            throw new IllegalArgumentException("Max depth cannot be negative");
        }
    }

    private void initialize(Parser parser, String rootUrlString) {
        this.parser = parser;
        this.visited = Collections.synchronizedSet(new HashSet<>());
        this.urlMap = new ConcurrentHashMap<>();
        this.visitedCount = new AtomicInteger(0);

        // Add root URL to visited
        Url rootUrl = new Url(rootUrlString);
        addToVisited(rootUrlString, rootUrl);
    }

    // ==================== BFS Crawling ====================

    private void crawlBreadthFirst(String rootUrlString, int maxDepth) {
        Queue<String> currentLevel = new ConcurrentLinkedQueue<>(Collections.singletonList(rootUrlString));
        int depth = 0;

        while (shouldContinueCrawling(currentLevel, depth, maxDepth)) {
            Queue<String> nextLevel = processLevel(currentLevel, depth, maxDepth);
            currentLevel = nextLevel;
            depth++;
        }
    }

    private boolean shouldContinueCrawling(Queue<String> currentLevel, int depth, int maxDepth) {
        return !currentLevel.isEmpty()
                && visitedCount.get() < MAX_LINKS
                && depth < maxDepth;
    }

    private Queue<String> processLevel(Queue<String> currentLevel, int depth, int maxDepth) {
        List<String> levelUrls = new ArrayList<>(currentLevel);
        Queue<String> nextLevel = new ConcurrentLinkedQueue<>();

        if (executor!= null) {
            processLevelInParallel(levelUrls, depth, maxDepth, nextLevel);
        } else {
            processLevelSequentially(levelUrls, depth, maxDepth, nextLevel);
        }

        return nextLevel;
    }


    // ==================== Parallel Processing ====================

    private void processLevelInParallel(List<String> urls, int depth, int maxDepth,
                                        Queue<String> nextLevel) {
        List<CompletableFuture<List<Url>>> futures = submitUrlProcessingTasks(urls, depth, maxDepth);
        waitForAllTasksToComplete(futures);
        collectResultsAndBuildNextLevel(futures, nextLevel, depth, maxDepth);
    }

    private List<CompletableFuture<List<Url>>> submitUrlProcessingTasks(List<String> urls,
                                                                        int depth, int maxDepth) {
        return urls.stream()
                .map(url -> CompletableFuture.supplyAsync(
                        () -> processUrl(url, depth, maxDepth),
                        executor))
                .collect(Collectors.toList());
    }

    private void waitForAllTasksToComplete(List<CompletableFuture<List<Url>>> futures) {
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        allTasks.join();
    }

    private void collectResultsAndBuildNextLevel(List<CompletableFuture<List<Url>>> futures,
                                                 Queue<String> nextLevel, int depth, int maxDepth) {
        for (CompletableFuture<List<Url>> future : futures) {
            try {
                List<Url> children = future.get();
                if (children != null) {
                    addChildrenToNextLevel(children, nextLevel, depth, maxDepth);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted while processing URL", e);
            } catch (ExecutionException e) {
                logger.error("Error processing URL in parallel", e.getCause());
            }
        }
    }

    // ==================== Sequential Processing ====================

    private void processLevelSequentially(List<String> urls, int depth, int maxDepth,
                                          Queue<String> nextLevel) {
        for (String url : urls) {
            List<Url> children = processUrl(url, depth, maxDepth);
            if (children != null) {
                addChildrenToNextLevel(children, nextLevel, depth, maxDepth);
            }
        }
    }

    // ==================== URL Processing ====================

    private List<Url> processUrl(String urlString, int depth, int maxDepth) {
        logger.info("Processing URL: {} at depth: {}", urlString, depth);
        if (!canProcessUrl(depth, maxDepth)) {
            return null;
        }

        List<String> extractedLinks = extractLinksFromUrl(urlString);
        List<Url> children = filterAndAddValidLinks(extractedLinks);
        updateUrlWithChildren(urlString, children);

        return children;
    }

    private boolean canProcessUrl(int depth, int maxDepth) {
        return depth < maxDepth && visitedCount.get() < MAX_LINKS;
    }

    private List<String> extractLinksFromUrl(String urlString) {
        try {
            return parser.extractLinks(urlString);
        } catch (Exception e) {
            logger.error("Failed to extract links from URL: {}", urlString, e);
            return Collections.emptyList();
        }
    }

    private List<Url> filterAndAddValidLinks(List<String> links) {
        List<Url> children = new ArrayList<>();

        for (String link : links) {
            if (visitedCount.get() >= MAX_LINKS) {
                break;
            }

            if (addUrl(link)) {
                children.add(urlMap.get(link));
            }
        }

        return children;
    }

    private void updateUrlWithChildren(String urlString, List<Url> children) {
        Url urlObj = urlMap.get(urlString);
        if (urlObj != null) {
            urlObj.setChildUrls(children);
        }
    }

    // ==================== Thread-Safe Operations ====================

    private boolean addUrl(String link) {
        synchronized (visited) {
            if (isValidUnvisitedUrl(link)) {
                Url childUrl = new Url(link);
                addToVisited(link, childUrl);
                return true;
            }
            return false;
        }
    }

    private boolean isValidUnvisitedUrl(String link) {
        return UrlValidatorService.isValidLink(link) && !visited.contains(link);
    }

    private void addToVisited(String url, Url urlObj) {
        visited.add(url);
        urlMap.put(url, urlObj);
        visitedCount.incrementAndGet();
    }

    private void addChildrenToNextLevel(List<Url> children, Queue<String> nextLevel,
                                        int depth, int maxDepth) {
        for (Url child : children) {
            if (canAddToNextLevel(depth, maxDepth)) {
                nextLevel.offer(child.getUrl());
            }
        }
    }

    private boolean canAddToNextLevel(int depth, int maxDepth) {
        return visitedCount.get() < MAX_LINKS && depth + 1 < maxDepth;
    }

    // ==================== Logging ====================

    private void logParsingStart(String url, int maxDepth) {
        String depthStr = (maxDepth == Integer.MAX_VALUE) ? "UNLIMITED" : String.valueOf(maxDepth);
        String mode = executor!=null ? "PARALLEL" : "SEQUENTIAL";
        logger.info("Starting BFS crawl | URL: {} | Depth: {} | Mode: {}", url, depthStr, mode);
    }

    private void logParsingComplete() {
        logger.info("BFS crawl completed | Total URLs visited: {}", visitedCount.get());
    }
}