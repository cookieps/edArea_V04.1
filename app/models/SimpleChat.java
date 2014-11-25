package models;

/**
 * Created by cookie on 22.11.14.
 */
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONException;
import play.mvc.*;
import play.libs.*;
import play.libs.F.*;
import org.json.JSONObject;

import java.nio.channels.ClosedChannelException;
import java.util.*;

import static play.mvc.Controller.request;

public class SimpleChat{

    // список соединении
    private static List<WebSocket.Out<String>> connections = new ArrayList<WebSocket.Out<String>>();
    private static Map<String, WebSocket.Out<String>> usersConnections = new HashMap<String, WebSocket.Out<String>>();
    public static void start(String username,WebSocket.In<String> in, WebSocket.Out<String> out){
        connections.add(out);
      //  Map<String, WebSocket.Out<String>> usersConnections = new HashMap<String, WebSocket.Out<String>>();
        usersConnections.put(username,out);

        in.onMessage(new Callback<String>() {
            @Override
            public void invoke(String s) throws Throwable {
                SimpleChat.sendMessageTo(s);
              //  SimpleChat.notifyAll(s);


            }
        });


 //       in.onMessage(new Callback<String>(){
 //           public void invoke(String event){
  //              SimpleChat.notifyAll(event);
   //         }
   //     });

        in.onClose(new Callback0(){
            public void invoke(){

                SimpleChat.notifyAll("A connection closed");
            }
        });
    }

    public static void sendMessageTo(String message) throws JSONException {
        JSONObject jsonObj = new JSONObject(message);
        String to = jsonObj.get("to").toString();
        System.out.println(message);
       for(String key : usersConnections.keySet()) {
           if(key.equals(to)) {
               System.out.println("соединение номер" + usersConnections.get(key));
               WebSocket.Out<String> out = usersConnections.get(key);
               out.write(message);
           }
       }
    }


    // Iterate connection list and write incoming message
    public static void notifyAll(String message){
        for (WebSocket.Out<String> out : connections) {

            out.write(message);
        }
    }
}