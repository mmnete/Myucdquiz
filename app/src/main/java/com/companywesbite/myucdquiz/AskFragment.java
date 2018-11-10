package com.companywesbite.myucdquiz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AskFragment extends Fragment {

    private static final String TAG = "AskFragment";


    Context context;
    private View view;

    private EditText searchBar;
    private Button searchButton;
    private TextView searchResult;

    private ProgressBar spinner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_ask_fragment,container,false);

        searchBar = (EditText) view.findViewById(R.id.question);
        searchButton = (Button) view.findViewById(R.id.askButton);
        searchResult = (TextView) view.findViewById(R.id.searchResult);
        spinner = (ProgressBar)view.findViewById(R.id.progressBar1);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               performSearch(searchBar.getText().toString().trim());
            }
        });


        spinner.setVisibility(View.GONE);




        return view;
    }


    public class FetchQueryTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;


            int numDays = 10;

            // You will need to replace this key with your own one. To get a key
            // go to http://openweathermap.org/api and sign up.
            String key = "WH8246-T65HKAUE66";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String FORECAST_BASE_URL =
                        "http://api.wolframalpha.com/v2/query?";
                final String QUERY_PARAM = "appid";
                final String QUERY = "input";


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, key)
                        .appendQueryParameter(QUERY, "population%20of%20france")
                        .build();

                URL url = new URL("http://api.wolframalpha.com/v2/query?appid=WH8246-T65HKAUE66&input="+params[0]+"&includepodid=Result&format=plaintext");


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                forecastJsonStr = readStream(inputStream);

                //forecastJsonStr="{\"city\":{\"id\":7778677,\"name\":\"Dublin City\",\"coord\":{\"lon\":-6.24922,\"lat\":53.355122},\"country\":\"IE\",\"population\":0},\"cod\":\"200\",\"message\":0.0199,\"cnt\":7,\"list\":[{\"dt\":1444564800,\"temp\":{\"day\":11.14,\"min\":10.77,\"max\":12.46,\"night\":12.46,\"eve\":11.96,\"morn\":10.77},\"pressure\":1025.62,\"humidity\":100,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"speed\":4.35,\"deg\":100,\"clouds\":80},{\"dt\":1444651200,\"temp\":{\"day\":12.05,\"min\":11.13,\"max\":12.85,\"night\":12.04,\"eve\":12.63,\"morn\":11.25},\"pressure\":1032.62,\"humidity\":100,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"02d\"}],\"speed\":8.61,\"deg\":358,\"clouds\":8},{\"dt\":1444737600,\"temp\":{\"day\":12.37,\"min\":11.97,\"max\":12.39,\"night\":12.02,\"eve\":12.1,\"morn\":12.24},\"pressure\":1036.05,\"humidity\":100,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"speed\":5.61,\"deg\":50,\"clouds\":64},{\"dt\":1444824000,\"temp\":{\"day\":11.23,\"min\":8.95,\"max\":11.23,\"night\":9.86,\"eve\":10.99,\"morn\":8.95},\"pressure\":1036.52,\"humidity\":0,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":6.35,\"deg\":131,\"clouds\":0},{\"dt\":1444910400,\"temp\":{\"day\":12.47,\"min\":8.78,\"max\":12.47,\"night\":10.21,\"eve\":11.44,\"morn\":8.78},\"pressure\":1036.04,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":5.45,\"deg\":50,\"clouds\":0},{\"dt\":1444996800,\"temp\":{\"day\":12.96,\"min\":9.26,\"max\":12.96,\"night\":9.26,\"eve\":11.74,\"morn\":9.78},\"pressure\":1033.93,\"humidity\":0,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":3.5,\"deg\":7,\"clouds\":2},{\"dt\":1445083200,\"temp\":{\"day\":12.68,\"min\":8.32,\"max\":12.68,\"night\":9.12,\"eve\":11.59,\"morn\":8.32},\"pressure\":1033.53,\"humidity\":0,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":1.86,\"deg\":342,\"clouds\":3}]}";

                Log.d("TAG",forecastJsonStr);

            }
            catch (IOException e) {

                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }

            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            return forecastJsonStr.split(" ");
        }


        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String[] results) {
            //super.onPostExecute(strings);

            if (results != null) {


                // display the results on a string...
                String output = "";
                for(int i = 0; i < results.length;i++)
                {
                    output += results[i] + " ";
                }


                final Pattern pattern = Pattern.compile("<plaintext>(.+?)</plaintext>");
                final Matcher matcher = pattern.matcher(output);

                spinner.setVisibility(View.GONE);
                if(!matcher.find())
                {
                    searchResult.setText("No Result found!");
                    return;
                }

                searchResult.setText(matcher.group(1));

            }
        }
    }


    private void performSearch(String query)
    {
        if(!isNetworkAvailable())
        {
            Toast.makeText(getContext(),"Internet not available!",Toast.LENGTH_LONG).show();
            return;
        }
        searchResult.setText("");
        if(query.trim().length() < 3)
        {
            Toast.makeText(getContext(),"Query too short!",Toast.LENGTH_LONG).show();
            return;
        }

        spinner.setVisibility(View.VISIBLE);
        //Toast.makeText(getContext(),"Searching..",Toast.LENGTH_LONG).show();
        query.replace(" ","%20");
        query = query.trim();

        FetchQueryTask task = new FetchQueryTask();
        task.execute(query);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}