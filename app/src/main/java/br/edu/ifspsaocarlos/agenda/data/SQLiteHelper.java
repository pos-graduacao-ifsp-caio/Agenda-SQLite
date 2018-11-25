package br.edu.ifspsaocarlos.agenda.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda.db";
    static final String DATABASE_TABLE = "contatos";
    static final String KEY_ID = "id";
    static final String KEY_NAME = "nome";
    static final String KEY_FONE = "fone";
    static final String KEY_FONE_SECUNDARIO = "foneSecundario";
    static final String KEY_EMAIL = "email";
    static final String KEY_BIRTHDAY_DATE = "birthdayDate";
    static final String KEY_IS_FAVORED = "isFavored";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_FONE_SECUNDARIO + " TEXT, "  +
            KEY_EMAIL + " TEXT, " +
            KEY_BIRTHDAY_DATE + " TEXT, " +
            KEY_IS_FAVORED + " BOOLEAN)";

    private static final String DATABASE_ALTER_TABLE_FIRST = "ALTER TABLE "+DATABASE_TABLE+" ADD COLUMN "+KEY_IS_FAVORED+" BOOLEAN";
    private static final String DATABASE_ALTER_TABLE_SECOND = "ALTER TABLE "+DATABASE_TABLE+" ADD COLUMN "+KEY_FONE_SECUNDARIO+" TEXT";
    private static final String DATABASE_ALTER_TABLE_THIRD = "ALTER TABLE "+DATABASE_TABLE+" ADD COLUMN "+KEY_BIRTHDAY_DATE+" TEXT";

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

    // Se o banco de dados ainda não existe então o método onCreate é chamado para criar o BD
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // verifica qual a versao do banco para ir realizando as alterações de cada versão
        if(DATABASE_VERSION == 2){
            database.execSQL(DATABASE_ALTER_TABLE_FIRST);   // Na versão 2 do banco é adicionada uma nova coluna na tabela.
        }   else if(DATABASE_VERSION == 3){
            database.execSQL(DATABASE_ALTER_TABLE_SECOND);   // Na versão 3 do banco é adicionada uma nova coluna na tabela.
        }   else if(DATABASE_VERSION == 4){
            database.execSQL(DATABASE_ALTER_TABLE_THIRD);   // Na versão 4 do banco é adicionada uma nova coluna na tabela.
        }
    }
}

