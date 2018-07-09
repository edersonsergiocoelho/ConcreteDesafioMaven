package br.com.concrete.controller;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.concrete.dto.entrada.ManutencaoUsuarioEntradaDto;
import br.com.concrete.service.UsuarioService;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	
	@RequestMapping(value = "/{codigoUsuario}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response buscaPorCodigoUsuario(@PathVariable Integer codigoUsuario) throws Exception {
		return usuarioService.buscaPorCodigoUsuario(codigoUsuario);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response incluirUsuario(@RequestBody ManutencaoUsuarioEntradaDto entrada) throws Exception {
		return usuarioService.incluirUsuario(entrada);
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response alterarUsuario(@RequestBody ManutencaoUsuarioEntradaDto entrada) throws Exception {
		return usuarioService.alterarUsuario(entrada);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response excluirUsuario(@RequestBody ManutencaoUsuarioEntradaDto entrada) throws Exception {
        return usuarioService.excluirUsuario(entrada);
	}
	
	@RequestMapping(value = "/login" , method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response login(@RequestHeader String email, @RequestHeader String password) throws Exception {
		return usuarioService.login(email, password);
	}
	
	@RequestMapping(value = "/perfil/{codigoUsuario}" , method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response perfil(@PathVariable Integer codigoUsuario, @RequestHeader String token) throws Exception {
		return usuarioService.perfil(codigoUsuario, token);
	}
}
