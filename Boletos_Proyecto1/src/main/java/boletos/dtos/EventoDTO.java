package boletos.dtos;

import java.sql.Timestamp;

/**
 *
 * @author gael_
 */
public class EventoDTO {

    private Integer idEvento;
    private String nombre;
    private Timestamp fecha;
    private String recinto;
    private String ciudad;
    private String estado;
    private String descripcion;

    public EventoDTO(Integer idEvento, String nombre, Timestamp fecha, String recinto, String ciudad, String estado, String descripcion) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.fecha = fecha;
        this.recinto = recinto;
        this.ciudad = ciudad;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public String getRecinto() {
        return recinto;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
