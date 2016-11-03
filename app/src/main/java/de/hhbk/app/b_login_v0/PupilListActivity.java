package de.hhbk.app.b_login_v0;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PupilListActivity extends AppCompatActivity {

    private ArrayList<String> schuelerliste = new ArrayList<>();
    private ListView listViewSchueler;
    private Spinner spinner;
    private ArrayAdapter<String> schuelerAdapter;

    private List<String> klassenliste = new ArrayList<>();
    private ArrayAdapter<String> klassenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_list_activity);

        spinner = (Spinner) findViewById(R.id.spinner_class);
        listViewSchueler = (ListView) findViewById(R.id.pupil_list);
        getJSON();
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

    // TODO: use similar method for refreshing list
//    private void updateKlasse(String klasse) {
//        TextView textViewKlasse = (TextView) findViewById(R.id.TextViewKlasseValue);
//        this.klasse = klasse;
//        textViewKlasse.setText(this.klasse);
//    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PupilListActivity.this, "Fetching Data", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                schuelerAdapter = new ArrayAdapter(PupilListActivity.this, R.layout.activity_pupil_item, schuelerliste);
                listViewSchueler.setAdapter(schuelerAdapter);
                klassenAdapter = new ArrayAdapter(PupilListActivity.this, R.layout.activity_pupil_item, klassenliste);
                spinner.setAdapter(klassenAdapter);

                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://hhbk.bplaced.net/alleSchueler_v3.php");

                try {
                    JSONObject schueler = new JSONObject(s);
                    JSONArray schuelerArray = schueler.getJSONArray(Config.TAG_JSON_ARRAY);
                    for (int i = 0; i <= schuelerArray.length(); i++) {
                        JSONObject schuelerobjekt = schuelerArray.getJSONObject(i);

                        Schueler neuerStudent = new Schueler();
                        neuerStudent.setId(schuelerobjekt.getInt(Config.TAG_SID));
                        neuerStudent.setFirstname(schuelerobjekt.getString(Config.TAG_VORNAME));
                        neuerStudent.setLastname(schuelerobjekt.getString(Config.TAG_NAME));
                        neuerStudent.setKlasse(schuelerobjekt.getString(Config.TAG_KLASSE));

                        if (!klassenliste.isEmpty() || klassenliste.contains(neuerStudent.getKlasse())) {
                            // do nothing
                        } else {
                            klassenliste.add(neuerStudent.getKlasse());
                        }
                        schuelerliste.add(neuerStudent.toString());
                    }

                } catch (JSONException e) {
                    Log.e("json exception", "Could not load schueler liste!");
                }
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
