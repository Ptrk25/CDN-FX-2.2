package imgReceiver;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class IconDownloader {

    private final String ADDRESS = "https://idbe-ctr.cdn.nintendo.net/icondata/10/";
    private byte[] numArray1;
    private byte[] encryptedData;

    public IconDownloader(String titleID){
        try{
            fixSSL();
            download(titleID);
        }catch (Exception e){
            //
        }

    }

    private void fixSSL() throws Exception{
        /* Start of Fix */
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /* End of the fix*/
    }

    private void download(String titleID) throws Exception{
        URL url = new URL(ADDRESS + titleID + ".idbe");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = url.openStream ();
            byte[] byteChunk = new byte[4096];
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
            System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            //e.printStackTrace ();
        }
        finally {
            if (is != null) { is.close(); }
        }

        numArray1 = baos.toByteArray();
        encryptedData = Arrays.copyOfRange(numArray1, 2, numArray1.length);
    }

    public byte[] getEncryptedData(){
        return encryptedData;
    }

    public int getKeyIndex(){
        return numArray1[1];
    }

}
