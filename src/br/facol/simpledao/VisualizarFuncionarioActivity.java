package br.facol.simpledao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import br.facol.dao.DaoFuncionario;
import br.facol.modelo.Funcionario;

public class VisualizarFuncionarioActivity extends Activity {

	private final int editarFuncionario = 0001;
	private TextView nome;
	private TextView nascimento;
	private TextView email;
	private TextView telefone;
	private Funcionario funView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizar_funcionario);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		nome = (TextView) findViewById(R.id.lblNome);
		nascimento = (TextView) findViewById(R.id.lblNascimento);
		email = (TextView) findViewById(R.id.lblEmail);
		telefone = (TextView) findViewById(R.id.lblTelefone);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// recebemos dados via intent
		Bundle extras = getIntent().getExtras();
		long id = extras.getLong("id");

		// instanciamos o objeto dao
		DaoFuncionario dao = new DaoFuncionario(VisualizarFuncionarioActivity.this);

		// tentamos pegar a tupla do banco via id
		try {
			funView = dao.getFuncionario(id);
		} catch (Exception e) {
			Log.e("teste", "VisualizarFuncionarioActivity onCreate: " + e.getMessage());
			finish();
		}

		// exibimos os valores
		nome.setText(funView.getNome());
		nascimento.setText(DaoFuncionario.formataCalendarParaString(funView.getNascimento()));
		email.setText(funView.getEmail());
		telefone.setText(funView.getTelefone());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, editarFuncionario, 0, "Editar Funcionário");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Intent i;
		switch (id) {
		case editarFuncionario:
			i = new Intent(VisualizarFuncionarioActivity.this, CadastroFuncionarioActivity.class);
			i.putExtra("id", funView.getId());
			startActivity(i);
			break;
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}