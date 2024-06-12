package translationForClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import typeadapter.CommandTypeAdapter;
import typeadapter.ServerMessageTypeAdapter;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class TranslatorForClient {
    private static Gson gson = prepareGson();

    public static Object fromObjectToJson(Object result){
        return gson.toJson(result);
    }

    public static <T> T fromJsontoObjectNotRequest(String string, Class<T> classOfT){
        return gson.fromJson(string, classOfT);
    }

    private static Gson prepareGson(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServerMessage.class, new ServerMessageTypeAdapter());
        return builder.create();
    }

}
