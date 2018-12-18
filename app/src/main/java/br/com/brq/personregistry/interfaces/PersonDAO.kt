package br.com.brq.personregistry.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.brq.personregistry.model.Person
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface PersonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(person: Person?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(person: Person?)

    @Query("SELECT * FROM person")
    fun getAll(): Observable<List<Person>>

    @Query("SELECT * FROM person")
    fun getAllPerson(): LiveData<List<Person>>

    @Query("Select * from person where name = :namePerson")
    fun getByName(namePerson: String?): Person?

    @Query("Select * from person where id = :id")
    fun getById(id: Long): Person

    @Delete
    fun delete(person: Person?)
}