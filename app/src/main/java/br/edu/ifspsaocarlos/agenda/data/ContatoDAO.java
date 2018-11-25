package br.edu.ifspsaocarlos.agenda.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {
    private SQLiteDatabase database;   // CLASSE que provê os métodos de manipulação dos dados no banco insert/update/delete
    private SQLiteHelper dbHelper;

    public ContatoDAO(Context context) {
        this.dbHelper = new SQLiteHelper(context);
    }

    public  List<Contato> buscaTodosContatos() {
        database = dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[] { SQLiteHelper.KEY_ID,
                                       SQLiteHelper.KEY_NAME,
                                       SQLiteHelper.KEY_FONE,
                                       SQLiteHelper.KEY_FONE_SECUNDARIO,
                                       SQLiteHelper.KEY_EMAIL,
                                       SQLiteHelper.KEY_BIRTHDAY_DATE,
                                       SQLiteHelper.KEY_IS_FAVORED };

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, null , null,
                null, null, SQLiteHelper.KEY_NAME);   // ordena a consulta pelo nome do contato

        while (cursor.moveToNext()) {
            Contato contato = new Contato();

            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setFoneSecundario(cursor.getString(3));
            contato.setEmail(cursor.getString(4));
            contato.setBirthdayDate(cursor.getString(5));
            contato.setIsFavored(cursor.getInt(6));
            contatos.add(contato);
        }

        cursor.close();

        database.close();
        return contatos;
    }

    // busca apenas os contatos que foram favoritados.
    public  List<Contato> buscaTodosContatosFavoritos() {
        database = dbHelper.getReadableDatabase();
        List<Contato> contatosFavoritos = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[] { SQLiteHelper.KEY_ID,
                SQLiteHelper.KEY_NAME,
                SQLiteHelper.KEY_FONE,
                SQLiteHelper.KEY_FONE_SECUNDARIO,
                SQLiteHelper.KEY_EMAIL,
                SQLiteHelper.KEY_BIRTHDAY_DATE,
                SQLiteHelper.KEY_IS_FAVORED };

        String where = SQLiteHelper.KEY_IS_FAVORED + " = ?";
        String[] argWhere = new String[]{ "1" };

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);   // ordena a consulta pelo nome do contato

        while (cursor.moveToNext()) {
            Contato contatoFavorito = new Contato();

            contatoFavorito.setId(cursor.getInt(0));
            contatoFavorito.setNome(cursor.getString(1));
            contatoFavorito.setFone(cursor.getString(2));
            contatoFavorito.setFoneSecundario(cursor.getString(3));
            contatoFavorito.setEmail(cursor.getString(4));
            contatoFavorito.setBirthdayDate(cursor.getString(5));
            contatoFavorito.setIsFavored(cursor.getInt(6));
            contatosFavoritos.add(contatoFavorito);
        }

        cursor.close();

        database.close();
        return contatosFavoritos;
    }

    // conforme o nome passado, busca a lista de contatos que possuem esse nome
    public  List<Contato> buscaContato(String nome) {
        database = dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[] { SQLiteHelper.KEY_ID,
                SQLiteHelper.KEY_NAME,
                SQLiteHelper.KEY_FONE,
                SQLiteHelper.KEY_FONE_SECUNDARIO,
                SQLiteHelper.KEY_EMAIL,
                SQLiteHelper.KEY_BIRTHDAY_DATE,
                SQLiteHelper.KEY_IS_FAVORED };

        String where = SQLiteHelper.KEY_NAME + " like ? OR " + SQLiteHelper.KEY_EMAIL +" like ?";
        String[] argWhere = new String[]{nome + "%", "%" + nome + "%"};


        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext()) {
            Contato contato = new Contato();

            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setFoneSecundario(cursor.getString(3));
            contato.setEmail(cursor.getString(4));
            contato.setBirthdayDate(cursor.getString(5));
            contato.setIsFavored(cursor.getInt(6));
            contatos.add(contato);
        }

        cursor.close();

        database.close();
        return contatos;
    }

    public void salvaContato(Contato c) {               // esse método é usado para  cadastrar e atualizar
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_FONE_SECUNDARIO, c.getFoneSecundario());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_BIRTHDAY_DATE, c.getBirthdayDate());
        values.put(SQLiteHelper.KEY_IS_FAVORED, c.getIsFavored());

       if (c.getId() > 0) {    // se o id for maior que 0  então significa que é um usuário que será atualizado.
            database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "=" + c.getId(), null);

       }    else {             // se o id for menor ou igual a 0  então significa que é um usuário que será cadastrado.
               database.insert(SQLiteHelper.DATABASE_TABLE, null, values);
            }

        database.close();
    }

    public void apagaContato(Contato c) {
        database = dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "=" + c.getId(), null);
        database.close();
    }

    // altera a coluna que informa se o contato está favoritado na lista de contatos ou não.
    public void alterarContatoFavorito(Contato c){

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_FONE_SECUNDARIO, c.getFoneSecundario());
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_BIRTHDAY_DATE, c.getBirthdayDate());
        values.put(SQLiteHelper.KEY_IS_FAVORED, c.getIsFavored());

        database = dbHelper.getWritableDatabase();
        database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "=" + c.getId(), null);
        database.close();
    }
}