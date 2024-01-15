import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//External API used to retrieve weather data
public class App {
    public static JSONObject getWeatherData(String cityName, String provinceName, String countryName){
        //Retrieving location coordinates using gelocation API
        JSONObject location = getLocationData(cityName, provinceName, countryName);

        //If the city could not be found
        if(location == null){
            return null;
        }

        //Extract latitude and longitude data

        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        //Build API request URL with location coords.
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto";
        try{
            //Call API and get response
            HttpURLConnection conn = fetchAPIResponse(urlString);

            //Check for response status
            //200 means the connection was successful
            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not establish a connection with API");
                return null;
            }

            //Store resulting json data
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());

            while(scanner.hasNext()){
                //Read and store into the string builder
                resultJson.append(scanner.nextLine());
            }

            //Close the scanner
            scanner.close();

            //Close the connection
            conn.disconnect();

            //Parse data
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj  = (JSONObject) parser.parse(String.valueOf(resultJson));

            //Retrieve hourly data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            //Getting data for the current hour
            //we need to get the index of the current hour
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentItem(time);

            //Get the temperature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            //Get weather code
            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));

            //Get humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            //Get humidity
            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windSpeed = (double) windSpeedData.get(index);

            //Get city
            String finalCityName = (String) location.get("name");

            //Get Province/State
            String finalProvinceName = (String) location.get("admin1");

            //Get country Name
            String finalCountryName = (String) location.get("country");

            //Build the weather json data object that we are going to access in our frontend
            JSONObject weatherData = new JSONObject();

            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("wind_speed", windSpeed);
            weatherData.put("city", finalCityName);
            weatherData.put("province", finalProvinceName);
            weatherData.put("country", finalCountryName);

            return weatherData;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //Retrieve geo. coordinates for provided location
    public static JSONObject getLocationData(String cityName, String provinceName, String countryName){
        cityName = cityName.replaceAll(" ", "+");

        //Building API URL
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + cityName + "&count=10&language=en&format=json";

        try{
            //Call API and attempt to get a response
            HttpURLConnection connection = fetchAPIResponse(urlString);

            //Check response status
            //200 means connection was succesful
            if(connection.getResponseCode() != 200){
                System.out.println("Error: Could not establish a connection with API");
                return null;
            }
            else{
                //Store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                //Read and store json data
                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }

                //close scanner
                scanner.close();

                //Close url connection
                connection.disconnect();

                //parsing json string into a json object
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                //Get the list of location data that the API generated based on the location name
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");

                if(locationData == null){
                    return null;
                }

                int i = 0;
                JSONObject location = null;


                if(!provinceName.isEmpty() || !countryName.isEmpty()){
                    boolean isFound = false;
                    for(i = 0; i < locationData.size(); i++){
                        JSONObject temp = (JSONObject) locationData.get(i);

                        if(!provinceName.isEmpty() && countryName.isEmpty()){
                            if(provinceName.equalsIgnoreCase((String) temp.get("admin1"))){
                                isFound = true;
                                break;
                            }
                        }
                        else if(provinceName.isEmpty() && !countryName.isEmpty()){
                            if(countryName.equalsIgnoreCase((String) temp.get("country"))){
                                isFound = true;
                                break;
                            }
                        }
                        else{
                            if(provinceName.equalsIgnoreCase((String) temp.get("admin1"))){
                                if(countryName.equalsIgnoreCase((String) temp.get("country"))){
                                    isFound = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(isFound){
                        location = (JSONObject) locationData.get(i);
                    }
                }
                else{
                    location = (JSONObject) locationData.get(0);
                }


                return location;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //Couldn't find location
        return null;
    }

    private static HttpURLConnection fetchAPIResponse(String urlString){
        try{
            //Attempt to establish connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //Setting request method
            connection.setRequestMethod("GET");

            //Connect to API
            connection.connect();
            return connection;
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //Connection could not be established
        return null;
    }

    private static int findIndexOfCurrentItem(JSONArray timeList){
        String currentTime = getCurrentTime();

        for(int i = 0; i < timeList.size(); i++){
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }

        return 0;
    }

    public static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        //format date to match the API way
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        //format and print the current date and time
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    //Making the weather code redeable
    private static String convertWeatherCode(long weatherCode){
        String weatherCondition = "";

        if(weatherCode == 0L){
            weatherCondition = "Clear";
        }
        else if(weatherCode <= 3L){
            weatherCondition = "Cloudy";
        }
        else if((weatherCode >= 51L && weatherCode <= 67L)
                || (weatherCode >=80L && weatherCode <=99L)){
            weatherCondition = "Rain";
        }
        else if(weatherCode >=71L && weatherCode <= 77L){
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}
