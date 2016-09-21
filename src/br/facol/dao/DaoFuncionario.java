/*
 * Este é o modelo de classe Dao para cada
 * tabela do seu banco de dados...
 */
package br.facol.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import br.facol.modelo.Funcionario;

public class DaoFuncionario {
	private Context context;
	private DaoAdapter banco;

	public DaoFuncionario(Context context) {
		this.context = context;
		//instanciamos o DaoAdapter (Dao mãe)
		banco = new DaoAdapter(context);
	}

	public long insert(Funcionario funcionario) {
		/*
		 * Este método é um pouco mais trabalhado porém, nos retorna o id do
		 * utlimo registro. Este processo soluciona quando temos que inserir
		 * dados em chaves estrangeiras em outras tabelas...
		 */
		ContentValues values = new ContentValues();
		values.put("nome", funcionario.getNome());
		values.put("email", funcionario.getEmail());
		values.put("nascimento", formataCalendarParaDate(funcionario.getNascimento()));
		values.put("telefone", funcionario.getTelefone());

		long result = banco.queryInsertLastId("funcionario", values);

		return result;
	}

	//Método de alteração
	public boolean update(Funcionario funcionario) {
		Object[] args = { funcionario.getNome(), funcionario.getEmail(),
				formataCalendarParaDate(funcionario.getNascimento()), funcionario.getTelefone(), funcionario.getId() };

		boolean result = banco.queryExecute("UPDATE funcionario SET " + "nome = ?, " + "email = ?, "
				+ "nascimento = ?, " + "telefone = ?" + "WHERE id = ?;", args);

		return result;
	}
	
	//Método de exclusão
	public boolean delete(long id) {
		Object[] args = { id };
		boolean result = banco.queryExecute("DELETE FROM funcionario WHERE id = ?", args);

		return result;
	}

	//Método de consulta geral
	public ArrayList<Funcionario> getTodos() {
		ArrayList<Funcionario> funcionarios = new ArrayList<Funcionario>();
		ObjetoBanco ob = banco.queryConsulta("SELECT * FROM funcionario ORDER BY id DESC", null);

		if (ob != null) {
			for (int i = 0; i < ob.size(); i++) {
				Funcionario funcionario = new Funcionario();
				funcionario.setId(ob.getLong(i, "id"));
				funcionario.setNome(ob.getString(i, "nome"));
				funcionario.setEmail(ob.getString(i, "email"));
				funcionario.setNascimento(formataDateParaCalendar(ob.getString(i, "nascimento")));
				funcionario.setTelefone(ob.getString(i, "telefone"));

				funcionarios.add(funcionario);
			}
		}

		return funcionarios;
	}

	//Método de consulta especifica
	public Funcionario getFuncionario(long id) {
		String[] args = { String.valueOf(id) };
		ObjetoBanco ob = banco.queryConsulta("SELECT * FROM funcionario WHERE id = ? ORDER BY id DESC", args);

		Funcionario funcionario = null;
		if (ob != null) {
			funcionario = new Funcionario();
			funcionario.setId(ob.getLong(0, "id"));
			funcionario.setNome(ob.getString(0, "nome"));
			funcionario.setEmail(ob.getString(0, "email"));
			funcionario.setNascimento(formataDateParaCalendar(ob.getString(0, "nascimento")));
			funcionario.setTelefone(ob.getString(0, "telefone"));
		}

		return funcionario;
	}

	// Conversão de Date para Calendar e vice-versa
	private String formataCalendarParaDate(Calendar data) {
		String ret = null;
		if (data != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

			ret = df.format(data.getTime());
		}
		Log.i("teste", "teste: " + ret);
		return ret;
	}

	private Calendar formataDateParaCalendar(String data) {
		Calendar ret = null;
		if (data != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

			try {
				ret = Calendar.getInstance();
				ret.setTime(df.parse(data));
			} catch (ParseException e) {
				Log.e("teste", "DaoFuncionario formataDateParaCalendar(): " + e.getMessage());
			}
		}
		return ret;
	}
	// Conversão de Date para Calendar e vice-versa

	// Conversão de String para Calendar e vice-versa
	public static String formataCalendarParaString(Calendar data) {
		String ret = null;
		if (data != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

			ret = df.format(data.getTime());
		}
		return ret;
	}

	public static Calendar formataStringParaCalendar(String data) {
		Calendar ret = null;
		if (data != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

			try {
				ret = Calendar.getInstance();
				ret.setTime(df.parse(data));
			} catch (ParseException e) {
				Log.e("teste", "DaoFuncionario formataStringParaCalendar(): " + e.getMessage());
			}
		}
		return ret;
	}
	// Conversão de String para Calendar e vice-versa
}
