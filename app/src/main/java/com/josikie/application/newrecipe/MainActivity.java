package com.josikie.application.newrecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    // Link data
    private static final String urlJSON = "https://content.guardianapis.com/search?page-size=20&q=recipe&api-key=564f438f-3319-42df-a256-87bb11a9250b";
    private TextView emptyStatesLayout;
    private RecipeAdapter recipeAdapter;
    private ProgressBar progressBar;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle(R.string.app_name);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.toolBarColor));
        Log.i(LOG_TAG, "TEST : MainActivity is Calles");

        progressBar = findViewById(R.id.progressBar);

        ListView recipeListView = findViewById(R.id.listItem);

        emptyStatesLayout = findViewById(R.id.empty_text_for_layout);

        recipeListView.setEmptyView(emptyStatesLayout);

        recipeAdapter = new RecipeAdapter(this, new ArrayList<Recipe>());

        recipeListView.setAdapter(recipeAdapter);

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = recipeAdapter.getItem(i);

                Uri uri = Uri.parse(recipe.getLink());

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);
            }
        });

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1, null, this);
        Log.i(LOG_TAG, "TEST : initLoader is called");



    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(LOG_TAG, "TEST : onCreateLoader method is calles");

        //create new loader for the given url
        return new RecipeLoader(this, urlJSON);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
        Log.i(LOG_TAG, "TEST : onLoadFinished method is called");
        recipeAdapter.clear();
        progressBar.setVisibility(View.GONE);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (data != null && !data.isEmpty()){
            recipeAdapter.addAll(data);
        }else if(networkInfo == null || !networkInfo.isConnected()){
            progressBar.setVisibility(View.GONE);
            emptyStatesLayout.setText(R.string.noNetwork);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {
        Log.i(LOG_TAG, "TEST : onLoadReset method is called");
        recipeAdapter.clear();
    }
}
