package models;

/**
 * Created by cookie on 22.11.14.
 */
import jdk.nashorn.internal.parser.JSONParser;
import play.mvc.*;
import play.libs.*;
import play.libs.F.*;
import org.json.JSONObject;
import java.util.*;

import static play.mvc.Controller.request;

public class SimpleChat{

    // collect all websockets here
    private static List<WebSocket.Out<String>> connections = new ArrayList<WebSocket.Out<String>>();
    public static List<String> users;
    public static void start(WebSocket.In<String> in, WebSocket.Out<String> out){

        for(int i=0; i<connections.size();i++) {
            System.out.println(connections.get(i) +"");
      //  play.core.j.JavaWebSocket.webSocketWrapper(connections);
        }

        connections.add(out);

        in.onMessage(new Callback<String>() {
            @Override
            public void invoke(String s) throws Throwable {
                JSONObject jsonObj = new JSONObject(s);

                SimpleChat.notifyAll(s);


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

    // Iterate connection list and write incoming message
    public static void notifyAll(String message){

        for (WebSocket.Out<String> out : connections) {

            out.write(message);
        }
    }
}