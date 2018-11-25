package br.edu.ifspsaocarlos.agenda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.R;

import java.util.List;


public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder> {

    private List<Contato> contatos;
    private Context context;

    private static ItemClickListener clickListener;

    public ContatoAdapter(List<Contato> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    // transforma o arquivo xml em uma view na activity.
    @Override
    public ContatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contato_celula, parent, false);
        return new ContatoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ContatoViewHolder holder, int position) {
        Contato contato  = contatos.get(position) ;
        holder.nome.setText(contato.getNome());
        // verifica se o contato está favoritado para setar a imagem correspondente no ImageView
        if(contato.getIsFavored() == 1){
            holder.imgviewFavorito.setImageResource(R.mipmap.ic_yes_favorite);
        }   else{
            holder.imgviewFavorito.setImageResource(R.mipmap.ic_no_favorite);
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }


    public  class ContatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView nome;
        final ImageView imgviewFavorito;

        ContatoViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.nome);
            imgviewFavorito = view.findViewById(R.id.img_view_favorito);

            nome.setOnClickListener(this);
            imgviewFavorito.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //int id_view = view.getId();  // pega o id da view que foi clicada. Ou foi o editText ou o ImageView

            if (clickListener != null) {
                // passa como parâmetro a posicao do item da recyclerView, o id do componente que foi clicado e a propria referência da view.
                clickListener.onItemClick(getAdapterPosition(), view);
            }
        }
    }

    // passa como parâmetro a posicao do item da recyclerView e o id do componente que foi clicado.
    public interface ItemClickListener {
        void onItemClick(int position, View imagem);
    }
}


