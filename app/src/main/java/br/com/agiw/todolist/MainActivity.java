package br.com.agiw.todolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private SQLiteDatabase bancoDados;
    private ArrayAdapter<String> tarefasAdaptador;
    private ArrayList<String> tarefas;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadViews();
        loadDB();
        loadActions();

    }//end protected void onCreate

    private void loadViews(){
        mViewHolder.etTarefa     = (EditText) findViewById(R.id.et_descTarefa);
        mViewHolder.btnAdicionar = (Button) findViewById(R.id.btn_addTarefa);
        mViewHolder.lvTarefas    = (ListView) findViewById(R.id.lv_tarefas);
    }//end private void loadViews()


    private void loadActions(){
        mViewHolder.btnAdicionar.setOnClickListener(this);
        //mViewHolder.lvTarefas.setOnItemClickListener(this);
        mViewHolder.lvTarefas.setLongClickable(true);
        mViewHolder.lvTarefas.setOnItemLongClickListener(this);
        loadList();
    }//end loadActions()

    private void loadDB(){
        try {
            bancoDados = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS Tarefas (id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR )");
        } catch (Exception e){
            e.printStackTrace();
        }//end try catch (Exception e)

    }//end private void loadDB()


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_addTarefa){
            salvarTarefa(mViewHolder.etTarefa.getText().toString());
            mViewHolder.etTarefa.setText("");
        }//end if (id = R.id.btn_addTarefa)

    }//end public void onClick(View v)

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        removerTarefa(ids.get( position));

    }//end public void onItemClick*/

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        removerTarefa( ids.get( position ) );
        return false;
    }//end public boolean onItemLongClick



    private void loadList(){
        try {
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM Tarefas ORDER BY id DESC", null);

            int indiceColId = cursor.getColumnIndex("id");
            int indiceColTarefa = cursor.getColumnIndex("tarefa");

            tarefas = new ArrayList<String>();
            ids = new ArrayList<Integer>();

            tarefasAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    tarefas);

            mViewHolder.lvTarefas.setAdapter(tarefasAdaptador);

            cursor.moveToFirst();

            while (cursor != null){

                tarefas.add(cursor.getString(indiceColTarefa));
                ids.add(Integer.parseInt(cursor.getString(indiceColId)));

                cursor.moveToNext();
            }//end while (cursor != null)
        }catch (Exception e){
            e.printStackTrace();
        }//end try catch (Exception e)
    }//end private void loadList()


    private void salvarTarefa(String tarefa){
        try {
            if(tarefa.equals("")){
                Toast.makeText(this, "Digite uma tarefa!", Toast.LENGTH_SHORT).show();
            }else {
                bancoDados.execSQL("INSERT INTO Tarefas (tarefa) values ('" + tarefa + "')");
                Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
                loadList();
            }

        } catch (Exception e){
            e.printStackTrace();
        }//end try catch (Exception e)

    }//end private void salvarTarefa()


    private void removerTarefa(Integer id){
        try{
            bancoDados.execSQL("DELETE FROM Tarefas WHERE id = " + id );
            Toast.makeText(this, "Tarefa deletada com sucesso!", Toast.LENGTH_SHORT).show();
            loadList();
        } catch (Exception e){
            e.printStackTrace();
        }//end try catch (Exception e)
    }//end private void removerTarefa()


    private static class  ViewHolder {
        EditText etTarefa;
        Button btnAdicionar;
        ListView lvTarefas;
    }//end private static class  ViewHolder


}//end public class MainActivity