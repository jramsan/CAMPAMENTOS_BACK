package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "convocatoria")
public class Convocatoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_convocatoria", nullable = false, length = 150)
    private String nombreConvocatoria;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    // Join con campamentos_convocatoria
    @ManyToMany
    @JoinTable(
        name = "campamentos_convocatoria",
        joinColumns = @JoinColumn(name = "convocatoria_id"),
        inverseJoinColumns = @JoinColumn(name = "campamento_id")
    )
    @JsonIgnore
    private Set<Campamento> campamentos;

    public Convocatoria() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombreConvocatoria() { return nombreConvocatoria; }
    public void setNombreConvocatoria(String nombreConvocatoria) { this.nombreConvocatoria = nombreConvocatoria; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public Set<Campamento> getCampamentos() { return campamentos; }
    public void setCampamentos(Set<Campamento> campamentos) { this.campamentos = campamentos; }
}
