package com.aamaldonado.viaje.seguro.utpl.tft.common;

public final class Constants {

    private Constants(){}
    public static final int LOCATION_SERVICE_ID = 175;
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";
    public static final  int REQUEST_CODA_LOCATION_PERMISSION = 1;

    public static final String USER_ID = "user_id";
    public static final String USER_DATA_REF = "user_data";
    public static final String BUS_DATA_REF = "reports";
    public static final String CHILD_REPORTS = "exceso_reports";
    public static final String CHILD_MOVIMEINTO = "report_movimiento";
    public static final String CHILD_PERSONAL_REPORTS = "personal_reports";
    public static final String ID_BUS = "idbus";
    public static final String ID_VIAJE = "idviaje";
    public static final String ID_COMPANIA = "idCompania";
    public static final String FECHA = "fecha";
    public static final String COORDENADAS = "coordenadas";
    public static final String DB_LAT = "lat";
    public static final String DB_LNG = "lng";
    public static final String DB_SPEED = "speed";
    public static final String DB_ID_USUARIO = "idUsuario";
    public static final String DB_ID_EXCESO = "idExceso";
    public static final String DB_CHECK_EXCESO = "checkExceso";
    public static final String SEPARATE_CHARACTER = "-";
    public static final String ALERT_PANIC_DATA = "alert_panic_data";
    public static final String TAG1 = "TAG1";
    public static final String MENSAJE_ERROR_BDD = "Error al leer valores de la bdd.";
    public static final String FORMATO_DATE_HORA = "dd-MM-yyyy HH:mm";
    public static final String FORMATO_DATE = "dd-MM-yyyy";
    public static final String GUIA_REPORTE = "Sección para crear un reporte personalizado";
    public static final String GUIA_MAPA = "Sección para visualizar tu ubicación actual";
    public static final String GUIA_EXCESOS = "Sección donde se registran los excesos de velocidad del transporte";
    public static final String GUIA_COUNTER = "Contador de excesos de velocidad";
    public static final String GUIA_QR = "Sección para registrar un nuevo transporte";
    public static final String GUIA_INFO = "Sección donde se muestra la información de la ubicación actual";
    public static final String GUIA_ALERTA = "Botón de alerta prioritaria";
    public static final String GUIA_VELOCIDAD = "Velocidad actual";
}
