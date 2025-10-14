package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "solicitud")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    // tinyint(1) en MySQL -> Boolean en Java
    @Column(name = "id_adjudicada")
    private Boolean adjudicada = Boolean.FALSE;

    @ManyToOne(optional = false)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Solicitante solicitante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "convocatoria_id", nullable = false)
    private Convocatoria convocatoria;

    // Join table campamentos_solicitud
    @ManyToMany
    @JoinTable(
        name = "campamentos_solicitud",
        joinColumns = @JoinColumn(name = "solicitud_id"),
        inverseJoinColumns = @JoinColumn(name = "campamento_id")
    )
    @JsonIgnore
    private Set<Campamento> campamentos;

    public Solicitud() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDate fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public Boolean getAdjudicada() { return adjudicada; }
    public void setAdjudicada(Boolean adjudicada) { this.adjudicada = adjudicada; }
    public Solicitante getSolicitante() { return solicitante; }
    public void setSolicitante(Solicitante solicitante) { this.solicitante = solicitante; }
    public Convocatoria getConvocatoria() { return convocatoria; }
    public void setConvocatoria(Convocatoria convocatoria) { this.convocatoria = convocatoria; }
    public Set<Campamento> getCampamentos() { return campamentos; }
    public void setCampamentos(Set<Campamento> campamentos) { this.campamentos = campamentos; }
}
