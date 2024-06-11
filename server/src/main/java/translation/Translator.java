package translation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import typeadapter.CommandTypeAdapter;
import websocket.commands.UserGameCommand;

public class Translator {
    private static Gson gson = prepareGson();


    public static <T> T fromJsonToObject(Request request, Class<T> classOfT) {
        return gson.fromJson(request.body(), classOfT);
    }

    public static Object fromObjectToJson(Object result){
        return gson.toJson(result);
    }

    public static <T> T fromJsontoObjectNotRequest(String string, Class<T> classOfT){
        return gson.fromJson(string, classOfT);
    }

    private static Gson prepareGson(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new CommandTypeAdapter());
        return builder.create();
    }

}
