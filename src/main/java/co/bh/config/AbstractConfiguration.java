package co.bh.config;

import io.swagger.v3.parser.core.models.AuthorizationValue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class AbstractConfiguration {
    private static final String ACCEPT_HEADER_VALUE = "application/json, application/yaml, */*";

    public static String urlToString(String url, List<AuthorizationValue> auths) throws Exception {
        InputStream inputStream = null;
        URLConnection conn = null;
        BufferedReader br = null;

        try {
            if (auths != null) {
                StringBuilder queryString = new StringBuilder();
                // build a new url if needed
                for (AuthorizationValue auth : auths) {
                    if ("query".equals(auth.getType())) {
                        if (queryString.toString().length() == 0) {
                            queryString.append("?");
                        } else {
                            queryString.append("&");
                        }
                        queryString.append(URLEncoder.encode(auth.getKeyName(), "UTF-8"))
                                .append("=")
                                .append(URLEncoder.encode(auth.getValue(), "UTF-8"));
                    }
                }
                if (queryString.toString().length() != 0) {
                    url = url + queryString.toString();
                }
                conn = new URL(url).openConnection();

                for (AuthorizationValue auth : auths) {
                    if ("header".equals(auth.getType())) {
                        conn.setRequestProperty(auth.getKeyName(), auth.getValue());
                    }
                }
            } else {
                conn = new URL(url).openConnection();
            }

            conn.setRequestProperty("Accept", ACCEPT_HEADER_VALUE);
            conn.connect();
            InputStream in = conn.getInputStream();

            StringBuilder contents = new StringBuilder();

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(in, "UTF-8"));

            for (int i = 0; i != -1; i = input.read()) {
                char c = (char) i;
                if (!Character.isISOControl(c)) {
                    contents.append((char) i);
                }
                if (c == '\n') {
                    contents.append('\n');
                }
            }

            in.close();

            return contents.toString();
        } catch (javax.net.ssl.SSLProtocolException e) {
            System.out.println("there is a problem with the target SSL certificate");
            System.out.println("**** you may want to run with -Djsse.enableSNIExtension=false\n\n");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (br != null) {
                br.close();
            }
        }
    }
}
