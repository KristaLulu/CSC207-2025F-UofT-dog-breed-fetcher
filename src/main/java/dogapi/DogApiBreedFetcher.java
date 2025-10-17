package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.

        // ① 输入校验：早失败（fail fast），避免去请求无效参数
        if (breed == null || breed.isBlank()) {
            // 这里传入“Invalid breed”是为了 e.getMessage() 更友好
            throw new BreedNotFoundException("Invalid breed");
        }
        // ② 构造 URL：按 dog.ceo 文档约定的路径，并做“规范化”
        String url = "https://dog.ceo/api/breed/" + breed + "/list";

        // ③ 创建 HTTP 请求对象（OkHttp 的标准用法）
        Request request = new Request.Builder().url(url).build();

        // ④ 发送请求并接收响应（同步调用）
        //    try (...) 是“try-with-resources”语法：会在块结束时自动关闭 response（防资源泄漏）
        try (Response response = client.newCall(request).execute()) {

            // ⑤ 兜底：极端情况下 body 可能为 null（网络代理/拦截器异常等）
            if (response.body() == null) {
                // 作业要求：所有失败场景都要映射成 BreedNotFoundException
                throw new BreedNotFoundException(breed);
            }
        // ⑥ 读取字符串正文并解析 JSON（只能 .string() 一次）
        String body = response.body().string();
        JSONObject json = new JSONObject(body);
        // ⑦ 解析 JSON 并按 API 约定处理各种情况; dog.ceo 的 JSON 有 "status": "success" 或 "error"
        String status = json.getString("status");
        if ("error".equalsIgnoreCase(status)) {
            // ⑧ API 明确表示“找不到”：按作业要求，统一抛 BreedNotFoundException（“领域化异常”）
            throw new BreedNotFoundException(breed);
        }
        // ⑨ 取出数据区："message" 是一个字符串数组（子品种列表）
        JSONArray arr = json.getJSONArray("message");
        // ⑩ 把 JSONArray 转成 Java List<String>
        List<String> subBreeds = new ArrayList<>();
        for(int i = 0; i < arr.length(); i++) {
            subBreeds.add(arr.getString(i));
        }
        return subBreeds;
    } catch (IOException e) {
            // ⑪ 网络/IO 失败：按作业要求，统一抛 BreedNotFoundException（“领域化异常”）
            throw new BreedNotFoundException(breed);
        }
    }
}