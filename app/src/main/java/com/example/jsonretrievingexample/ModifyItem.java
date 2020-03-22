package com.example.jsonretrievingexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ModifyItem extends AppCompatActivity {
    EditText ev_name;
    EditText ev_country;
    EditText ev_city;
    String id;
    String url="https://demonuts.com/Demonuts/JsonTest/Tennis/json_parsing.php";
    Button changePic;
    ImageView targetimage;
    //Assuming Update and delete will be url/id
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_item);
        settingWidgetsofPage();
        queue = Volley.newRequestQueue(this);
        changePic = (Button) findViewById(R.id.changePic_btn);
        targetimage = (ImageView)findViewById(R.id.lv);
        changePic.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetimage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void settingWidgetsofPage(){
        Intent modify= getIntent();
        String name = modify.getStringExtra("name");
        String city = modify.getStringExtra("city");
        String country = modify.getStringExtra("country");
        id=modify.getStringExtra("id");
        ev_name = (EditText) findViewById(R.id.name);
        ev_country = (EditText) findViewById(R.id.country);
        ev_city = (EditText) findViewById(R.id.city);
        ev_name.setText(name);
        ev_city.setText(city);
        ev_country.setText(country);
        ImageView im = (ImageView) findViewById(R.id.lv);
        Picasso.get().load(modify.getStringExtra("picture")).into(im);
    }

    public void Modify(View view) {
        if (ev_name.getText().length()==0 || ev_city.getText().length()==0 || ev_country.getText().length()==0){
            Toast.makeText(getApplicationContext(), "Please Fill all Fields", Toast.LENGTH_SHORT).show();
        } else {
            updateItem();
        }
    }



    public void updateItem(){
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url+"/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getApplicationContext(), "error updating", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                //or try with this:
                //headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", ev_name.getText().toString());
                params.put("city", ev_city.getText().toString());
                params.put("country",ev_country.getText().toString());
                return params;
            }
        };

        queue.add(putRequest);
        GotoMain();
    }

    public void GotoMain(){
        Intent main= new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
    }
    public void Delete(View view) {
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url+"/"+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        Toast.makeText(getApplicationContext(), "error deleting", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(deleteRequest);
        GotoMain();
    }

    }


