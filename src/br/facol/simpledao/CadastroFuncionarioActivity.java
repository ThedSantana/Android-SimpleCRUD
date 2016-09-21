/*
 * Classe manipuladora da Activity principal
 */
package br.facol.simpledao;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import br.facol.dao.DaoFuncionario;
import br.facol.modelo.Funcionario;

public class CadastroFuncionarioActivity extends Activity {

	/*
	 * Declaração dos atributos que serão linkados com
	 * o nosso layout...
	 */
	private EditText nome;
	private EditText nascimento;
	private EditText email;
	private EditText telefone;
	private Funcionario funView;
	private DaoFuncionario dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//linkamos a classe com o XML
		setContentView(R.layout.activity_cadastro_funcionario);
		
		/*
		 * Habilitamos o botão voltar ao clicar no icone do 
		 * aplicativo na barra superior à esquerda (ActionBar)
		 */
		getActionBar().setDisplayHomeAsUpEnabled(true);

		//Linkamos os objetos XML aos atributos
		nome = (EditText) findViewById(R.id.txtNome);
		nascimento = (EditText) findViewById(R.id.txtNascimento);
		email = (EditText) findViewById(R.id.txtEmail);
		telefone = (EditText) findViewById(R.id.txtTelefone);

		//instanciamos a classe DaoFuncionario
		dao = new DaoFuncionario(CadastroFuncionarioActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// recebemos dados via intent
		Bundle extras = getIntent().getExtras();
		long id = 0;
		if(extras != null){
			id = extras.getLong("id");
		}

		if (id > 0) {
			// tentamos pegar a tupla do banco via id
			try {
				funView = dao.getFuncionario(id);
			} catch (Exception e) {
				Log.e("teste", "CadastroFuncionarioActivity onCreate: " + e.getMessage());
				finish();
			}

			// exibimos os valores
			nome.setText(funView.getNome());
			nascimento.setText(DaoFuncionario.formataCalendarParaString(funView.getNascimento()));
			email.setText(funView.getEmail());
			telefone.setText(funView.getTelefone());
		}
	}
	
	/*
	 * Ao selecionar uma opção do menu...
	 * No caso, utilizamos para tratar a ação do botão Home,
	 * neste caso, ao clicar, fechamos a janela...
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	finish();
			break;
	    }
	    return super.onOptionsItemSelected(item);
	}

	/*
	 * Método de ação do botão btnSalvar contido no XML,
	 * configuramos na opção onClick para chamar o método
	 * salvar...
	 */
	public void salvar(View v) {
		Funcionario fun = new Funcionario();
		fun.setNome(nome.getText().toString());
		// Convertemos a String para Calendar
		Calendar data = DaoFuncionario.formataStringParaCalendar(nascimento.getText().toString());
		fun.setNascimento(data);
		fun.setEmail(email.getText().toString());
		fun.setTelefone(telefone.getText().toString());
		
		if (funView == null) {
			// inserimos
			long id = dao.insert(fun);
			if (id > 0) {
				Toast.makeText(CadastroFuncionarioActivity.this, "Salvo com Sucesso", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(CadastroFuncionarioActivity.this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
			}
		} else {
			// alteramos
			fun.setId(funView.getId());
			if (dao.update(fun)) {
				Toast.makeText(CadastroFuncionarioActivity.this, "Alterado com Sucesso", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(CadastroFuncionarioActivity.this, "Erro ao alterar!", Toast.LENGTH_SHORT).show();
			}
		}
	}
}