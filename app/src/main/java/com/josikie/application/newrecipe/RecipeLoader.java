package com.josikie.application.newrecipe;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final String LOG_TAG = RecipeLoader.class.getName();

    String url;

    public RecipeLoader(@NonNull Context context, String urls){
        super(context);
        url = urls;
    }

    @Override
    protected void onStartLoading(){
        Log.i(LOG_TAG, "TEST : onStartLoading is called");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Recipe> loadInBackground(){
        Log.i(LOG_TAG, "TEST : loadInBackground method is calling");
        if (url == null){
            return null;
        }
        List<Recipe> recipeList = QueryUtils.fetchNewRecipeData(url);
        return recipeList;
    }
}
