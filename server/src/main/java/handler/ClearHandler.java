package handler;

import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        //there won't be anything in the request body, so no need to generate an object
        //create a service object
        //call clear on that object
        //return a success string

        return null;
    }
}
