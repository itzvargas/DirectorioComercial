package clases;

public class Constant {
    //Raíz
    public static final String URL = "http://192.168.0.12/";
    public static final String HOME = URL + "directorio-ca/public";
    public static final String API = HOME + "/api";

    //Login y suscribirse
    public static final String USERS = API + "/login";
    public static final String REGISTRAR = HOME + "/registro";

    //Negocios
    public static final String INSCRIBIR = HOME + "/inscribirNegocio";
    public static final String NEGOCIO = HOME + "/negocio";
    public static final String COMENTARIOS = HOME + "/comentarios";
    public static final String PUBLICAR_COMENTARIO = HOME + "/publicar_comentario";

    //Actualizar tablas
    public static final String ACTUALIZAR_NEGOCIO = HOME + "/negocios";
    public static final String ACTUALIZAR_DIRECCION = HOME + "/direccion_negocios";
    public static final String ACTUALIZAR_CONTACTO = HOME + "/contacto_negocios";
}
