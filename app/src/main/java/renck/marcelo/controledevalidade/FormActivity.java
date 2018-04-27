package renck.marcelo.controledevalidade;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormActivity extends AppCompatActivity {

    public String userMsg;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String postUrl= "http://192.168.61.2/produtosdesc/jwt-api/";

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Intent intent = getIntent();

        final EditText codigoText = (EditText) findViewById(R.id.codigoText) ;
        codigoText.setText(intent.getStringExtra("scanResult"));

        final String postBody = "{\n\t\"name\":\"getProdutosDetailsByCodBarras\",\n\t\"param\":{\n\t\t\"codBarras\":"+codigoText.getText().toString()+"\n\t}\n}";

        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        openHelper = new DatabaseHelper(this);

        final Button _btnInsert = (Button)findViewById(R.id.btnInsert);
        final EditText _codigoText = (EditText)findViewById(R.id.codigoText);
        final EditText _descText = (EditText)findViewById(R.id.descText);
        final EditText _validadeText = (EditText) findViewById(R.id.validadeText);
        final EditText _qtdText = (EditText)findViewById(R.id.qtdText);
        final EditText _precoText = (EditText)findViewById(R.id.precoText);

        _btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = openHelper.getWritableDatabase();
                insertdata("7894000", "MAIZENA 500G", 1, "8.50", 1, "2020-02-01");
                Toast.makeText(getApplicationContext(), "Registro efetuado com sucesso", Toast.LENGTH_LONG).show();

                //Chama a tela do ListView
                Intent intent = new Intent(FormActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void insertdata(String cod_barras, String descricao, int quantidade, String preco, int local, String validade){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2, cod_barras);
        contentValues.put(DatabaseHelper.COL_3, descricao);
        contentValues.put(DatabaseHelper.COL_4, quantidade);
        contentValues.put(DatabaseHelper.COL_5, preco);
        contentValues.put(DatabaseHelper.COL_6, local);
        contentValues.put(DatabaseHelper.COL_7, validade);
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    void postRequest(String postUrl,String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        //getting shared preferences
        SharedPreferences sp = getSharedPreferences("Token", MODE_PRIVATE);
        //Toast.makeText(getApplicationContext(), "Shared Token: " + sp.getString("Token", "default value"),Toast.LENGTH_LONG).show();

        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + sp.getString("Token", "default value"))
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //Log.d("TAG",response.body().string());

                final String myResponse = response.body().string();

                FormActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(myResponse);

                            if (json.getJSONObject("response").getInt("status") == 200) {

                                final EditText descTxt = (EditText) findViewById(R.id.descText);
                                descTxt.setText(json.getJSONObject("response").getJSONObject("result").getString("descricao"));

                            } else {
                                userMsg = json.getJSONObject("error").getString("message");
                                Log.d("TAG",userMsg);
                                Toast.makeText(getApplicationContext(), userMsg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }
}
