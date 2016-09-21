package br.facol.simpledao;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.facol.dao.DaoAdapter;
import br.facol.dao.DaoFuncionario;
import br.facol.modelo.Funcionario;

public class MainActivity extends Activity {

	private final int cadFuncionario = 0001;
	private final int visFuncionario = 0002;
	private ListView lista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//exibimos o botão voltar na action bar (no icone)
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Asseguramos que a nossa base está criada
		DaoAdapter dao = new DaoAdapter(MainActivity.this);
		dao.onCreate(dao.getWritableDatabase());

		//Linkamos a lista XML com a classe ListView instanciando o seu objeto
		lista = (ListView) findViewById(R.id.lista);

	}

	@Override
	protected void onResume() {
		super.onResume();

		final DaoFuncionario daoFuncionario = new DaoFuncionario(MainActivity.this);

		final ArrayList<String> values = new ArrayList<String>();

		// pegamos os Funcionários do Banco
		final ArrayList<Funcionario> funcionarios = daoFuncionario.getTodos();
		for (Funcionario fun : funcionarios) {
			values.add(fun.getNome());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,
				android.R.id.text1, values);

		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*
				 * Abrimos pegando a posição tocada pelo usuário. O método get,
				 * pega o dado do vetor utilizando indices.
				 */
				Funcionario funToque = funcionarios.get(position);
				Toast.makeText(MainActivity.this, "Visualizar " + funToque.getNome(), Toast.LENGTH_SHORT).show();
				Intent i = new Intent(MainActivity.this, VisualizarFuncionarioActivity.class);
				i.putExtra("id", funToque.getId());
				startActivity(i);
			}
		});

		lista.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final Funcionario funToque = funcionarios.get(position);

				/*
				 * Código responsável pela abertura de uma caixa
				 * de dialogo com uma pergunta de sim e não...
				 */
				new AlertDialog.Builder(MainActivity.this)
						.setMessage("Tem certeza que deseja remover " + funToque.getNome() + "?").setCancelable(false)
						.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (daoFuncionario.delete(funToque.getId())) {
							Toast.makeText(MainActivity.this, funToque.getNome() + " removido com Sucesso",
									Toast.LENGTH_SHORT).show();
							onResume();
						} else {
							Toast.makeText(MainActivity.this, "Erro ao remover " + funToque.getNome(),
									Toast.LENGTH_SHORT).show();
						}
					}
				}).setNegativeButton("Não", null).show();
				//Fim da caixa de dialogo

				return false;
			}
		});

		lista.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Adicionamos um item ao menu
		//Grupo de botões, id do botão, ordem, titulo do botão
		menu.add(0, cadFuncionario, 0, "Cadastrar Funcionário");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Intent i;
		switch (id) {
		case cadFuncionario:
			//abrimos a activity CadastroFuncionarioActivity
			i = new Intent(MainActivity.this, CadastroFuncionarioActivity.class);
			startActivity(i);
			break;
		case android.R.id.home:
			//Ação do botão voltar, no caso fecha a janela
			finish();
			break;
		default:
			//Tratamos em caso de opção inválida (erro de programação)
			Toast.makeText(MainActivity.this, "Opção inválida", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
