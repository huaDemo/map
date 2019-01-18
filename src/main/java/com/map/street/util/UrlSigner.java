package com.map.street.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;  // JDK 1.8 only - older versions may need to use Apache Commons or similar.
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author: hua
 * @date: 2019/1/18 09:37
 */
public class UrlSigner {

    // Note: Generally, you should store your private key someplace safe
    // and read them into your code

    private static String keyString = "HnbFKX9fS509yemnyFzBH_D1vGQ=";

    // The URL shown in these examples is a static URL which should already
    // be URL-encoded. In practice, you will likely have code
    // which assembles your URL from user or web service input
    // and plugs those values into its parameters.
    private static String urlString = "https://maps.googleapis.com/maps/api/streetview?location=41.403609,2.174448&size=456x456&key=AIzaSyCnNtqbkGPTsqwD4PuS7O7sKA-QKj2Qbxs";

    // This variable stores the binary key, which is computed from the string (Base64) key
    private static byte[] key;

    public static void main(String[] args) throws IOException,
            InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String inputUrl, inputKey = null;

        // For testing purposes, allow user input for the URL.
        // If no input is entered, use the static URL defined above.
        System.out.println("Enter the URL (must be URL-encoded) to sign: ");
        inputUrl = input.readLine();
        if (inputUrl.equals("")) {
            inputUrl = urlString;
        }

        // Convert the string to a URL so we can parse it
        URL url = new URL(inputUrl);

        // For testing purposes, allow user input for the private key.
        // If no input is entered, use the static key defined above.
        System.out.println("Enter the Private key to sign the URL: ");
        inputKey = input.readLine();
        if (inputKey.equals("")) {
            inputKey = keyString;
        }

        UrlSigner signer = new UrlSigner(inputKey);
        String request = signer.signRequest(url.getPath(), url.getQuery());

        System.out.println("Signed URL :" + url.getProtocol() + "://" + url.getHost() + request);
    }

    public UrlSigner(String keyString) throws IOException {
        // Convert the key from 'web safe' base 64 to binary
        keyString = keyString.replace('-', '+');
        keyString = keyString.replace('_', '/');
        System.out.println("Key: " + keyString);
        // Base64 is JDK 1.8 only - older versions may need to use Apache Commons or similar.
        this.key = Base64.getDecoder().decode(keyString);
    }

    public String signRequest(String path, String query) throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException, URISyntaxException {

        // Retrieve the proper URL components to sign
        String resource = path + '?' + query;

        // Get an HMAC-SHA1 signing key from the raw key bytes
        SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

        // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(sha1Key);

        // compute the binary signature for the request
        byte[] sigBytes = mac.doFinal(resource.getBytes());

        // base 64 encode the binary signature
        // Base64 is JDK 1.8 only - older versions may need to use Apache Commons or similar.
        String signature = Base64.getEncoder().encodeToString(sigBytes);

        // convert the signature to 'web safe' base 64
        signature = signature.replace('+', '-');
        signature = signature.replace('/', '_');

        return resource + "&signature=" + signature;
    }

}
