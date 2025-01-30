package com.example.proyectedb;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText editTextNombre, editTextComentario;
    private Button btnCrear, btnVer, borrarmemoria;
    private TextView textViewComentarios;
    private Spinner spinnerComentarios;
    private ArrayList<Comentario> listaComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextComentario = findViewById(R.id.editTextComentario);
        btnCrear = findViewById(R.id.btnCrear);
        btnVer = findViewById(R.id.btnVer);
        textViewComentarios = findViewById(R.id.textViewComentario);
        spinnerComentarios = findViewById(R.id.spinnerComentarios);
        borrarmemoria = findViewById(R.id.BorrarMemoria);

        btnCrear.setOnClickListener(v -> {
            String nombre = editTextNombre.getText().toString();
            String comentario = editTextComentario.getText().toString();
            if (!nombre.isEmpty() && !comentario.isEmpty()) {
                dbHelper.insertComentario(nombre, comentario);
                Toast.makeText(MainActivity.this, "Comentario agregado!", Toast.LENGTH_SHORT).show();

                editTextNombre.setText("");
                editTextComentario.setText("");

                updateSpinnerComentarios();
            } else {
                Toast.makeText(MainActivity.this, "Por favor, completa los campos.", Toast.LENGTH_SHORT).show();
            }
        });

        btnVer.setOnClickListener(v -> {
            int selectedPosition = spinnerComentarios.getSelectedItemPosition();
            if (selectedPosition != AdapterView.INVALID_POSITION && !listaComentarios.isEmpty()) {
                Comentario selectedComentario = listaComentarios.get(selectedPosition);

                textViewComentarios.setText("Título: "+selectedComentario.getNombre() +"\n"+"Comentario: " + selectedComentario.getComentario());
            } else {
                Toast.makeText(MainActivity.this, "Selecciona un comentario.", Toast.LENGTH_SHORT).show();
            }
        });



        borrarmemoria.setOnClickListener(v -> {
            int selectedPosition = spinnerComentarios.getSelectedItemPosition();
            if (selectedPosition != AdapterView.INVALID_POSITION && !listaComentarios.isEmpty()) {
                Comentario selectedComentario = listaComentarios.get(selectedPosition);

                dbHelper.deleteComentarioById(selectedComentario.getId());

                Toast.makeText(MainActivity.this, "Comentario eliminado.", Toast.LENGTH_SHORT).show();

                updateSpinnerComentarios();
                textViewComentarios.setText("");
            } else {
                Toast.makeText(MainActivity.this, "Selecciona un comentario para borrar.", Toast.LENGTH_SHORT).show();
            }
        });

        updateSpinnerComentarios();
    }

    private void updateSpinnerComentarios() {
        listaComentarios = dbHelper.getAllComentarios();
        ArrayList<String> comentariosNombres = new ArrayList<>();

        for (Comentario c : listaComentarios) {
            comentariosNombres.add(c.getNombre());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, comentariosNombres);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComentarios.setAdapter(spinnerAdapter);
    }
}
