package API;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class APIVk {
    public static String email = "***";
    public static String pass = "***";
    public static String  access_token = "***";
    public static String UserName = null;
    public static void SetConnection() throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost post = new HttpPost("https://oauth.vk.com/token?grant_type=password" +
                "&client_id=***" +
                "&client_secret=****" +
                "&username=" + email +
                "&password=" + pass +
                "&v=5.52");

        HttpResponse response = httpClient.execute(post);
        post.abort();
        URL Redirect = new URL(post.getURI().toString());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Redirect.openStream()));
        String link = bufferedReader.readLine();
        System.out.println(link);
    }

    public static String GetMsg(String Count) throws IOException, ParseException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(
                "https://api.vk.com/method/" +
                        "messages.get" +
                        "?out=0" +
                        "&count=" + Count +
                        "&access_token=" + access_token);
        HttpResponse response = httpClient.execute(post);
        post.abort();

        String line = "";
        URL url = new URL(post.getURI().toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        line = reader.readLine();
        reader.close();

        String UserId  = ParseJSONMsg(line, "uid");    //get userID
        UserName = GetUsernm(UserId); //get UserName by UserId
        String msgText = ParseJSONMsg(line, "body");   //get message text

        return msgText;
    }

    public static String ParseJSONMsg(String line, String param) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(line);
        JSONArray postList = (JSONArray) jsonObject.get("response");
        JSONObject Unic = null;
        String msg = null;
        for (int i = 1; i < postList.size(); i++) {
            Unic = (JSONObject) postList.get(i);
            msg = Unic.get(param).toString();
        }
        return msg;
    }

    public static String GetUsernm(String userId) throws IOException, ParseException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://api.vk.com/method/" +
                "users.get" +
                "?user_ids=" + userId +
                "&access_token=" + access_token);
        HttpResponse response = httpClient.execute(post);
        post.abort();

        URL Redirect = new URL(post.getURI().toString());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Redirect.openStream(), "UTF-8"));
        String line = bufferedReader.readLine();
        bufferedReader.close();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(line);
        JSONArray postList = (JSONArray) jsonObject.get("response");
        JSONObject Unic = null;
        String usrnm = null;
        try {
            Unic = (JSONObject) postList.get(0);
            usrnm = String.valueOf(Unic.get("first_name")) + " " + String.valueOf(Unic.get("last_name"));
        } catch (Exception e) {};
        return usrnm;
    }
}
