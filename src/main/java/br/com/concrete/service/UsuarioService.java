package br.com.concrete.service;

import javax.ws.rs.core.Response;

import br.com.concrete.dto.entrada.ManutencaoUsuarioEntradaDto;

public interface UsuarioService {

	Response buscaPorCodigoUsuario(Integer codigoUsuario);
	
	Response incluirUsuario(ManutencaoUsuarioEntradaDto usuario);

	Response alterarUsuario(ManutencaoUsuarioEntradaDto usuario);

	Response excluirUsuario(ManutencaoUsuarioEntradaDto usuario);

	Response login(String email, String password);

	Response perfil(Integer codigoUsuario, String token);

}
