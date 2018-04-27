package renck.marcelo.controledevalidade;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton _fab;

    private ListView mListView;
    DatabaseHelper mDatabaseHelper;
    private static final String TAG = "TelaLista";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.prodList);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chama leitura de código de barras
                //Intent intent = new Intent(MainActivity.this, LeituraActivity.class);
                //startActivity(intent);

                //Chama form de cadastro
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);

            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Exibindo dados no ListView.");

        //busca os dados e atribui à lista
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //Busca o valor do BD na coluna 2 e adiciona ao array
            listData.add(data.getString(2));
        }
        //Cria o list adapter e seta o adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);
            }
        });
    }

}
