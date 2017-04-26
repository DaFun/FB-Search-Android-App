/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.ban.fb_search.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String SERVER_URL =
            "http://sample-env.bcz3r45x9e.us-west-2.elasticbeanstalk.com";

    final static String PARAM_QUERY = "q";
    final static String PARAM_TYPE = "type";
    final static String PARAM_ID = "id";
    final static String FB = "https://graph.facebook.com/v2.8/";
    final static String TOKEN = "EAADstsKuf0MBAO67Hm3DRV5bUx34NzfZAcrSPXid0Eky1ZAMin7YoamWnusmRj7mxF7Ns3J8Au1qNL11iDQCx7Fp4RFZADbXskaszqwCTXwmMQoIpO74FlODxtKPviXbUxHxKMnaJts0wihu8XKoj1bxlgjQfkZD";

    /**
     * Builds the URL used to query Github.
     *
     * @param searchQuery The keyword that will be queried for.
     * @param type 1 of 5 types.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String searchQuery, String type, String lat, String lon) {
        Uri builtUri;
        switch (type) {
            case "place":
                builtUri = Uri.parse(SERVER_URL).buildUpon()
                        .appendQueryParameter(PARAM_QUERY, searchQuery)
                        .appendQueryParameter(PARAM_TYPE, type)
                        .appendQueryParameter("lat", lat)
                        .appendQueryParameter("lon", lon)
                        .build();
                break;
            case "id":
                builtUri = Uri.parse(SERVER_URL).buildUpon()
                        .appendQueryParameter(PARAM_ID, searchQuery)
                        .build();
                break;
            case "link":
                builtUri = Uri.parse(SERVER_URL).buildUpon()
                        .appendQueryParameter("link", searchQuery)
                        .build();
                break;
            default:
                builtUri = Uri.parse(SERVER_URL).buildUpon()
                        .appendQueryParameter(PARAM_QUERY, searchQuery)
                        .appendQueryParameter(PARAM_TYPE, type)
                        .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}