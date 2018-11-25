package br.edu.ifspsaocarlos.agenda.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.R;


public class DetalheActivity extends AppCompatActivity {
    private Contato contato;
    private ContatoDAO contatoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("contato")) {   // verifica se existe um contato passado na intent.
            this.contato = (Contato) getIntent().getSerializableExtra("contato");

            EditText nameText = findViewById(R.id.editTextNome);
            nameText.setText(contato.getNome());
            EditText foneText = findViewById(R.id.editTextFone);
            foneText.setText(contato.getFone());
            EditText foneSecundarioText = findViewById(R.id.editTextFoneSecundario);
            foneSecundarioText.setText(contato.getFoneSecundario());
            EditText emailText = findViewById(R.id.editTextEmail);
            emailText.setText(contato.getEmail());
            EditText dataAniversarioText = findViewById(R.id.editTextDataAniversario);
            dataAniversarioText.setText(contato.getBirthdayDate());

            int posicao = contato.getNome().indexOf(" ");

            if (posicao == -1) {
                posicao = contato.getNome().length();
            }
            setTitle(contato.getNome().substring(0,posicao));
        }
        contatoDAO = new ContatoDAO(this);
    }

    // infla o menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);  // Infla o menu; this adds items to the action bar if it is present.
        if (!getIntent().hasExtra("contato")) {         // se ñ passar o contato  entao ñ apresenta o bottao de excluir na barra
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }
        return true;
    }

    // trata o evento de quem foi clicado no menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                apagar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void apagar() {
        contatoDAO.apagaContato(contato);
        Intent resultIntent = new Intent();
        setResult(3,resultIntent);   // da um setResult  para setar o resultado e poder tratar na activity de baixo
        finish();       // fecha a activity e volta para a activity que estava na camada abaixo desta.
    }

    private void salvar() {
        String name = ((EditText) findViewById(R.id.editTextNome)).getText().toString();
        String fone = ((EditText) findViewById(R.id.editTextFone)).getText().toString();
        String foneSecundario = ((EditText) findViewById(R.id.editTextFoneSecundario)).getText().toString();
        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
        String dataAniversario = ((EditText) findViewById(R.id.editTextDataAniversario)).getText().toString();

        if (contato == null) {
            contato = new Contato();
        }

        contato.setNome(name);
        contato.setFone(fone);
        contato.setFoneSecundario(foneSecundario);
        contato.setEmail(email);
        contato.setBirthdayDate(dataAniversario);

        contatoDAO.salvaContato(contato);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }
}