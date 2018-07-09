package br.com.concrete.repository;

import br.com.concrete.bean.Usuario;
import br.com.concrete.repository.generic.GenericRepositoryJPA;

public interface UsuarioRepository extends GenericRepositoryJPA<Usuario, Integer>  {

	Usuario buscaUsuarioPorEmail(String email);

	Usuario buscaUsuarioPorCodigoUsuario(Integer codigoUsuario);

}
