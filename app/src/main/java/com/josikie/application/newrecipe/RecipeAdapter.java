package com.josikie.application.newrecipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> arrayList = new ArrayList<>();

    public RecipeAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
        super(context, 0, recipeArrayList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_recipe, parent, false);
        }

        final Recipe recipe = getItem(position);

        TextView title = view.findViewById(R.id.tv_title);
        title.setText(recipe.getTitle());

        TextView publicationDate = view.findViewById(R.id.tv_publicationDate);
        StringBuilder time = dateFormat(recipe.getDate());
        publicationDate.setText(time);

        TextView author = view.findViewById(R.id.tv_author_name);
        if (author == null){
            author.setText("-");
        }else{
            author.setText(recipe.getAuthor());
        }


        TextView sectionsName = view.findViewById(R.id.sectionName);
        sectionsName.setText(recipe.getSectionsName());

        return view;
    }

    private StringBuilder dateFormat(String date){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++){
            char s = date.charAt(i);
            stringBuilder.append(s);
        }
        return stringBuilder;
    }


}
