package clases;

public class Constant {
    //Raíz
    //Itz 17 Koce 12
    public static final String URL = "http://192.168.0.17/";
    public static final String HOME = URL + "directorio-ca/public";
    public static final String API = HOME + "/api";

    //Descarga Directorio
    public static final String URL_DESCARGA = API + "/descargarDirectorio";
    public static final String DESCARGA = URL + "directorio-ca/storage/app/public";

    //Login y suscribirse
    public static final String USERS = API + "/login";
    public static final String USERS_LOGAOUT = API + "/logout";
    public static final String REGISTRAR = API + "/registro";

    //Usuarios
    public static final String PERFIL_USER = API + "/users/";
    public static final String EDITAR_USER = API + "/users/";

    //Negocios
    public static final String INSCRIBIR = API + "/negocios/create";
    public static final String NEGOCIO = API + "/directorio";
    public static final String NEGOCIO_INDIVIDUAL = API + "/negocios/";
    public static final String EDITAR_NEGOCIO = API + "/negocios/";

    //Comentarios
    public static final String COMENTARIOS = API + "/comentarios";
    public static final String OPINIONES = API + "/opinionNegocios/";

    //Actualizar tablas
    public static final String ACTUALIZAR_NEGOCIO = API + "/negocios";
    public static final String ACTUALIZAR_DIRECCION = API + "/direcciones";
    public static final String ACTUALIZAR_CONTACTO = API + "/contactos";

    //Eventos
    public static final String EVENTOS = API + "/eventos";
    public static final String REGISTRAR_EVENTO = API + "/registrate/";
    public static final String MOSTRAR_MIS_EVENTO = API + "/users/";

    //Ayudanos
    public static final String AYUDANOS_A_MEJORAR = API + "/buzonAyuda";

    //Mis negocios
    public static final String MIS_NEGOCIOS = API + "/users/";

    //Imagenes
    public static final String FOTO = URL + "directorio-ca/storage/app/public/";

    //Contactanos
    public static final String CONTACTANOS_DIRECTORIO = API + "/datosContacto";

    //Promociones
    public static final String CREAR_PROMO = API + "/negocios/create";
}
