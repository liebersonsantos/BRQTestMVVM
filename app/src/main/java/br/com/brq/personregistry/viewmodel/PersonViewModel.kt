package br.com.brq.personregistry.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.brq.personregistry.PersonRepository
import br.com.brq.personregistry.model.Address
import br.com.brq.personregistry.model.Person
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    private val disposable = CompositeDisposable()
    val cepResponse: MutableLiveData<Address> = MutableLiveData()
    val personResponse: MutableLiveData<List<Person>> = MutableLiveData()

    private val repository = PersonRepository(getApplication())

    fun savePerson(person: Person) {
        disposable.add(
            repository.insert(person)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("Log", "Item salvo")
                    getAll()
                }, {
                    Log.i("Log", "Error : ${it.message}")
                })
        )
    }

    fun updatePerson(person: Person) {
        disposable.add(
            repository.update(person)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("Log", "Item alterado")
                    getAll()
                }, {
                    Log.i("Log", "Error : ${it.message}")
                })
        )
    }

    fun deletePerson(person: Person?) {
        person?.id?.let {
            disposable.add(
                repository.delete(person)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.i("Log", "Item deletado")
                        getAll()
                    }, {
                        Log.i("Log", "Error : ${it.message}")
                    })
            )
        }
    }

    fun getAll() {
        disposable.add(
            repository.getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    personResponse.value = response
                }, {
                    Log.i("Log", "Error : ${it.message}")
                })
        )
    }

    fun getAddress(cep: String) {
        disposable.add(
            repository.getAddress(cep)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ address ->
                    cepResponse.value = address
                }, {
                    Log.i("Log", "Error : ${it.message}")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}