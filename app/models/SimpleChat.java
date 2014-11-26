package models;

/**
 * Created by cookie on 22.11.14.
 */
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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

    public static class   connectionList {
        public String username;
        public String messageTo;
        public WebSocket.In<String> in;
        public WebSocket.Out<String> out;
        public  connectionList(String username,String messageTo,WebSocket.In<String> in, WebSocket.Out<String> out) {
            this.username = username;
            this.messageTo = messageTo;
            this.in = in;
            this.out = out;
        }
    }



    // список соединении
    private static List<WebSocket.Out<String>> connections = new ArrayList<WebSocket.Out<String>>();
    private static Map<String, WebSocket.Out<String>> usersConnections = new HashMap<String, WebSocket.Out<String>>();
    //start
    private static Map<WebSocket.Out<String>, connectionList> activeConnection = new HashMap<>();


    //end
    private static String chatwith1;

    public static void start(String username, String chatWith,WebSocket.In<String> in, WebSocket.Out<String> out){


        if(activeConnection.containsKey(out)) {
            System.out.println("___________________");
            System.out.println("Внимание, сопадение ключа, этого не должно происходить");
            System.out.println("___________________");
        }
        activeConnection.put(out, new connectionList(username, chatWith, in, out));

        connectionList all;
        all = activeConnection.get(out);

        System.out.println("new "+activeConnection.size());
        in.onMessage(new Callback<String>() {
            @Override
            public void invoke(String s) throws Throwable {
                SimpleChat.sendMessageTo(s);
            }
        });



        in.onClose(new Callback0(){
            @Override
            public void invoke(){
                connectionList all;
                for(WebSocket.Out<String> out : activeConnection.keySet()) {
                    all = activeConnection.get(out);
                    if(all.in.equals(in)) {
                        activeConnection.remove(out);
                        System.out.println("connection closed");
                    }
                }

            }
        });
    }
    public static void sendMessageTo(String message) throws JSONException {
        JSONObject jsonObj = new JSONObject(message);
        String to = jsonObj.get("to").toString();
        String from = jsonObj.get("from").toString();
        System.out.println(message);
        connectionList all;
        System.out.println(" From = "+from+ " To = "+to);
        for(WebSocket.Out<String> out : activeConnection.keySet()) {
            all = activeConnection.get(out);
            System.out.println("Connection user "+ all.username + "to user " + all.messageTo);

            if(all.username.equals(to) && all.messageTo.equals(from) ) {
                out.write(message);

            }
            if(all.username.equals(from)) {
                out.write(message);
            }
       }
    }

    public static void notifyAll(String message){
        for (WebSocket.Out<String> out : connections) {

            out.write(message);
        }
    }
}