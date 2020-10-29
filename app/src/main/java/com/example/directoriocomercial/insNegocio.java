package com.example.directoriocomercial;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class insNegocio extends Fragment implements CheckBox.OnClickListener {

    CheckBox sociales;
    //int id_propietario[] = {R.id.edt_nombreUsuario,R.id.edt_telefonoUsuario,R.id.edt_emailUsuario,R.id.edt_fechaNacUsuario,R.id.edt_faceUsuario};
    int id_negocio[] = {R.id.edt_denominacion,R.id.edt_giro,R.id.edt_descripcion,R.id.edt_productos};
    int id_domicilio[] = {R.id.edt_calle,R.id.edt_noInt,R.id.edt_noExt,R.id.edt_colonia,R.id.edt_codigoPos,R.id.edt_municipio,R.id.edt_estado};
    int id_contacto[] = {R.id.edt_emailNeg,R.id.edt_telefonoNeg,R.id.edt_horario};
    EditText pagina,faceN,instaN;
    //EditText propietario[] = new EditText[5];
    EditText negocio[] = new EditText[4];
    EditText domicilio[] = new EditText[7];
    EditText contacto[] = new EditText[3];
    Button inscr;

    //URL para google maps del negocio
    //https://maps.google.com/?q=
    public insNegocio() {
        // Required empty public constructor
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ins_negocio, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_inscribir));
        //for (int i = 0; i<5; i++)
            //propietario[i] = (EditText) rootView.findViewById(id_propietario[i]);
        for (int i = 0; i<4; i++)
            negocio[i] = (EditText) rootView.findViewById(id_negocio[i]);
        for (int i = 0; i<7; i++)
            domicilio[i] = (EditText) rootView.findViewById(id_domicilio[i]);
        for (int i = 0; i<3; i++)
            contacto[i] = (EditText) rootView.findViewById(id_contacto[i]);
        sociales = (CheckBox)rootView.findViewById(R.id.chk_redes);
        pagina = (EditText)rootView.findViewById(R.id.edt_paginaW);
        faceN = (EditText)rootView.findViewById(R.id.edt_faceNeg);
        instaN = (EditText)rootView.findViewById(R.id.edt_instaNeg);
        inscr = (Button)rootView.findViewById(R.id.btn_inscribir);
        inscr.setOnClickListener(this);
        sociales.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        //Propietario
        //String nombre,telefono,email,fecha,face;
        //Negocio
        String denom,giro,descrip,producto;
        //Domicilio
        String calle,noI,noE,colonia,codigo,munic,estado;
        //Contacto
        String emailN,telefonoN,horario,page,faceNe,instaNe;

        if(v.getId()==R.id.chk_redes){
            if(sociales.isChecked()) {
                pagina.setVisibility(View.VISIBLE);
                faceN.setVisibility(View.VISIBLE);
                instaN.setVisibility(View.VISIBLE);
            }
            else {
                pagina.setVisibility(View.INVISIBLE);
                faceN.setVisibility(View.INVISIBLE);
                instaN.setVisibility(View.INVISIBLE);
            }
        }
        if(v.getId() == R.id.btn_inscribir){
            /*nombre = propietario[0].getText().toString();
            telefono = propietario[1].getText().toString();
            email = propietario[2].getText().toString();
            fecha = propietario[3].getText().toString();
            face = propietario[4].getText().toString();*/

            denom = negocio[0].getText().toString();
            giro = negocio[1].getText().toString();
            descrip = negocio[2].getText().toString();
            producto = negocio[3].getText().toString();

            calle = domicilio[0].getText().toString();
            noI = domicilio[1].getText().toString();
            noE = domicilio[2].getText().toString();
            colonia = domicilio[3].getText().toString();
            codigo = domicilio[4].getText().toString();
            munic = domicilio[5].getText().toString();
            estado = domicilio[6].getText().toString();

            emailN = contacto[0].getText().toString();
            telefonoN = contacto[1].getText().toString();
            horario = contacto[2].getText().toString();

            page = pagina.getText().toString();
            faceNe = faceN.getText().toString();
            instaNe = instaN.getText().toString();

            if(!denom.isEmpty() && !giro.isEmpty() &&
            !calle.isEmpty() && !noI.isEmpty() && !colonia.isEmpty() && !codigo.isEmpty() && !munic.isEmpty() && !estado.isEmpty() &&
            !emailN.isEmpty() && !telefonoN.isEmpty()){

                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "Datos enviados. Se te notificará cuando este aceptado tu negocio.",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getContext(), "Faltan campos por llenar",Toast.LENGTH_LONG).show();
            }
        }
    }


}
