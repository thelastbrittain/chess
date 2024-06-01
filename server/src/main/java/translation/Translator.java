package translation;

import com.google.gson.Gson;
import spark.Request;

public class Translator {
    private static Gson gson = new Gson();


    public static <T> T fromJsonToObject(Request request, Class<T> classOfT) {
        return gson.fromJson(request.body(), classOfT);
    }

    public static Object fromObjectToJson(Object result){
        return gson.toJson(result);
    }

    public static <T> T fromJsontoObjectNotRequest(String string, Class<T> classOfT){
        return gson.fromJson(string, classOfT);
    }

}
