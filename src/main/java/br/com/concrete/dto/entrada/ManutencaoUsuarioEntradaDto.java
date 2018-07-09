package br.com.concrete.dto.entrada;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.concrete.model.PhoneModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class ManutencaoUsuarioEntradaDto {

	private Integer idUser;
    private String name;
    private String email;
    private String password;
    private List<PhoneModel> phones;
	
    public Integer getIdUser() {
		return idUser;
	}
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<PhoneModel> getPhones() {
		return phones;
	}
	public void setPhones(List<PhoneModel> phones) {
		this.phones = phones;
	}
}
