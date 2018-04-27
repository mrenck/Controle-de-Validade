package renck.marcelo.controledevalidade;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistroActivity extends AppCompatActivity {

    //SQLiteOpenHelper openHelper;
    //SQLiteDatabase db;
    //Button _btnReg;
    //EditText _txtNome, _txtEmail, _txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //openHelper = new DatabaseHelper(this);

        final Button _btnReg = (Button)findViewById(R.id.btnReg);
        final EditText _txtNome = (EditText)findViewById(R.id.txtNome);
        final EditText _txtEmail = (EditText)findViewById(R.id.txtEmail);
        final EditText _txtSenha = (EditText)findViewById(R.id.txtSenha);

        _btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //db = openHelper.getWritableDatabase();
                String nome = _txtNome.getText().toString();
                String email = _txtEmail.getText().toString();
                String senha = _txtSenha.getText().toString();

                //insertdata(nome, email, senha);
                //Toast.makeText(getApplicationContext(), "Registro efetuado com sucesso", Toast.LENGTH_LONG).show();

                final EditText txtNome = (EditText) findViewById(R.id.txtNome) ;
                final EditText txtEmail = (EditText) findViewById(R.id.txtEmail) ;
                final EditText txtSenha = (EditText) findViewById(R.id.txtSenha);

                txtNome.setText("");
                txtEmail.setText("");
                txtSenha.setText("");
            }
        });
    }

    /*
    public void insertdata(String nome, String email, String senha){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2, nome);
        contentValues.put(DatabaseHelper.COL_3, email);
        contentValues.put(DatabaseHelper.COL_4, senha);
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }
    */

}

