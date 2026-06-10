package com.example.pec

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val supabase by lazy {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Postgrest)
        }
    }

    private lateinit var spinnerSemestre: Spinner
    private lateinit var spinnerMaterias: Spinner
    private lateinit var listViewAlumnos: ListView

    private val semesters = listOf(
        "Seleccione Semestre", "Primero", "Segundo", "Tercero", "Cuarto", "Quinto",
        "Sexto", "Séptimo", "Octavo", "Noveno", "Décimo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerSemestre = findViewById(R.id.spinnerSemestre)
        spinnerMaterias = findViewById(R.id.spinnerMaterias)
        listViewAlumnos = findViewById(R.id.listViewAlumnos)

        setupSemesterSpinner()
        setupMateriaSpinnerListener()
    }

    private fun setupSemesterSpinner() {
        val semesterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, semesters)
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSemestre.adapter = semesterAdapter

        spinnerSemestre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    // Placeholder selected: clear everything
                    updateMateriasSpinner(emptyList())
                    listViewAlumnos.adapter = AlumnoAdapter(this@MainActivity, emptyList())
                } else {
                    val selectedLevel = position // Since "Primero" is at index 1, its level is 1
                    onSemesterSelected(selectedLevel)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupMateriaSpinnerListener() {
        spinnerMaterias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSemesterLevel = spinnerSemestre.selectedItemPosition // Index 6 represents "Sexto"
                val selectedMateria = spinnerMaterias.adapter?.getItem(position) as? String

                // Only load students if we are on 6th semester and have a valid subject selected (not loading/empty state)
                if (selectedSemesterLevel == 6 && selectedMateria != null && 
                    selectedMateria != "Sin materias" && selectedMateria != "Cargando materias...") {
                    fetchAlumnos()
                } else {
                    // Otherwise clear list
                    listViewAlumnos.adapter = AlumnoAdapter(this@MainActivity, emptyList())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun onSemesterSelected(level: Int) {
        // Clear list immediately on change
        listViewAlumnos.adapter = AlumnoAdapter(this, emptyList())
        // Show loading state in materias spinner
        updateMateriasSpinnerPlaceholder(listOf("Cargando materias..."))

        lifecycleScope.launch {
            try {
                val materiasList = withContext(Dispatchers.IO) {
                    supabase.from("materias").select {
                        filter {
                            eq("nivel", level)
                        }
                    }.decodeList<Materia>()
                }
                updateMateriasSpinner(materiasList)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error al cargar materias: ${e.message}", Toast.LENGTH_SHORT).show()
                updateMateriasSpinner(emptyList())
            }
        }
    }

    private fun updateMateriasSpinnerPlaceholder(items: List<String>) {
        val materiasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        materiasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMaterias.adapter = materiasAdapter
    }

    private fun updateMateriasSpinner(materias: List<Materia>) {
        val materiaNames = if (materias.isEmpty()) {
            listOf("Sin materias")
        } else {
            materias.map { it.nombre }
        }
        val materiasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, materiaNames)
        materiasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMaterias.adapter = materiasAdapter
    }

    private fun fetchAlumnos() {
        lifecycleScope.launch {
            try {
                val alumnosList = withContext(Dispatchers.IO) {
                    supabase.from("alumnos").select {
                        order(column = "nombres", order = Order.ASCENDING)
                    }.decodeList<Alumno>()
                }
                
                val adapter = AlumnoAdapter(this@MainActivity, alumnosList)
                listViewAlumnos.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error al cargar alumnos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}