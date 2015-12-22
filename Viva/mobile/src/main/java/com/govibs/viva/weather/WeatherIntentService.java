package com.govibs.viva.weather;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;


import com.govibs.viva.R;
import com.govibs.viva.global.Global;
import com.govibs.viva.storage.VivaLibraryPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WeatherIntentService extends IntentService {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_API_LAT_LONG =
            "http://api.openweathermap.org/data/2.5/find?%s&units=metric";

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FETCH_CITY_WEATHER = "com.govibs.iriscorelibrary.weather.action.CITY_WEATHER";
    private static final String ACTION_FETCH_CITY = "com.govibs.iriscorelibrary.weather.action.CITY";

    // TODO: Rename parameters
    private static final String EXTRA_LATITUDE = "com.govibs.iriscorelibrary.weather.extra.LATITUDE";
    private static final String EXTRA_LONGITUDE = "com.govibs.iriscorelibrary.weather.extra.LONGITUDE";
    private static final String EXTRA_CITY_ADDRESS = "com.govibs.iriscorelibrary.weather.extra.CITY_ADDRESS";

    private static Context mContext;

    public WeatherIntentService() {
        super("WeatherIntentService");
    }

    /**
     * Starts this service to perform action Weather with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionCityWeather(Context context, String cityName) {
        Intent intent = new Intent(context, WeatherIntentService.class);
        intent.setAction(ACTION_FETCH_CITY_WEATHER);
        intent.putExtra(EXTRA_CITY_ADDRESS, cityName);
        context.getApplicationContext().startService(intent);
    }

    /**
     * Starts this service to perform action Forecast with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFetchCity(Context context, double param1, double param2) {
        Intent intent = new Intent(context, WeatherIntentService.class);
        intent.setAction(ACTION_FETCH_CITY);
        intent.putExtra(EXTRA_LATITUDE, param1);
        intent.putExtra(EXTRA_LONGITUDE, param2);
        mContext = context;
        context.getApplicationContext().startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_CITY_WEATHER.equals(action)) {
                final String cityName = intent.getStringExtra(EXTRA_CITY_ADDRESS);
                handleActionFetchWeather(cityName);
            } else if (ACTION_FETCH_CITY.equals(action)) {
                final double param1 = intent.getDoubleExtra(EXTRA_LATITUDE, 0);
                final double param2 = intent.getDoubleExtra(EXTRA_LONGITUDE, 0);
                handleActionFetchCity(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     * @param cityName - the name of the city
     */
    private void handleActionFetchWeather(String cityName) {
        String response = "";
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, cityName));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-api-key",
                    mContext.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject jsonObject = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(jsonObject.getInt("cod") != 200){
                Log.i(Global.TAG, "Some error occurred.");
            }

            response = jsonObject.toString();
            Log.i(Global.TAG, "Weather: " + response);

            if(jsonObject.has("name")) {
                String temperature = jsonObject.getJSONObject("main").getString("temp");
                String windSpeed = (int)jsonObject.getJSONObject("wind").getDouble("speed") + "";
                JSONObject weatherObject = jsonObject.getJSONArray("weather").getJSONObject(0);
                String weather = weatherObject.getString("description");
                String vivaSay = weather + " with " + temperature + " degrees Celsius in " + cityName + ". Wind speed is " + windSpeed + " miles per hour.";
                VivaLibraryPreferenceHelper.saveIrisWeatherInfo(mContext, vivaSay);
                VivaLibraryPreferenceHelper.setVivaCurrentTemp(mContext, Float.parseFloat(temperature));
                VivaLibraryPreferenceHelper.setVivaCurrentWindSpeed(mContext, Float.parseFloat(windSpeed));
                VivaLibraryPreferenceHelper.setVivaCurrentWeatherState(mContext, weather);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        VivaLibraryPreferenceHelper.saveIrisWeatherInfo(mContext, response);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     * @param latitude - the latitude for the city address.
     * @param longitude - the longitude for the city address.
     */
    private void handleActionFetchCity(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mContext, Global.VIVA_LOCALE);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String cityName = "", countryName = "";
            for (Address address : addresses) {
                cityName = address.getLocality() + "%20" + address.getAdminArea();
                countryName = address.getCountryCode() + "%20" + address.getPostalCode();
            }
            //String[] temp = addresses.get(0).getAddressLine(1).split(Pattern.quote(","));
            //cityName = temp[0].replaceAll(Pattern.quote(" "), Pattern.quote("")) + temp[1].replaceAll(Pattern.quote(" "), Pattern.quote(""));
            //String countryName = addresses.get(0).getAddressLine(2).replaceAll(Pattern.quote(" "), Pattern.quote(""));
            Log.i(Global.TAG, "City Name: " + cityName + "; Country: " + countryName);


            String response = "";
            try {
                String urlString = String.format(OPEN_WEATHER_MAP_API_LAT_LONG, "lat=" + latitude + "&" + "lon=" + longitude + "&cnt=1");
                URL url = new URL(urlString);
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("x-api-key",
                        mContext.getString(R.string.open_weather_maps_app_id));

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuilder json = new StringBuilder(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject data = new JSONObject(json.toString());

                // This value will be 404 if the request was not
                // successful
                if(data.getInt("cod") != 200){
                    Log.i(Global.TAG, "Some error occurred.");
                }

                response = data.toString();
                Log.i(Global.TAG, "Weather: " + response);
                //VivaLibraryPreferenceHelper.saveIrisWeatherInfo(mContext, response);

                if (data.has("list")) {
                    boolean found = false;
                    JSONArray weatherList = data.getJSONArray("list");
                    for (int index = 0; index < weatherList.length(); index++) {
                        JSONObject jsonObject = weatherList.getJSONObject(index);
                        if (jsonObject.has("name")) {
                            String temperature = jsonObject.getJSONObject("main").getString("temp");
                            String windSpeed = ((int) jsonObject.getJSONObject("wind").getDouble("speed")) + "";
                            //String weather = jsonObject.getJSONObject("weather").getString("main");
                            JSONObject weatherObject = jsonObject.getJSONArray("weather").getJSONObject(0);
                            String weather = weatherObject.getString("description");
                            // TODO Save the weather information for the current city.
                            String vivaSay = weather + " at " + temperature
                                    + " degree Celcius in " + cityName + ". Wind spped is " + windSpeed
                                    + " miles per hour.";
                            VivaLibraryPreferenceHelper.saveIrisWeatherInfo(mContext, vivaSay);
                            VivaLibraryPreferenceHelper.setVivaCurrentTemp(mContext, Float.parseFloat(temperature));
                            VivaLibraryPreferenceHelper.setVivaCurrentWindSpeed(mContext, Float.parseFloat(windSpeed));
                            VivaLibraryPreferenceHelper.setVivaCurrentWeatherState(mContext, weather);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        startActionCityWeather(mContext, cityName + "," + countryName);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            //startActionCityWeather(mContext, cityName + "," + countryName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
