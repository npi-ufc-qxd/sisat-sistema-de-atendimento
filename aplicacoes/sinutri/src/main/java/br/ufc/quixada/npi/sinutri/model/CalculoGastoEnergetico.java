package br.ufc.quixada.npi.sinutri.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CalculoGastoEnergetico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double pesoSugerido;
	private Double vet;
	private Double ajuste;
	private Double lipidio;
	private Double proteina;
	private Double glicidio;
	private Double totalCalorias;
	
	@ManyToOne
	private Servidor nutricionista;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<DistribuicaoAlimentar> distribuicoesAlimentares;
	
	@ManyToOne
	private Paciente paciente;
	
	public Double getPesoSugerido() {
		return pesoSugerido;
	}
	public void setPesoSugerido(Double pesoSugerido) {
		this.pesoSugerido = pesoSugerido;
	}
	public Double getVet() {
		return vet;
	}
	public void setVet(Double vet) {
		this.vet = vet;
	}
	public Double getAjuste() {
		return ajuste;
	}
	public void setAjuste(Double ajuste) {
		this.ajuste = ajuste;
	}
	public Double getLipidio() {
		return lipidio;
	}
	public void setLipidio(Double lipidio) {
		this.lipidio = lipidio;
	}
	public Double getProteina() {
		return proteina;
	}
	public void setProteina(Double proteina) {
		this.proteina = proteina;
	}
	public Double getGlicidio() {
		return glicidio;
	}
	public void setGlicidio(Double glicidio) {
		this.glicidio = glicidio;
	}
	public Double getTotalCalorias() {
		return totalCalorias;
	}
	public void setTotalCalorias(Double totalCalorias) {
		this.totalCalorias = totalCalorias;
	}
	public Servidor getNutricionista() {
		return nutricionista;
	}
	public void setNutricionista(Servidor nutricionista) {
		this.nutricionista = nutricionista;
	}
	public List<DistribuicaoAlimentar> getDistribuicoesAlimentares() {
		return distribuicoesAlimentares;
	}
	public void setDistribuicoesAlimentares(
			List<DistribuicaoAlimentar> distribuicoesAlimentares) {
		this.distribuicoesAlimentares = distribuicoesAlimentares;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}