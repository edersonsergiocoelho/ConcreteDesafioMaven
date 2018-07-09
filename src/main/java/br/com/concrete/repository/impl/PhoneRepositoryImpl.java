package br.com.concrete.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.concrete.bean.Phone;
import br.com.concrete.bean.Usuario;
import br.com.concrete.repository.PhoneRepository;
import br.com.concrete.repository.generic.GenericRepositoryJPAImp;

@Repository("PhoneRepository")
public class PhoneRepositoryImpl extends GenericRepositoryJPAImp<Phone, Integer> implements PhoneRepository {

	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.MANDATORY)
	@Override
	public List<Phone> buscaListaPhone(Usuario usuario) {
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("usuarioIduser", usuario));
		return (List<Phone>) criteria.list();
	}
	
	@Transactional(propagation=Propagation.MANDATORY)
	public void createPhones(List<Phone> listaPhone) {

		for (Phone phone : listaPhone) {
			create(phone);
		}
		getSession().flush();
		getSession().clear();
	}
	
	@Transactional(propagation=Propagation.MANDATORY)
	public void deletePhones(List<Phone> listaPhone) {
		
		for (Phone phone : listaPhone) {
			delete(phone);
		}
		getSession().flush();
		getSession().clear();
	}
}

