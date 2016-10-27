package de.hhbk.app.b_login_v0;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPasswort;
    private Button buttonLogin;
    private static final String URL_DB_VERBINDUNG = "http://hhbk.bplaced.net/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
//    private static final String TAG_NACHNAME = "nachname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_login__v0);
        editTextUsername = (EditText) findViewById(R.id.editTextBenutzername);
        editTextPasswort = (EditText) findViewById(R.id.editTextPasswort);

        // insert credentials for testing

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new ClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_login__v0, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ClickListener implements View.OnClickListener {

        public void onClick(View v) {

            String username = editTextUsername.getText().toString();
            String passwort = editTextPasswort.getText().toString();

            boolean localLogin = false;
            // locallogin anhand der CHeckbox auslesen lassen
            localLogin = ((CheckBox) findViewById(R.id.checkbox_local_login)).isChecked();

            if (localLogin){
                if (username.equals("Heinrich") && passwort.equals("Hertz")) {
                    Intent mainIntent = new Intent(v.getContext(), LoggedInActivity.class);
                    mainIntent.putExtra("username", username);
                    startActivity(mainIntent);
                } else {
                    editTextUsername.setText("");
                    editTextPasswort.setText("");
                    Toast toastAusgabe = Toast.makeText(v.getContext(), "Bitte verwenden Sie "
                            + "Benutzer:Heinrich und Passwort:Hertz", Toast.LENGTH_LONG);
                    toastAusgabe.show();
                }
            }else {
                new LoginService(getApplicationContext(),MainActivity.this).execute(username, passwort);
            }


        }
    }

    class LoginService extends AsyncTask<String, Void, JSONObject> {

        private ProgressDialog pDialog;
        private Context applicationContext;
        private Activity activity;
        private String[] params;

        public LoginService(Context applicationContext, Activity activity) {
            this.applicationContext = applicationContext;
            this.activity = activity;
        }

        /* Dialog Verbindungsaufbau */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Aufbau der HTTP Verbindung...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... logindaten) {
            HttpURLConnection connection = null;
            JSONObject jObj = null;
            String json = "";
            this.params = logindaten;

            try {
                URL _url = new URL(URL_DB_VERBINDUNG);
                connection = (HttpURLConnection) _url.openConnection();
                // Verbindung konfigurieren
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                // Übergabeparameter
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", logindaten[0])
                        .appendQueryParameter("passwort", logindaten[1]);
                String query = builder.build().getEncodedQuery();

                // Verbindung herstellen
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);

                writer.flush();
                writer.close();
                os.close();
                connection.connect();

                // Antwort empfangen
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    try {
                        return new JSONObject(result.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            int success = 0;
            try {
                if(json != null && json.has(TAG_SUCCESS)) {
                    success = json.getInt(TAG_SUCCESS);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (success == 1) {
                String username = params[0];
                String password = params[1];
                Intent mainIntent = new Intent(applicationContext, LoggedInActivity.class);
                mainIntent.putExtra("username", username);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(mainIntent);
            }
            try {
                if(json!=null && json.has(TAG_MESSAGE)){
                    Toast.makeText(MainActivity.this, json.getString(TAG_MESSAGE),
                    Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
        }
    }

}
