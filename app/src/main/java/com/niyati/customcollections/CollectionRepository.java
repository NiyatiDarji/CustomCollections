//parse various JSON files from the urls and then return appropriate JSON objects or JSON arrays
package com.niyati.customcollections;

import android.content.Context;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CollectionRepository {
    private final Context context;

    public CollectionRepository(Context context) {
        this.context = context;
    }

    //called by CustomCollectionList to populate the list view with different collections
    //returns a list of customCollection
    public void getCollection(final CollectionCallBack collectionCallBack){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .build();
            client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                collectionCallBack.onFailure();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    collectionCallBack.onFailure();
                }
                JSONObject obj;
                JSONArray arr = null;
                try {
                    obj = new JSONObject(response.body().string());
                    arr = obj.getJSONArray("custom_collections");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonarr= arr.toString();
                Moshi moshi = new Moshi.Builder().build();
                Type type = Types.newParameterizedType(List.class,CustomCollection.class);
                JsonAdapter<List<CustomCollection>> jsonAdapter = moshi.adapter(type);
                List<CustomCollection> customCollection = jsonAdapter.fromJson(jsonarr);
                collectionCallBack.onSuccess(customCollection);
            }
        });
    }
    public interface CollectionCallBack {
        void onFailure();
        void onSuccess(List<CustomCollection> customCollection);
    }

    //called by CollectionDetailsPage to get the product ids from Collects file for the selected collection
    //using the collection_id of the selected collection
    //onSuccess() returns a string of product_id separated by coma but in the format "null,123456,7890...."
    public void getCollects(final CollectsCallBack collectsCallBack,String collection_id){

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://shopicruit.myshopify.com/admin/collects.json?collection_id="+collection_id+"&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .build();
        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onFailure(Request request, IOException e) {
                collectsCallBack.onFailure(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                JSONObject obj;
                JSONArray arr;
                String product_id = null;

                if (!response.isSuccessful()) {
                    collectsCallBack.onFailure(0);
                }

                try {
                    obj = new JSONObject(response.body().string());
                    arr = (JSONArray) obj.get("collects");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject row = arr.getJSONObject(i);
                        product_id = product_id+","+row.getString("product_id");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                collectsCallBack.onSuccess(product_id);
            }
        });
    }
    public interface CollectsCallBack {
        void onFailure(int check);
        void onSuccess(String product_id);
    }

    //called by CollectionDetailsPage to get the product details from Products file for the selected collection
    //using the product_id string obtained from above function
    //onSuccess() returns a list of ProductQuantityClass
    public void getProducts(final ProductsCallBack productsCallBack, String product_id, final String collection_title, final String collection_image){

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://shopicruit.myshopify.com/admin/products.json?ids="+product_id+"&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Request request, IOException e) {
                productsCallBack.onFailure(1);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                JSONObject pobj;
                JSONArray parr;
                JSONArray varr;
                String product_title;
                String inventory_quantity;
                List<ProductQuantityClass> productQuantity = new ArrayList<>();

                if (!response.isSuccessful()) {
                    productsCallBack.onFailure(1);
                }
                try {
                    pobj = new JSONObject(response.body().string());
                    parr = (JSONArray) pobj.get("products");
                    for (int i = 0; i < parr.length(); i++) {
                        JSONObject prow = parr.getJSONObject(i);
                        product_title = prow.getString("title");
                        varr = prow.getJSONArray("variants");
                        int q =0;
                        for (int j = 0; j < varr.length(); j++) {
                            JSONObject vrow = varr.getJSONObject(j);
                            q+=Integer.parseInt(vrow.getString("inventory_quantity"));
                        }
                        inventory_quantity = Integer.toString(q);
                        //add the product details which needs to be displayed in the list of ProductQuantityClass object: productQuantity
                        productQuantity.add(new ProductQuantityClass(product_title, inventory_quantity, collection_title,collection_image));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                productsCallBack.onSuccess(productQuantity);
            }
        });
    }
    public interface ProductsCallBack {
        void onFailure(int check);
        void onSuccess(List<ProductQuantityClass> productQuantity);
    }
}
