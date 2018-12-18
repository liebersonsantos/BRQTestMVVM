package br.com.brq.personregistry

import android.content.Context
import androidx.lifecycle.LiveData
import br.com.brq.personregistry.data.database.DatabaseRoom
import br.com.brq.personregistry.data.netWork.RetrofitService
import br.com.brq.personregistry.interfaces.PersonDAO
import br.com.brq.personregistry.model.Address
import br.com.brq.personregistry.model.Person
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

class PersonRepository(context: Context) {

    private val personDAO: PersonDAO by lazy {
        DatabaseRoom.getDatabase(context).personDAO()
    }

    fun getAddress(cep: String): Single<Address> {
        return RetrofitService().getApiService().getAddress(cep)
    }

    fun insert(person: Person?): Observable<Unit> {

        return Observable.fromCallable { personDAO.insert(person) }
    }

    fun update(person: Person?): Observable<Unit> {
        return Observable.fromCallable { personDAO.update(person) }
    }

    fun delete(person: Person): Observable<Unit> {
        return Observable.fromCallable { personDAO.delete(person) }
    }

    fun getAll(): Observable<List<Person>> {
        return personDAO.getAll()
    }

    fun getAllPerson(): LiveData<List<Person>> {
        return personDAO.getAllPerson()
    }

    fun getByName(name: String?): Observable<Person?> {
        return Observable.fromCallable { personDAO.getByName(name) }
    }
}