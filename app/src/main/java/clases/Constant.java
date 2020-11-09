package clases;

public class Constant {
    //Raíz
    public static final String URL = "http://192.168.0.19/";
    public static final String HOME = URL + "directorio-ca/public";
    public static final String API = HOME + "/api";

    //Login y suscribirse
    public static final String USERS = API + "/login";
    public static final String USERS_LOGAOUT = API + "/logout";
    public static final String REGISTRAR = API + "/registro";

    //Negocios
    public static final String INSCRIBIR = API + "/negocios/create";
    public static final String NEGOCIO = API + "/directorio";
    public static final String COMENTARIOS = API + "/comentarios";
    public static final String PUBLICAR_COMENTARIO = API + "/publicar_comentario";

    //Actualizar tablas
    public static final String ACTUALIZAR_NEGOCIO = API + "/directorio";
    public static final String ACTUALIZAR_DIRECCION = API + "/directorio_direccion";
    public static final String ACTUALIZAR_CONTACTO = API + "/directorio_direccion";

    //Eventos
    public static final String EVENTOS = API + "/eventos";
    public static final String REGISTRAR_EVENTO = API + "/registro_evento";

    //Ayudanos
    public static final String AYUDANOS_LOGIN = API + "/opiniones";
    public static final String AYUDANOS_SIN_LOGIN = API + "/opiniones";

    //Mis negocios
    public static final String MIS_NEGOCIOS = API + "/usuarios/";
}
