import java.net.URL;
import java.io.*;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

public class JavaNetSslExample
{
  private static SSLContext getWeakSSLContext() throws Exception {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }

      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
    }};

    // Ignore differences between given hostname and certificate hostname
    HostnameVerifier hv = new HostnameVerifier() {
      @Override
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("TLSv1.2");
    sc.init(null, trustAllCerts, new SecureRandom());
    return sc;
  }

  public static void main(String[] args) throws Exception
  {
    SSLContext sc = getWeakSSLContext();
    SSLContext.setDefault(sc);

    SSLContext context = SSLContext.getDefault();
    SSLSocketFactory sf = context.getSocketFactory();
    String[] cipherSuites = sf.getSupportedCipherSuites();

    System.out.println("CipherSuite:");
    for (String cipher : cipherSuites) {
      System.out.println("  " + cipher);
    }

    String httpsURL = "https://maushaus.party/";
    URL myurl = new URL(httpsURL);
    HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
    InputStream ins = con.getInputStream();
    InputStreamReader isr = new InputStreamReader(ins);
    BufferedReader in = new BufferedReader(isr);

    String inputLine;

    while ((inputLine = in.readLine()) != null)
    {
      System.out.println(inputLine);
    }

    in.close();
  }
}