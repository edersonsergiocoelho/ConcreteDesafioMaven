package br.com.concrete.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.concrete.bean.Phone;
import br.com.concrete.bean.Usuario;
import br.com.concrete.repository.PhoneRepository;
import br.com.concrete.service.PhoneService;

@Service
public class PhoneServiceImpl implements PhoneService {

	@Autowired
	PhoneRepository phoneRepository;

	final static Logger logger = Logger.getLogger(PhoneServiceImpl.class);
	final static Long TIME_OUT_SESSAO_USUARIO = 1L;

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<Phone> buscaListaPhone(Usuario usuario) {
		
		List<Phone> listaPhone = phoneRepository.buscaListaPhone(usuario);
		
		return listaPhone;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void createPhones(List<Phone> listaPhone) {
		phoneRepository.createPhones(listaPhone);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void deletePhones(List<Phone> listaPhone) {
		phoneRepository.deletePhones(listaPhone);
	}
}
