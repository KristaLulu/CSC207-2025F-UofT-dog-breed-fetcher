package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private int callsMade = 0;
    private final BreedFetcher fetcher;              // 被包装的实际 fetcher
    private final Map<String, List<String>> cache;   // 缓存，key=规范化的品种名

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
        this.cache = new HashMap<>();

    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // return statement included so that the starter code can compile and run.
        if (breed == null || breed.isBlank()) {
            throw new BreedNotFoundException("Invalid breed");
        }
        String key = breed.toLowerCase();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        // ② 缓存未命中 → 调用底层 fetcher
        callsMade++;
        try {
            List<String> result = fetcher.getSubBreeds(breed);//fetcher 是你包装的“真实”实现，比如 DogApiBreedFetcher
                                                             // 或 BreedFetcherForLocalTesting。
                                                            //它会去访问 API 或本地表，返回子品种列表。
            cache.put(key, result);     // ③ 只在成功时缓存，//如果品种不存在，它会 throw new BreedNotFoundException(breed)
            return result;
        } catch (BreedNotFoundException e) {
            // ❌ 不缓存错误
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}