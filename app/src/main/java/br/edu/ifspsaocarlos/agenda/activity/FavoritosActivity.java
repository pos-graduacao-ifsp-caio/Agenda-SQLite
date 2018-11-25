package br.edu.ifspsaocarlos.agenda.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;

public class FavoritosActivity extends AppCompatActivity {

    private ContatoDAO cDAO ;
    private RecyclerView recyclerView;

    List<Contato> contatosFavoritos = new ArrayList<>();
    private TextView empty;

    private ContatoAdapter adapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        cDAO = new ContatoDAO(this);

        empty = (TextView) findViewById(R.id.empty_view_favoritos);   // textView   Sua lista está vazia.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_favoritos);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_favoritos);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        contatosFavoritos = cDAO.buscaTodosContatosFavoritos();

        adapter = new ContatoAdapter(contatosFavoritos, this);
        recyclerView.setAdapter(adapter);

        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        empty.setText(getResources().getString(R.string.lista_favoritos_vazia));

        if (recyclerView.getAdapter().getItemCount() == 0) {
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
        }
    }

    // trata configurações de swipe na recyclerView .
    private void setupRecyclerView() {
        adapter.setClickListener(new ContatoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                final Contato contato;

                if(view.getId() == R.id.nome){
                    contato = contatosFavoritos.get(position);
                    Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                    i.putExtra("contato", contato);
                    startActivity(i);
                }   else{
                    contato = contatosFavoritos.get(position);
                    // A partir da referência da view que veio p parâmetro é possível recuperar o imageView que está dentro da view.
                    ImageView imgView_favorito = (ImageView) view; //findViewById(R.id.img_view_favorito);

                    if(contato.getIsFavored() == 0){
                        contato.setIsFavored(1);
                        imgView_favorito.setImageResource(R.mipmap.ic_yes_favorite);
                    }   else{
                        contato.setIsFavored(0);
                        imgView_favorito.setImageResource(R.mipmap.ic_no_favorite);
                    }

                    cDAO.alterarContatoFavorito(contato);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        empty.setVisibility(View.GONE);
        finish();
    }
}
