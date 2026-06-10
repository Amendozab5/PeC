package com.example.pec

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class AlumnoAdapter(context: Context, private val alumnos: List<Alumno>) :
    ArrayAdapter<Alumno>(context, R.layout.item_alumno, alumnos) {

    private class ViewHolder(view: View) {
        val imgFoto: ImageView = view.findViewById(R.id.imgFoto)
        val txtNombres: TextView = view.findViewById(R.id.txtNombres)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreo)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefono)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_alumno, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val alumno = alumnos[position]

        // Set student details
        holder.txtNombres.text = alumno.nombres ?: ""
        holder.txtCorreo.text = alumno.correo ?: ""
        holder.txtTelefono.text = alumno.telefono ?: ""

        // Load image using Glide with circular transformation and default avatar placeholder
        Glide.with(context)
            .load(alumno.foto)
            .circleCrop()
            .placeholder(R.drawable.ic_avatar_placeholder)
            .error(R.drawable.ic_avatar_placeholder)
            .into(holder.imgFoto)

        return view
    }
}
