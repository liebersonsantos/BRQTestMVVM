package br.com.brq.personregistry.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "person")
@Parcelize
data class Person(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = null,
    var cpf: String? = null,
    var address: Address? = Address(),
    var birthDay: String? = null
) : Parcelable