package br.com.brq.personregistry.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.brq.personregistry.R
import br.com.brq.personregistry.model.Address
import br.com.brq.personregistry.model.Person
import br.com.brq.personregistry.util.Mask
import br.com.brq.personregistry.viewmodel.PersonViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val viewModel: PersonViewModel by lazy { ViewModelProviders.of(this).get(PersonViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editCep.addTextChangedListener(Mask.mask(editCep, Mask.FORMAT_CEP))
        editCpf.addTextChangedListener(Mask.mask(editCpf, Mask.FORMAT_CPF))
        editBirthday.addTextChangedListener(Mask.mask(editBirthday, Mask.FORMAT_DATE))

        editCep.setOnEditorActionListener { textView, i, keyEvent ->
            val cep = Mask.unmask(textView.text.toString())
            viewModel.getAddress(cep)
            return@setOnEditorActionListener false
        }

        viewModel.cepResponse.observe(this, Observer {
            editCity.setText(it.city)
            editNeighborhood.setText(it.neighborhood)
            editStreet.setText(it.street)
            editState.setText(it.state)
        })

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
                //                if (personSelected != null) {
//                    person.id = personSelected.id
//                    viewModel.updatePerson(person)
//                } else {
                viewModel.savePerson(person)
//                }
            }.start()

//        btnDelete.setOnClickListener {
//            Thread {
//                viewModel.deletePerson(personSelected)
//            }.start()


        }
    }
}