import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;


/**
 * Created by iamupen on 11/5/16.
 */
public class HttpUtils {





    public JSONObject getJSON(String urlString) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int status = conn.getResponseCode();
            BufferedReader bufferedReader = null;

            if (conn.getHeaderField("Content-Encoding")!=null && conn.getHeaderField("Content-Encoding").equals("gzip")){
                bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream())));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = bufferedReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            return new JSONObject(responseStrBuilder.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //conn.clo
        }
        return null;
    }
}
