package models;

/**
 * Created by cookie on 22.11.14.
 */
import javassist.tools.web.Webserver;
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
        if(usersConnections.containsKey(username)) {System.out.println("Совпадение ключа"); }
        usersConnections.put(username, out);

        in.onMessage(new Callback<String>() {
            @Override
            public void invoke(String s) throws Throwable {
                SimpleChat.sendMessageTo(s);
            }
        });



        in.onClose(new Callback0(){
            @Override
            public void invoke(){


            }
        });
    }

    public static void sendMessageTo(String message) throws JSONException {
        JSONObject jsonObj = new JSONObject(message);
        String to = jsonObj.get("to").toString();
        String from = jsonObj.get("from").toString();
        System.out.println(message);
       for(String key : usersConnections.keySet()) {            // поиск пользователя находящемся в чате
           if(key.equals(to)) {
               WebSocket.Out<String> out = usersConnections.get(key);  // отправка сообщения нужном пользователю
               out.write(message);

               WebSocket.Out<String> out2 = usersConnections.get(from); // вывод отправленого сообщения
               out2.write(message);

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