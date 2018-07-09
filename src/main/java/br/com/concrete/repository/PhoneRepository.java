package br.com.concrete.repository;

import java.util.List;

import br.com.concrete.bean.Phone;
import br.com.concrete.bean.Usuario;
import br.com.concrete.repository.generic.GenericRepositoryJPA;

public interface PhoneRepository extends GenericRepositoryJPA<Phone, Integer>  {

	List<Phone> buscaListaPhone(Usuario usuario);
	
	void createPhones(List<Phone> listaPhone);
	
	void deletePhones(List<Phone> listaPhone);
}
