package translation;

import com.google.gson.Gson;
import spark.Request;

public class Translator {
    private static Gson gson = new Gson();


    public static <T> T fromJsonToObject(Request request, Class<T> classOfT) {
        System.out.println("Made it inside new class");
//        System.out.println("This is what I'm translating it to:  " + gson.toJson(request.body(), classOfT);
        return gson.fromJson(request.body(), classOfT);
    }

    public static Object fromObjectToJson(Object result){
        return gson.toJson(result);
    }
}
