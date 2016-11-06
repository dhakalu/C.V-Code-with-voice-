//
//import com.mashape.unirest.request.HttpRequest;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
///**
// * Created by iamupe on 11/5/16.
// */
//public class App {
//
//    public static void main(String[] args) {
//
//
//        HttpRequest
//        //new TextEditor().getOverflowData();
//        try {
//
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpGet getRequest = new HttpGet(
//                    "https://api.stackexchange.com/2.2/search/advanced?order=desc&sort=activity&q=while%20loop%20java&site=stackoverflow");
//            getRequest.addHeader("accept", "application/json");
//
//            HttpResponse response = httpClient.execute(getRequest);
//
//            if (response.getStatusLine().getStatusCode() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatusLine().getStatusCode());
//            }
//
//            BufferedReader br = new BufferedReader(
//                    new InputStreamReader((response.getEntity().getContent())));
//
//            String output;
//            System.out.println("Output from Server .... \n");
//            while ((output = br.readLine()) != null) {
//                System.out.println(output);
//            }
//
//            httpClient.getConnectionManager().shutdown();
//
//        } catch (ClientProtocolException e) {
//
//            e.printStackTrace();
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        }
//    }
//}
