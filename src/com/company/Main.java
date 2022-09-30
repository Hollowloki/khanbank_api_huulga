package com.company;


import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static String access_token_value;
    public static String refresh_token_value;
    public static String account;
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.print("Username=");
        String username = scan.nextLine();
        System.out.print("Password=");
        String password = scan.nextLine();
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        login(username,encodedPassword.toString());


    }
    public static void refresh(String refresh_token) throws Exception {
        URL url = new URL("https://api.khanbank.com:9003/v1/cfrm/auth/token?grant_type=refresh_token&refresh_token=" + refresh_token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty("Authorization", "Basic Vm00eHFtV1BaQks3Vm5UYjNRRXJZbjlJZkxoWmF6enI6dElJQkFsU09pVXIwclV5cA==");
        conn.setRequestProperty("device-id", "9C41AF78-DC17-46A4-A31E-0533782C743D");
        conn.setRequestProperty("Accept-Language", "'mn-MN'");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        JSONObject obj = new JSONObject();
        obj.put("grant_type", "refresh_token");
        obj.put("refresh_token", refresh_token);
        StringWriter out = new StringWriter();
        obj.write(out);
        String jsonText = out.toString();
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonText.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

    }
    public static void login(String username, String password) throws Exception {
        String resjson;
        URL url = new URL("https://api.khanbank.com:9003/v1/cfrm/auth/token?grant_type=password");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty("Accept", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", "Basic Vm00eHFtV1BaQks3Vm5UYjNRRXJZbjlJZkxoWmF6enI6dElJQkFsU09pVXIwclV5cA==");
        conn.setRequestProperty("Device-id", "9C41AF78-DC17-46A4-A31E-0533782C743D");
        conn.setRequestProperty("Accept-Language", "'mn-MN'");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        JSONObject obj = new JSONObject();
        obj.put("grant_type", "password");
        obj.put("username", username);
        obj.put("password", password);
        obj.put("channelId", "I");
        obj.put("languageId", "003");
        StringWriter out = new StringWriter();
        obj.write(out);
        String jsonText = out.toString();


        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonText.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            resjson = response.toString();

        }
        ArrayList<String> actoken = new ArrayList<String>();
        ArrayList<String> reftoken = new ArrayList<String>();
        for(int i =19; i<47; i++ ) {

            actoken.add(String.valueOf(resjson.charAt(i)));
        };
        for(int i =101; i<133; i++ ) {

            reftoken.add(String.valueOf(resjson.charAt(i)));
        };

       String str = String.join("", actoken);
        String str2 = String.join("", reftoken);
       access_token_value = str;
       refresh_token_value = str2;
       if(resjson != null) {
           Scanner sc = new Scanner(System.in);
           System.out.print("Account=");
           String account = sc.nextLine();
           while(true) {
               recentTransactions(access_token_value, account);
               TimeUnit.SECONDS.sleep(3);
           }

           //refresh(refresh_token_value);
       }

    }
    public static void recentTransactions(String actoken,String account) throws Exception {
        try {
            String resjson;
            URL url = new URL("https://api.khanbank.com:9003/v1/omni/user/custom/recentTransactions?account="+account);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod( "GET" );
            conn.setRequestProperty("Host", "api.khanbank.com:9003");
            conn.setRequestProperty("Accept", "application/json, text/plain, */*");
            conn.setRequestProperty("Authorization", "Bearer " + actoken);
            conn.setRequestProperty("device-id", "C41AF78-DC17-46A4-A31E-0533782C743D");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
            conn.setRequestProperty("Accept-Language", "'mn-MN'");
            conn.setRequestProperty("Secure", "yes");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setDoOutput(true);
            try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.toString());

                }
                String resstring = response.toString();
                System.out.println(resstring);



            }

            System.out.println(conn.getResponseCode());
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
