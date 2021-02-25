package com.josikie.application.newrecipe;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to related to requesting and receiving new recipe from the guardian
 */
public final class QueryUtils {

    private static String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query dataset the guardian dan kembalikan daftar objek new recipe (@link Recipe)
     */
    public static List<Recipe> fetchNewRecipeData(String requestUrl){
        Log.i(LOG_TAG, "TEST : fetchNewRecipeData method is called");

        // buat objek URL untuk membuat String menjadi URL yang bisa diakses
        URL url = createUrl(requestUrl);

        // String untuk menerima kembali response dari json
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request " + e);
        }
        //Ekstrak field yang relevan dari respon JSON dan buatlah daftar{@link Recipe}
        List<Recipe> recipes = extractRecipes(jsonResponse);


        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        // Kembalikan daftar {@link Recipe}
        return recipes;


    }

    /**
     * Kembalikan objek URL baru dari String URL yang ada.
     */

    private static URL createUrl(String requestURL){
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL " + e);
            return null;
        }

        return url;
    }

    /**
     * Buat HTTP request ke URL yang ada dan kembalikan sebuah String sebagai respon.
     */
    private static String makeHttpRequest(URL urll) throws  IOException {
        String jsonResponse = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        //if the URL is null, then return early
        if (urll == null){
            return jsonResponse;
        }

        try {
            httpURLConnection = (HttpURLConnection) urll.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            //Jika request berhasil (kode respons 200),
            // maka bca input stream dan lakukan parse atas respons.

            if (httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else if (httpURLConnection.getResponseCode() != 200){
                Log.e(LOG_TAG, "Error Exception code : " + httpURLConnection.getResponseCode());
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }

            if (inputStream != null){
                // menutup input stream memunculkan IOException, karena itu lambang method (method signature)
                // makeHttpRequest(URL url) harus menyatakan IOException dapat dimunculkan.
                inputStream.close();;
            }
        }

        return jsonResponse;
    }

    /**
     * Ubah {@link InputStream} menjadi String yang mengandung
     * seluruh respon JSON dari server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    /**
     *Kembalikan daftar objek {@link Recipe} yang sudah terbuat dari
     * parsing respon JSON yang sudah ada.
     */
    public static List<Recipe> extractRecipes(String newRecipe){
        //Jika String JSON kosong atau null, segera kembalikan dengan null
        if (TextUtils.isEmpty(newRecipe)){
            return null;
        }


        //Buat ArrayList kosong yang dapat dimasukkan daftar resep baru
        List<Recipe> recipeList = new ArrayList<Recipe>();

        /*
        Coba parse string respons JSON. Jika ada masalah dengan pemformatan JSOn
        objek exception JSONException akan dimunculkkan
        Tangkap perkecualiannya agar aplikasi tidak mengalami crash, dan cetak pesan error-nya ke log
         */

        try {
            //Buat JSONObject dari String respons JSON
            JSONObject jsonObject = new JSONObject(newRecipe);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++){
                JSONObject result = results.getJSONObject(i);
                String sectionName = result.getString("sectionName");
                String webPublicationDate = result.getString("webPublicationDate");
                String webTitle = result.getString("webTitle");
                String link = result.getString("webUrl");

                // Buat objek {@link recipe} baru dengan webTitle, webPublicationDate, "", sectionName, link dari response JSON.Beta
                Recipe recipe = new Recipe(webTitle, webPublicationDate , "-" ,  sectionName, link );

                //tambahkan {@link Recipe} baru ke daftar Recipe
                recipeList.add(recipe);
            }

        }catch (JSONException e){
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtlis", "Problem parsing the recipe JSON results" + e);

        }

        // Return the list of recipe
        return recipeList;
    }
}
