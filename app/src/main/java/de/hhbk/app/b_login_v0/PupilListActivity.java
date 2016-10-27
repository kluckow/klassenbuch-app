package de.hhbk.app.b_login_v0;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class PupilListActivity extends AppCompatActivity {

    private String klasse;

    private List<Pupil> pupilList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_list_activity);
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logged_in_activity, menu);
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
        switch (id) {
            case R.id.action_settings:
                // do nothing for now
                return true;
            case R.id.action_pupil_list:
                // TODO: other action id
                Toast toastAusgabe = Toast.makeText(this, "Aktualisiere Liste...", Toast.LENGTH_SHORT);
                toastAusgabe.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initData() {

        ListView listView = (ListView) findViewById(R.id.pupil_list);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_pupil_item, getPupilList());
        listView.setAdapter(adapter);
    }

    private List<String> getPupilList() {

        String json = loadJSONFromAssets();
        List<Pupil> pupilList = convertJSON2PupilList(json);

        List<String> pupilStringList = new ArrayList();
        for (Pupil pupil: pupilList) {
            pupilStringList.add(pupil.toString());
        }
        return pupilStringList;

    }

    public List<Pupil> convertJSON2PupilList(String json) {

        List<Pupil> pupilList = new ArrayList();
        try {
            JSONObject jsonData = new JSONObject(loadJSONFromAssets());

            // set klasse
            JSONObject jsonKlasse = jsonData.getJSONObject("klasse");
            klasse = jsonKlasse.getString("identifier");
            updateKlasse(klasse);
            // set pupil list
            JSONArray jsonPupilArray = jsonKlasse.getJSONArray("pupils");
            int id = 0;
            String lastname = "";
            String firstname = "";
            for (int i = 0; i < jsonPupilArray.length(); i++) {

                JSONObject jsonPupil = jsonPupilArray.getJSONObject(i);
                if (jsonPupil.has("id")) {
                    id = jsonPupil.getInt("id");
                } else {
                    id = 0;
                }
                if (jsonPupil.has("lastname")) {
                    lastname = jsonPupil.getString("lastname");
                } else {
                    lastname = "kein Nachname";
                }
                if (jsonPupil.has("firstname")) {
                    firstname = jsonPupil.getString("firstname");
                } else {
                    firstname = "kein Vorname";
                }
                pupilList.add(new Pupil(id, lastname, firstname, klasse));
            }

        } catch (JSONException e) {
            // TODO: log somehow
            e.printStackTrace();
        }
        return pupilList;
    }

    private void updateKlasse(String klasse) {
        TextView textViewKlasse = (TextView) findViewById(R.id.TextViewKlasseValue);
        this.klasse = klasse;
        textViewKlasse.setText(this.klasse);
    }

    public String loadJSONFromAssets() {
        String json = null;
        try {

            InputStream is = getAssets().open("pupil_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            // TODO: log somehow
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
