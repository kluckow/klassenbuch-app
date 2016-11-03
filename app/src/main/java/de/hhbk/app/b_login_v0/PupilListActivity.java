package de.hhbk.app.b_login_v0;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class PupilListActivity extends AppCompatActivity {

    private ArrayAdapter<String> klassenAdapter;
    private ArrayAdapter<String> schuelerAdapter;

    private List<Schueler> schuelerliste = new ArrayList<>();
    private List<String> klassenNamen = new ArrayList<>();

    private ListView listViewSchueler;
    private Spinner spinner;

    private HashMap<String, ArrayList<Schueler>> klassenListe = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_list_activity);

        spinner = (Spinner) findViewById(R.id.spinner_class);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {
                String selectedKlasse = spinner.getItemAtPosition(pos).toString();
                schuelerliste.clear();
                Collection<Schueler> collection = new ArrayList<Schueler>(klassenListe.get(selectedKlasse));
                schuelerliste.addAll(collection);
                schuelerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Log.e("test", "test");
                // TODO Auto-generated method stub
            }
        });
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

                klassenAdapter = new ArrayAdapter(PupilListActivity.this, R.layout.activity_pupil_item, klassenNamen);
                spinner.setAdapter(klassenAdapter);
                schuelerAdapter = new ArrayAdapter(PupilListActivity.this, R.layout.activity_pupil_item, schuelerliste);
                schuelerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listViewSchueler.setAdapter(schuelerAdapter);

                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String result = rh.sendGetRequest("http://hhbk.bplaced.net/alleSchueler_v3.php");
                // TODO: remove logging
                Log.e("debug: ", result);
                try {
                    JSONObject schueler = new JSONObject(result);
                    JSONArray schuelerArray = schueler.getJSONArray(Config.TAG_JSON_ARRAY);
                    for (int i = 0; i <= schuelerArray.length(); i++) {
                        JSONObject schuelerobjekt = schuelerArray.getJSONObject(i);

                        Schueler neuerStudent = new Schueler();
                        neuerStudent.setId(schuelerobjekt.getInt(Config.TAG_SID));
                        neuerStudent.setFirstname(schuelerobjekt.getString(Config.TAG_VORNAME));
                        neuerStudent.setLastname(schuelerobjekt.getString(Config.TAG_NAME));
                        neuerStudent.setKlasse(schuelerobjekt.getString(Config.TAG_KLASSE));

                        ArrayList<Schueler> tmpSchuelerList = new ArrayList<>();

                        if (klassenNamen.contains(neuerStudent.getKlasse())) {
                            tmpSchuelerList.addAll(klassenListe.get(neuerStudent.getKlasse()));
                            tmpSchuelerList.add(neuerStudent);
                            klassenListe.put(neuerStudent.getKlasse(), tmpSchuelerList);
                        } else {
                            tmpSchuelerList.add(neuerStudent);
                            klassenListe.put(neuerStudent.getKlasse(), tmpSchuelerList);
                            klassenNamen.add(neuerStudent.getKlasse());
                        }
                    }

                } catch (JSONException e) {
                    Log.e("json exception", "Could not load schueler liste!");
                }
                return result;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
