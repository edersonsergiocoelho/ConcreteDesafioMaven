package br.com.concrete.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.concrete.bean.Usuario;
import br.com.concrete.repository.UsuarioRepository;
import br.com.concrete.repository.generic.GenericRepositoryJPAImp;

@Repository("UsuarioRepository")
public class UsuarioRepositoryImpl extends GenericRepositoryJPAImp<Usuario, Integer> implements UsuarioRepository {

	@Override
	@Transactional(propagation=Propagation.MANDATORY)
	public Usuario buscaUsuarioPorCodigoUsuario(Integer codigoUsuario) {
		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("iduser", codigoUsuario));

		return (Usuario) criteria.uniqueResult();
	}
	
	@Override
	@Transactional(propagation=Propagation.MANDATORY)
	public Usuario buscaUsuarioPorEmail(String email) {
		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("email", email));

		return (Usuario) criteria.uniqueResult();
	}
}
