package com.companywesbite.myucdquiz;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


/***
 *
 * Team: Flashcards Pro
 * Date: 12/09/2018
 * Name: AskFragment
 * Functionality: It is the third fragment of the main activity.
 *                It contains a search bar that allows user to ask short "ACADEMIC" questions.
 *
 */



public class AskFragment extends Fragment {


    /**
     * The UI Elements used..
     */
    private View mainView;
    private EditText searchBarUIElement;
    private Button searchButtonUI;
    private TextView searchResultUI;
    private ProgressBar loadingSpinner; // Used when info is being fetched.


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.activity_ask_fragment,container,false);

        searchBarUIElement = (EditText) mainView.findViewById(R.id.question);
        searchButtonUI = (Button) mainView.findViewById(R.id.askButton);
        searchResultUI = (TextView) mainView.findViewById(R.id.searchResult);
        loadingSpinner = (ProgressBar) mainView.findViewById(R.id.progressBar1);


        // perform the search operation
        searchButtonUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               performSearch(searchBarUIElement.getText().toString().trim());
            }
        });


        // make spinner invisible until search is started
        loadingSpinner.setVisibility(View.GONE);




        return mainView;
    }


    /***
     * This tesk fetched info from wolfram webiste using their answer API
     */
    public class FetchInfoTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {

            // if there is no info to look up then do not run
            if (params.length == 0) {
                return null;
            }

            // I declared these here so I could call them again in the final block

            HttpURLConnection urlConn = null;
            BufferedReader buffReader = null;

            // This is what the API returns
            String responseString = "";


            try {

                // This URL contains the API key
                URL url = new URL("http://api.wolframalpha.com/v2/query?appid=WH8246-T65HKAUE66&input="+params[0]+"&includepodid=Result&format=plaintext");


                // Create the request to WOLFRAMALPA WEBSITE
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.connect();

                // This reads the input stream
                InputStream inputStream = urlConn.getInputStream();
                responseString = processIncomingStream(inputStream);


            }
            catch (IOException e) {

                // The operation failed so we do not parse results
                return null;
            }

            finally {

                // clean up and close everything
                if (urlConn != null) {
                    urlConn.disconnect();
                }
                if (buffReader != null) {
                    try {
                        buffReader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            return responseString.split(" ");
        }


        private String  processIncomingStream(InputStream in) {

            BufferedReader bufferedReader = null;
            StringBuffer stringData = new StringBuffer("");

            try {

                bufferedReader  = new BufferedReader(new InputStreamReader(in));

                String oneLine = "";

                while ((oneLine = bufferedReader .readLine()) != null) {
                    stringData.append(oneLine);
                }
            } catch (IOException e) {

                // Problem happened we cannot do anything now..

            } finally {
                if (bufferedReader  != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return stringData.toString();
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


                // using pattern macther to remove all other junk
                final Pattern pattern = Pattern.compile("<plaintext>(.+?)</plaintext>");
                final Matcher matcher = pattern.matcher(output);

                // the search is complete
                loadingSpinner.setVisibility(View.GONE);
                if(!matcher.find())
                {
                    // nothing got returned.
                    searchResultUI.setText("No Result found!");
                    return;
                }

                searchResultUI.setText(matcher.group(1));

            }
        }
    }


    private void performSearch(String userQuery)
    {

        if(!isOnline())
        {
            Toast.makeText(getContext(),"Internet not available!",Toast.LENGTH_LONG).show();
            return;
        }

        // clear the results...
        searchResultUI.setText("");

        // check if the user has enough values..
        if(userQuery.trim().length() < 3)
        {
            Toast.makeText(getContext(),"Query too short!",Toast.LENGTH_LONG).show();
            return;
        }

        // the search has started
        loadingSpinner.setVisibility(View.VISIBLE);

        userQuery.replace(" ","%20");
        userQuery = userQuery.trim();

        FetchInfoTask task = new FetchInfoTask();
        task.execute(userQuery);
    }


    /**
     * Checking if the user is connected to the internet
     * Actually it only checks if a network if available
     * If the user is connected to wifi and no internet connection
     * is available then it will still return a true value
     * @return
     */
    private boolean isOnline() {
        ConnectivityManager connManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}