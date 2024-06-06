package translationForClient;

import com.google.gson.Gson;

public class TranslatorForClient {
    private static Gson gson = new Gson();

    public static Object fromObjectToJson(Object result){
        return gson.toJson(result);
    }

    public static <T> T fromJsontoObjectNotRequest(String string, Class<T> classOfT){
        return gson.fromJson(string, classOfT);
    }

}
