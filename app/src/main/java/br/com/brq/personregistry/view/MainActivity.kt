package br.com.brq.personregistry.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.brq.personregistry.R
import br.com.brq.personregistry.adapters.PersonAdapter
import br.com.brq.personregistry.model.Person
import br.com.brq.personregistry.viewmodel.PersonViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val adapter: PersonAdapter = PersonAdapter(ArrayList(), this::clickListener)
    private val viewModel: PersonViewModel by lazy { ViewModelProviders.of(this).get(PersonViewModel::class.java) }

    companion object {
        const val PERSON = "PERSON"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recycleview_person.layoutManager = LinearLayoutManager(this)
        recycleview_person.adapter = adapter

        // Mostra o dialog para inserir/editar/deletar uma pessoa
        fab.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //Observa a lista de pessoas quando muda atualiza a lista do recycler view
        viewModel.personResponse.observe(this, Observer { listPerson ->
            adapter.update(listPerson)
            swipetorefresh.isRefreshing = false
        })

        viewModel.isLoading.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })


        swipetorefresh.setOnRefreshListener {
            viewModel.getAll()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }

    private fun clickListener(person: Person) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra(PERSON, person)
        startActivity(intent)
    }
}
