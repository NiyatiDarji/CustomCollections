//Displays the custom collection list
package com.niyati.customcollections;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class CustomCollectionsList extends AppCompatActivity implements CollectionRepository.CollectionCallBack{

    private ListView collection_listView;
    private CollectionAdapter collectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_collections_list);
        collection_listView = findViewById(R.id.collection_list_view);

        new CollectionRepository(this).getCollection(this);

        //On clicking an item CollectionDetailsPage will be invoked displaying a list of products with their details
        // of the selected collection
        collection_listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomCollection customCollection = collectionAdapter.getItem(position);
                Intent intent = new Intent(CustomCollectionsList.this, CollectionDetailsPage.class);
                intent.putExtra("collection_id", customCollection.getId());
                intent.putExtra("collection_title", customCollection.getTitle());
                intent.putExtra("collection_image", customCollection.getImage().getSrc());
                intent.putExtra("collection_body_html", customCollection.getBody_html());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "Could not retrieve collection list", Toast.LENGTH_LONG).show();
    }

    //invokes the adapter class and populate list view
    @Override
    public void onSuccess(final List<CustomCollection> customCollection) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                collectionAdapter = new CollectionAdapter(CustomCollectionsList.this,0, customCollection);
                collection_listView.setAdapter(collectionAdapter);
            }
        });
    }

    //Adapter class to populate the customCollection
    static class CollectionAdapter extends ArrayAdapter<CustomCollection>{
        public CollectionAdapter(@NonNull Context context, int resource, @NonNull List<CustomCollection> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            CustomCollection customCollection = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
            }
            TextView textView = (TextView)convertView;
            textView.setText(customCollection.getTitle());
            return  textView;
        }
    }
}
