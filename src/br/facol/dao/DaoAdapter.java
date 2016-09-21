/*
 * Cria e manipula todo o banco de dados.
 */
package br.facol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DaoAdapter extends SQLiteOpenHelper {

	//nome do banco
	public static final String BANCO = "aula";
	/*
	 * Vers�o do banco, utilizamos quando fazemos o controle
	 * de vers�o do banco de dados...
	 */
	public static final int VERSAO = 1;

	//Query de exlus�o de todas as tabelas
	private static final String queryDelete[] = {
			"DROP TABLE IF EXISTS funcionario;"
	};
	//Query de cria��o de todas as tabelas
	private static final String query[] = {
			  "CREATE TABLE IF NOT EXISTS funcionario ("
			  + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
			  + "nome VARCHAR(30) NOT NULL,"
			  + "nascimento DATE NOT NULL,"
			  + "email VARCHAR(100) NOT NULL,"
			  + "telefone VARCHAR(12) NOT NULL"
			  + ");"
	};
	
	//Construtor DaoAdapter
	public DaoAdapter(Context context) {
		super(context, BANCO, null, VERSAO);
	}

	//M�todo de cria��o do banco (gera um novo banco)
	@Override
	public void onCreate(SQLiteDatabase db) {
		for(int i = 0; i < query.length; i++) db.execSQL(query[i]);
		//close();
	}
	
	//M�todo de reset para o banco (apaga e gera um novo banco)
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(int i = 0; i < queryDelete.length; i++) db.execSQL(queryDelete[i]);	//limpamos
		onCreate(db);															//criamos
		//close();
	}
	
	/*
	 * M�todo respons�vel pela execu��o de querys b�sicas
	 * como insert, update e delete...
	 */
	public boolean queryExecute(String query, Object[] args){
		/*
		 * String query => recebe uma query (INSERT INTO...)
		 * Object[] args => recebe os argumentos da query, exemplo:
		 *   INSERT INTO tabela (nome) VALUES (?)
		 * teriamos como argumentos o substituto do ?, ent�o,
		 * poderiamos enviar "F�bio" argumento, ou seja, o primeiro
		 * indice do vetor args seria "F�bio"...
		 * EVITA SQL INJECTION!
		 */
		boolean status = true;
		try {
			getWritableDatabase().execSQL(query, args);
		} catch(SQLException e){
			Log.e("teste", "DaoAdapter queryExecute: " + e.getMessage());
			status = false;
		}
		close();
		
		return status;
	}
	
	/*
	 * M�todo respons�vel pela execu��o de querys INSERT.
	 * Ao final, se sucesso, retorna o ID do registro inserido
	 * no banco...
	 */
	public long queryInsertLastId(String table, ContentValues values){
		/*
		 * table => nome da tabela
		 * values => valores da tabela (nome, telefone, rg...)
		 */
		long status = -1;
		try {
			status = getWritableDatabase().insert(table, null, values);
		} catch(SQLException e){
			Log.e("teste", "DaoAdapter queryInsertLastId: " + e.getMessage());
		}
		close();
		
		return status;
	}
	
	/*
	 * M�todo respons�vel pela execu��o de querys SELECT no banco
	 */
	public ObjetoBanco queryConsulta(String query, String[] args){
		/*
		 * query => SELECT * FROM tabela WHERE id = ?
		 * args => mesma explica��o do m�todo queryExecute
		 * exemplo: 5, ent�o ? seria substituido pelo numero 5
		 * se estivesse contido no primeiro indice de args 
		 */
		Cursor c = null;
		try {
			c = getReadableDatabase().rawQuery(query, args);
		} catch(SQLException e){
			Log.e("teste", "DaoAdapter queryConsulta: " + e.getMessage());
		}
		
		ObjetoBanco ob = null;
		
		if(c != null){
			ob = new ObjetoBanco();
			ob.setDados(c);
		}
		if(!c.isClosed()) c.close();
		close();
		
		return ob;
	}

}
