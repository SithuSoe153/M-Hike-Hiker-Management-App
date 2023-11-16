package com.uog.soemhike.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.uog.soemhike.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GPTActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    TextView tv_Question, tv_Response;
    TextInputEditText ed_Query;
    private ImageButton btnSpeak, btnEnter;

    String url = "https://api.openai.com/v1/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpt);

        tv_Question = findViewById(R.id.tv_Question);
        tv_Response = findViewById(R.id.tv_Response);
        btnSpeak = findViewById(R.id.btnSpeak);
        btnEnter = findViewById(R.id.btnEnter);

        ed_Query = findViewById(R.id.ed_Query);

        tv_Response.setMovementMethod(new ScrollingMovementMethod());

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    ed_Query.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_Query.getText().toString().length() > 0) {
                    tv_Response.setText("Please wait...");
                    getResponse(ed_Query.getText().toString());
                } else {
                    Toast.makeText(GPTActivity.this, "Please enter your query ..", Toast.LENGTH_SHORT).show();

                }
            }
        });
        ed_Query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                Log.d("EditText", "Editor action pressed: " + i);


                if (i == EditorInfo.IME_ACTION_SEND  || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    tv_Response.setText("Please wait...");
                    Log.d("EditText", "Enter pressed. Text: " + ed_Query.getText().toString());

                    if (ed_Query.getText().toString().length() > 0) {
                        getResponse(ed_Query.getText().toString());
                    } else {
                        Toast.makeText(GPTActivity.this, "Please enter your query ..", Toast.LENGTH_SHORT).show();

                    }
                }

                return false;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ed_Query.setText(text.get(0));
                }
                break;
        }
    }


    private void getResponse(String query) {
        Log.d("test", "onCreate: ");

// setting text on for question on below line.
        tv_Question.setText(query);
        ed_Query.setText("");

        // Add a delay between requests to avoid hitting rate limits
        try {
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


// creating a queue for request queue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
// creating a json object on below line.
        JSONObject jsonObject = new JSONObject();
// adding params to json object.
        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", query);
            jsonObject.put("temperature", 0.0);  // Use 0.0 instead of 0 for the temperature
            jsonObject.put("max_tokens", 100);
            jsonObject.put("top_p", 1.0);  // Use 1.0 instead of 1 for top_p
            jsonObject.put("frequency_penalty", 0.0);
            jsonObject.put("presence_penalty", 0.0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String responseMsg =
                            response.getJSONArray("choices").getJSONObject(0).getString("text");
                    tv_Response.setText(responseMsg);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAGAPI", "Error is :" + error.getMessage() + "\n" + error);

                if (error.networkResponse != null) {
                    Log.e("TAGAPI", "Status Code: " + error.networkResponse.statusCode);
                    Log.e("TAGAPI", "Response Data: " + new String(error.networkResponse.data));
                }

            }
        }) {


//Passing some request headers

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                //adding headers on below line.
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer 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");

                return params;
            }
        };

        // on below line adding retry policy for our request.
        postRequest.setRetryPolicy(new RetryPolicy() {


            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(postRequest);
    }
}