package com.example.easyflight;


    import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

    public class AirTransferAPI extends AsyncTask<String, Void, String> {
        private static final String API_URL = "https://api.example.com/air-transfer";
        private static final String API_KEY = "YOUR_API_KEY";

        private OnAirTransferListener listener;

        public AirTransferAPI(OnAirTransferListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String origin = params[0];
            String destination = params[1];
            String departureDate = params[2];

            try {
                URL url = new URL(API_URL + "?origin=" + origin + "&destination=" + destination );
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                } else {
                    Log.e("AirTransferAPI", "HTTP error code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e("AirTransferAPI", "Error: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray flights = jsonResponse.getJSONArray("flights");
                    listener.onAirTransferSuccess(flights);
                } catch (JSONException e) {
                    Log.e("AirTransferAPI", "JSON parsing error: " + e.getMessage());
                    listener.onAirTransferError("Failed to parse API response.");
                }
            } else {
                listener.onAirTransferError("Failed to fetch air transfer data.");
            }
        }

        public interface OnAirTransferListener {
            void onAirTransferSuccess(JSONArray flights);
            void onAirTransferError(String errorMessage);
            // air transfer
        }
    }






