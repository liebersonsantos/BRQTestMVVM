package br.com.brq.personregistry.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.brq.personregistry.R
import br.com.brq.personregistry.adapters.PersonAdapter
import br.com.brq.personregistry.model.Address
import br.com.brq.personregistry.model.Person
import br.com.brq.personregistry.util.Mask
import br.com.brq.personregistry.util.Mask.unmask
import br.com.brq.personregistry.viewmodel.PersonViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    private val adapter: PersonAdapter = PersonAdapter(ArrayList(), this::clickListener)
    private val viewModel: PersonViewModel by lazy { ViewModelProviders.of(this).get(PersonViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recycleview_person.layoutManager = LinearLayoutManager(this)
        recycleview_person.adapter = adapter

        // Mostra o dialog para inserir/editar/deletar uma pessoa
        fab.setOnClickListener {
            showInsertDialog()
        }

        //Observa a lista de pessoas quando muda atualisa a lista do recycler view
        // Chamada para buscar a lista d epessoas
        viewModel.personResponse.observe(this, Observer { listPerson ->
            Log.i("LOG", "LIsta de pessoas: $listPerson")
            adapter.update(listPerson)
            swipetorefresh.isRefreshing = false
        })

        swipetorefresh.setOnRefreshListener {
            viewModel.getAll()
        }

        viewModel.getAll()
    }


    private fun showInsertDialog(personSelected: Person? = null) {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_insert_person, null)
        setViewsDialog(view, personSelected)
        builder.setView(view)
        dialog = builder.create()
        dialog.show()
    }

    private fun setViewsDialog(view: View, personSelected: Person? = null) {
        val editName = view.findViewById<TextInputEditText>(R.id.edit_name)
        val editCpf = view.findViewById<TextInputEditText>(R.id.edit_cpf)
        val editCep = view.findViewById<TextInputEditText>(R.id.edit_cep)
        val editStreet = view.findViewById<TextInputEditText>(R.id.edit_street)
        val editNeighborhood = view.findViewById<TextInputEditText>(R.id.edit_neighborhood)
        val editNumber = view.findViewById<TextInputEditText>(R.id.edit_number)
        val editCity = view.findViewById<TextInputEditText>(R.id.edit_city)
        val editState = view.findViewById<TextInputEditText>(R.id.edit_state)
        val editBirthday = view.findViewById<TextInputEditText>(R.id.edit_birthday)
        val btnRegistry = view.findViewById<Button>(R.id.btn_registry)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete)

        editCep.addTextChangedListener(Mask.mask(editCep, Mask.FORMAT_CEP))
        editCpf.addTextChangedListener(Mask.mask(editCpf, Mask.FORMAT_CPF))
        editBirthday.addTextChangedListener(Mask.mask(editBirthday, Mask.FORMAT_DATE))


        editCep.setOnEditorActionListener { textView, i, keyEvent ->
            val cep = unmask(textView.text.toString())
            viewModel.getAddress(cep)
            return@setOnEditorActionListener false
        }

       /* editCep.setOnFocusChangeListener { _, b ->
            if (!b){
                val cep = unmask(editCep.text.toString())
                viewModel.getAddress(cep)
            }
        }*/

        viewModel.cepResponse.observe(this, Observer {
            editCity.setText(it.city)
            editNeighborhood.setText(it.neighborhood)
            editStreet.setText(it.street)
            editState.setText(it.state)
        })

        if (personSelected != null) {
            editName.setText(personSelected.name)
            editCpf.setText(personSelected.cpf)
            editCep.setText(personSelected.address?.cep)
            editStreet.setText(personSelected.address?.street)
            editNeighborhood.setText(personSelected.address?.neighborhood)
            editNumber.setText(personSelected.address?.number)
            editCity.setText(personSelected.address?.city)
            editState.setText(personSelected.address?.state)
            editBirthday.setText(personSelected.birthDay)
        }

        val person = Person()
        val address = Address()

        btnRegistry.setOnClickListener {

            address.cep = editCep.text.toString()
            address.street = editStreet.text.toString()
            address.neighborhood = editNeighborhood.text.toString()
            address.number = editNumber.text.toString()
            address.city = editCity.text.toString()
            address.state = editState.text.toString()

            person.name = editName.text.toString()
            person.cpf = editCpf.text.toString()
            person.birthDay = editBirthday.text.toString()
            person.address = address

            Thread {
                if (personSelected != null) {
                    person.id = personSelected.id
                    viewModel.updatePerson(person)
                } else {
                    viewModel.savePerson(person)
                }
            }.start()

            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            Thread {
                viewModel.deletePerson(personSelected)
            }.start()

            dialog.dismiss()
        }
    }

    private fun clickListener(person: Person) {
        showInsertDialog(person)
    }
}
