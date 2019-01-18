//Main activity : displays a button
package com.niyati.customcollections;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getCollections_button = findViewById(R.id.getCollections_button);

        //clicking on the button redirects to CustomCollectionsList activity
        getCollections_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomCollectionsList.class);
                startActivity(intent);
            }
        });

    }
}
