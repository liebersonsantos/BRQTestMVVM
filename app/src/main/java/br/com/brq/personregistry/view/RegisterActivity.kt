package br.com.brq.personregistry.view

import android.location.Geocoder
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.brq.personregistry.R
import br.com.brq.personregistry.model.Person
import br.com.brq.personregistry.util.Mask
import br.com.brq.personregistry.viewmodel.PersonViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_register.*
import java.io.IOException

class RegisterActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: PersonViewModel by lazy { ViewModelProviders.of(this).get(PersonViewModel::class.java) }
    private lateinit var map: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        person = intent.getParcelableExtra(MainActivity.PERSON)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.view?.visibility = GONE

        mapFragment.getMapAsync(this)
        editCep.addTextChangedListener(Mask.mask(editCep, Mask.FORMAT_CEP))
        editCpf.addTextChangedListener(Mask.mask(editCpf, Mask.FORMAT_CPF))
        editBirthday.addTextChangedListener(Mask.mask(editBirthday, Mask.FORMAT_DATE))

        editCep.setOnEditorActionListener { textView, i, keyEvent ->
            val cep = Mask.unmask(textView.text.toString())
            viewModel.getAddress(cep)
            return@setOnEditorActionListener false
        }

        editStreet.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val cep = Mask.unmask(editCep.text.toString())
                viewModel.getAddress(cep)
            }
        }

        // Se a pessoa chegou aqui da outra tela preencho os campos com os dados
        person?.let {
            btnDelete.visibility = VISIBLE

            editCep.setText(it.address?.cep)
            editStreet.setText(it.address?.street)
            editNeighborhood.setText(it.address?.neighborhood)
            editNumber.setText(it.address?.number)
            editCity.setText(it.address?.city)
            editState.setText(it.address?.state)
            editName.setText(it.name)
            editCpf.setText(it.cpf)
            editBirthday.setText(it.birthDay)
            btnRegistry.text = "Atualizar"

            btnDelete.setOnClickListener {
                viewModel.deletePerson(person)
                finish()
            }
        }

        btnRegistry.setOnClickListener {

            if (person == null) {
                person = Person()
            }

            person?.address?.cep = editCep.text.toString()
            person?.address?.street = editStreet.text.toString()
            person?.address?.neighborhood = editNeighborhood.text.toString()
            person?.address?.number = editNumber.text.toString()
            person?.address?.city = editCity.text.toString()
            person?.address?.state = editState.text.toString()

            person?.name = editName.text.toString()
            person?.cpf = editCpf.text.toString()
            person?.birthDay = editBirthday.text.toString()


            if (person?.id == null) {
                viewModel.savePerson(person)
            } else {
                viewModel.updatePerson(person)
            }

            finish()
        }

        viewModel.cepResponse.observe(this, Observer {
            editCity.setText(it.city)
            editNeighborhood.setText(it.neighborhood)
            editStreet.setText(it.street)
            editState.setText(it.state)
        })

        viewModel.isLoading.observe(this, Observer {
            if (it) {
                progressBar.visibility = VISIBLE
            } else {
                progressBar.visibility = GONE
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        person?.let {
            mapFragment.view?.visibility = VISIBLE
            btnDelete.visibility = VISIBLE
            val mapAddress = getLocationFromAddress("${it.address?.street}, ${it.address?.number}, ${it.address?.neighborhood}, ${it.address?.city}")

            mapAddress?.let {
                map.addMarker(MarkerOptions().position(mapAddress).title("Minha casa :)"))
                map.moveCamera(CameraUpdateFactory.newLatLng(mapAddress))
            }
        }
    }

    private fun getLocationFromAddress(strAddress: String): LatLng? {

        val coder = Geocoder(this)
        val address: List<android.location.Address>?
        var latLng: LatLng? = null

        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null || address.isEmpty()) {
                return null
            }

            val location = address[0]
            latLng = LatLng(location.latitude, location.longitude)

        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return latLng
    }
}