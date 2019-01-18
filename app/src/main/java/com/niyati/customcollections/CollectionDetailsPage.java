//Display collection details like product name, inventory quantity, collection title and collection image
package com.niyati.customcollections;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class CollectionDetailsPage extends AppCompatActivity implements CollectionRepository.CollectsCallBack,CollectionRepository.ProductsCallBack {

    private String collection_title;
    private String collection_image;
    private String collection_body_html;
    private ProductAdapter productAdapter;
    private ListView product_listView;
    private TextView collection_title_cardview_text;
    private TextView collection_body_html_cardview_text;
    private ImageView collection_image_cardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details_page);

        product_listView = findViewById(R.id.product_listView);
        collection_title_cardview_text = findViewById(R.id.collection_title_cardview_text);
        collection_body_html_cardview_text = findViewById(R.id.collection_body_html_cardview_text);
        collection_image_cardview = findViewById(R.id.collection_image_cardview);

        //get the intent from CustomCollectionList page i.e. of selected collection
        String collection_id = getIntent().getStringExtra("collection_id");
        collection_title = getIntent().getStringExtra("collection_title");
        collection_body_html = getIntent().getStringExtra("collection_body_html");
        collection_image = getIntent().getStringExtra("collection_image");

        //set text and image in the cardview
        collection_title_cardview_text.setText(collection_title);
        collection_body_html_cardview_text.setText(collection_body_html);
        new DownloadImageTask(collection_image_cardview).execute(collection_image);

        //get products ids from collects file for a selected collection
        new CollectionRepository(this).getCollects(this, collection_id);
    }

    @Override
    public void onFailure(int check) {
        if (check == 0) {
            Toast.makeText(this, "Could not retrieve collects ", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "Could not retrieve products ", Toast.LENGTH_LONG).show();
    }

    //after getting the products ids from collects, modify the string according to the url requirement
    @Override
    public void onSuccess(String product_id) {
        product_id = product_id.substring(5);
        // get products details from collects file for a selected collection
        new CollectionRepository(this).getProducts(this, product_id, collection_title, collection_image);

    }

    //populate the list view with products details
    @Override
    public void onSuccess(final List<ProductQuantityClass> productQuantity) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                productAdapter = new ProductAdapter(CollectionDetailsPage.this, 0, productQuantity);
                product_listView.setAdapter(productAdapter);
            }
        });
    }

    //Adapter to get the products details from the ProductQuantityClass class and set it in the list view
    //the list view is according to the layout file custom_row_view.xml
    //the image is first downloaded from the url and then set in the imageview
    static class ProductAdapter extends ArrayAdapter<ProductQuantityClass> {

        public ProductAdapter(@NonNull Context context, int resource, @NonNull List<ProductQuantityClass> objects) {
            super(context, 0, objects);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ProductQuantityClass productQuantityClass = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_row_view, parent, false);
            }
            TextView title_textview = convertView.findViewById(R.id.title_textview);
            TextView inventory_quantity_textview = convertView.findViewById(R.id.inventory_quantity_textview);
            TextView collection_title_textview = convertView.findViewById(R.id.collection_title_textview);
            ImageView collection_image_imageview = convertView.findViewById(R.id.collection_image_imageview);

            title_textview.setText("Product: "+productQuantityClass.getTitle());
            inventory_quantity_textview.setText("Quantity: "+productQuantityClass.getInventory_quantity());
            collection_title_textview.setText("Collection: "+productQuantityClass.getCollection_title());

            new DownloadImageTask(collection_image_imageview).execute(productQuantityClass.getCollection_image());

            return convertView;
        }
    }
}