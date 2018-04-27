package renck.marcelo.controledevalidade;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    public String token;
    public int status;
    public String userMsg;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String postUrl= "http://192.168.61.2/produtosdesc/jwt-api/";

    /*
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Button _btnLog;
    EditText _txtEmail1, _txtSenha1;
    Cursor cursor;

    Button _btnCad;
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button _btnLog = (Button) findViewById(R.id.btnLog);
        final EditText _txtEmail1 = (EditText) findViewById(R.id.txtEmail1);
        final EditText _txtSenha1 = (EditText) findViewById(R.id.txtSenha1);
        final Button _btnCad = (Button) findViewById(R.id.btnCad);

        _btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final String username = _txtEmail1.getText().toString();
                //final String senha = _txtSenha1.getText().toString();

                final String postBody = "{\n\t\"name\":\"generateToken\",\n\t\"param\":{\n\t\t\"email\":\""+_txtEmail1.getText().toString()+"\",\n\t\t\"pass\":\""+_txtSenha1.getText().toString()+"\"\n\t}\n}";
                //Toast.makeText(getApplicationContext(), postBody, Toast.LENGTH_LONG).show();
                //final String postBody = "{\n\t\"name\":\"generateToken\",\n\t\"param\":{\n\t\t\"email\":\"admin@gmail.com\",\n\t\t\"pass\":\"123\"\n\t}\n}";

                try {
                    postRequest(postUrl,postBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //login
        /*

        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();
        _btnLog = (Button)findViewById(R.id.btnLog);
        _txtEmail1 = (EditText) findViewById(R.id.txtEmail1);
        _txtSenha1 = (EditText) findViewById(R.id.txtSenha1);

        _btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _txtEmail1.getText().toString();
                String senha = _txtSenha1.getText().toString();

                cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME+" WHERE "+DatabaseHelper.COL_3 + "=? AND "+ DatabaseHelper.COL_4 + " =? ", new String[]{email, senha});
                if(cursor != null){
                    if (cursor.getCount()>0){
                        cursor.moveToNext();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                        //Toast.makeText(getApplicationContext(), "Login Efetuado com Sucesso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuário ou senha inválidos",Toast.LENGTH_LONG).show();


                    }
                }
            }
        });
        //fim login
        */

        //chamar o cadastro
        _btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
        //fim chamar o cadastro


    }

    void postRequest(String postUrl,String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
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

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(myResponse);

                            if (json.getJSONObject("response").getInt("status") == 200) {
                                token = json.getJSONObject("response").getJSONObject("result").getString("token");

                                //getting shared preferences
                                SharedPreferences sp = getSharedPreferences("Token", MODE_PRIVATE);
                                //initializing editor
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Token", token);
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
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


    protected void onResume(){
        super.onResume();

        final EditText txtEmail1 = (EditText) findViewById(R.id.txtEmail1) ;
        final EditText txtSenha1 = (EditText) findViewById(R.id.txtSenha1);

        txtEmail1.setText("");
        txtSenha1.setText("");
    }
}

