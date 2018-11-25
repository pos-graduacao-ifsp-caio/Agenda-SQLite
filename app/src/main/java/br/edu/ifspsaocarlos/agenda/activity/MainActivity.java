package br.edu.ifspsaocarlos.agenda.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;

public class MainActivity extends AppCompatActivity {

    private ContatoDAO cDAO ;
    private RecyclerView recyclerView;

    private List<Contato> contatos = new ArrayList<>();
    private TextView empty;

    private ContatoAdapter adapter;
    private SearchView searchView;

    private FloatingActionButton fab;

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
            updateUI(null);
        }   else {
                super.onBackPressed();
            }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    // trabalha com o searchView
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.clearFocus();
            updateUI(query);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        handleIntent(intent);

        cDAO= new ContatoDAO(this);

        empty= (TextView) findViewById(R.id.empty_view);   // textView   Sua lista está vazia.

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        adapter = new ContatoAdapter(contatos, this);
        recyclerView.setAdapter(adapter);

        // setupRecyclerView();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        // abre a tela de cadastro de contato  ao clicar no floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                startActivityForResult(i, 1);
            }
        });

        updateUI(null);
    }
    // infla o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();

        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText)findViewById(R.id.search_src_text);
                if (et.getText().toString().isEmpty()) {
                    searchView.onActionViewCollapsed();
                }

                searchView.setQuery("", false);
                updateUI(null);
            }
        });

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem_id = item.getItemId();  // recupera o id do item que foi clicado.

        switch (menuItem_id){
            case R.id.menuItem_exibirFaoritos:
                this.showFavoriteContacts();
                break;
        }

        return true;
    }

    // quande fecha a detalheActivity  esse método é chamado para verificar qual o requestCode foi setado na detalheActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if (resultCode == RESULT_OK) {
                showSnackBar(getResources().getString(R.string.contato_adicionado));
                updateUI(null);
            }

        if (requestCode == 2) {             // REQUEST CODE
            if (resultCode == RESULT_OK)
                showSnackBar(getResources().getString(R.string.contato_alterado));

            if (resultCode == 3)                //RESULT CODE
                showSnackBar(getResources().getString(R.string.contato_apagado));

            updateUI(null);
        }
    }

    private void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout= (CoordinatorLayout)findViewById(R.id.coordlayout);
        Snackbar.make(coordinatorlayout, msg, Snackbar.LENGTH_LONG).show();
    }

    // recupera os contatos de acordo com o parâmetro de busca informado.
    @SuppressLint("RestrictedApi")
    private void updateUI(String nomeContato) {
        contatos.clear();

        if (nomeContato == null) {
            contatos.addAll(cDAO.buscaTodosContatos());
            empty.setText(getResources().getString(R.string.lista_vazia));
            fab.setVisibility(View.VISIBLE);

        }
        else {
            contatos.addAll(cDAO.buscaContato(nomeContato));
            empty.setText(getResources().getString(R.string.contato_nao_encontrado));
            fab.setVisibility(View.GONE);
        }

        recyclerView.getAdapter().notifyDataSetChanged();

        // verifica se existe alguma célula criada na recyclerView
        if (recyclerView.getAdapter().getItemCount() == 0) {
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
        }
    }

    // configura a implementacao do listener
    private void setupRecyclerView() {
        adapter.setClickListener(new ContatoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                final Contato contato;

                if(view.getId() == R.id.nome){
                    contato = contatos.get(position);
                    Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                    i.putExtra("contato", contato);
                    startActivityForResult(i, 2);
                }   else{
                        contato = contatos.get(position);
                        // A partir da referência da view que vem por parâmetro, é possível recuperar o imageView que está dentro da view.
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


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // trata configurações de swipe na recyclerView.
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {    // verifica se deu o swipe para a direita
                    Contato contato = contatos.get(viewHolder.getAdapterPosition());
                    cDAO.apagaContato(contato);
                    contatos.remove(viewHolder.getAdapterPosition());     // se deslizar p a direita entao exclui o contato.
                    recyclerView.getAdapter().notifyDataSetChanged();
                    showSnackBar(getResources().getString(R.string.contato_apagado));
                    updateUI(null);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_remove);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
        updateUI(null);
    }

    // Abre a activity que contém os contatos que foram favoritados.
    @SuppressLint("RestrictedApi")
    public void showFavoriteContacts(){
        Intent intentFavoritos = new Intent(getApplicationContext(), FavoritosActivity.class);
        startActivity(intentFavoritos);
    }

}
