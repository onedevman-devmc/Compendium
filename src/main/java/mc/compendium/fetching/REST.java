package mc.compendium.fetching;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class REST {

    public static Gson GSON = new Gson();

    public enum METHOD {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        HEAD("HEAD"),
        OPTIONS("OPTIONS"),
        PATCH("PATCH");

        //

        private final String method;

        //

        private METHOD(String method) {
            this.method = method;
        }

        //

        public String get() { return this.method; }

    }

    //

    public record Response(int code, String message, Map<String, String> headers, String content) {

        public Response(
            int code,
            String message,
            Map<String, String> headers,
            String content
        ) {
            this.code = code;
            this.message = message;
            this.headers = headers;
            this.content = content;
        }

    }

    //

    public static Response request(String endpoint, METHOD method, Map<String, String> headers) throws IOException {
        URL requestUrl = new URL(endpoint.toString());

        HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
        conn.setInstanceFollowRedirects(true);

        //

        conn.setRequestMethod(method.get());

        //

        List<String> headerNameList = new ArrayList<>(headers.keySet());
        int headerNameListLength = headerNameList.size();

        String headerName;
        for(int i = 0; i < headerNameListLength; ++i) {
            headerName = headerNameList.get(i);
            conn.setRequestProperty(headerName, headers.get(headerName));
        }

        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        //

        StringBuffer bodyContentConstructor = null;

        if(method != METHOD.HEAD) {
            InputStream connectionIn = conn.getResponseCode() > 299 ? conn.getErrorStream() : conn.getInputStream();
            BufferedReader responseBodyReader = new BufferedReader(new InputStreamReader(connectionIn));

            bodyContentConstructor = new StringBuffer();
            String line;
            while ((line = responseBodyReader.readLine()) != null) {
                bodyContentConstructor.append(line);
            }

            connectionIn.close();
        }

        Map<String, String> responseHeaders = new HashMap<>();
        for (Map.Entry<String, List<String>> entries : conn.getHeaderFields().entrySet()) {
            StringBuilder value = new StringBuilder();

            List<String> valueParts = entries.getValue();
            int valuePartCount = valueParts.size();

            for (int i = 0; i < valuePartCount; ++i)
                value.append(valueParts.get(i)).append(i < valuePartCount - 1 ? "," : "");

            responseHeaders.put(entries.getKey(), value.toString());
        }

        conn.disconnect();

        return new Response(
            conn.getResponseCode(),
            conn.getResponseMessage(),
            responseHeaders,
            bodyContentConstructor == null ? null : bodyContentConstructor.toString()
        );
    }

}
