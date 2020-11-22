package clases;

public class Constant {
    //Raíz
    public static final String URL = "http://192.168.0.12/";
    public static final String HOME = URL + "directorio-ca/public";
    public static final String API = HOME + "/api";

    //Login y suscribirse
    public static final String USERS = API + "/login";
    public static final String USERS_LOGAOUT = API + "/logout";
    public static final String REGISTRAR = API + "/registro";

    //Negocios
    public static final String INSCRIBIR = API + "/negocios/create";
    public static final String NEGOCIO = API + "/directorio";
    public static final String NEGOCIO_INDIVIDUAL = API + "/negocios/";
    public static final String EDITAR_NEGOCIO = API + "/negocios/";
    public static final String COMENTARIOS = API + "/comentarios";
    public static final String PUBLICAR_COMENTARIO = API + "/publicar_comentario";

    //Actualizar tablas
    public static final String ACTUALIZAR_NEGOCIO = API + "/negocios";
    public static final String ACTUALIZAR_DIRECCION = API + "/direcciones";
    public static final String ACTUALIZAR_CONTACTO = API + "/contactos";

    //Eventos
    public static final String EVENTOS = API + "/eventos";
    public static final String REGISTRAR_EVENTO = API + "/registrate/";
    public static final String MOSTRAR_MIS_EVENTO = API + "/usuarios/";

    //Ayudanos
    public static final String AYUDANOS_LOGIN = API + "/opiniones";
    public static final String AYUDANOS_SIN_LOGIN = API + "/opiniones";

    //Mis negocios
    public static final String MIS_NEGOCIOS = API + "/usuarios/";

    //Imagenes
    public static final String FOTO = URL + "directorio-ca/storage/app/public/";
}
