package br.com.concrete.service;

import java.util.List;

import br.com.concrete.bean.Phone;
import br.com.concrete.bean.Usuario;

public interface PhoneService {

	List<Phone> buscaListaPhone(Usuario usuario);

	void createPhones(List<Phone> listaPhone);

	void deletePhones(List<Phone> listaPhone);

}
