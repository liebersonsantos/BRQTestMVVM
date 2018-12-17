package br.com.brq.personregistry.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.brq.personregistry.R
import br.com.brq.personregistry.model.Person

class PersonAdapter(var personList: List<Person>, val clickListener: (person: Person) -> Unit) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pego a pessoa na posição enviada
        val person = personList[position]

        //Preenche os dados da pessoa nas views
        holder.bind(person)

        //Seta a animação para cada item
        setAnimation(holder.itemView)

        //Vincula o evento de click do item e repassa para o listener
        holder.itemView.setOnClickListener {
            // Viculamos o click no item
            clickListener(person)
        }
    }

    // Set a animação em uma view
    private fun setAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left)
        view.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    fun update(listPerson: List<Person>) {
        this.personList = listPerson
        notifyDataSetChanged()

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Atributos para usar no preenchimento de dados
        private val textViewName: TextView = itemView.findViewById(R.id.textName)

        // Preenche os dados da pessoa
        fun bind(person: Person) {
            textViewName.text = person.name
        }
    }
}