package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "campamento")
public class Campamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    // enum('Nacional','Internacional') en DB
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Destino destino;

    public Campamento() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Destino getDestino() { return destino; }
    public void setDestino(Destino destino) { this.destino = destino; }
}
