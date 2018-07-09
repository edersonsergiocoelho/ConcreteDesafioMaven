package br.com.concrete.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.concrete.bean.Phone;
import br.com.concrete.bean.Usuario;
import br.com.concrete.dto.entrada.ManutencaoUsuarioEntradaDto;
import br.com.concrete.dto.saida.UsuarioSaidaDTO;
import br.com.concrete.enums.BusinessRulesReturnCodes;
import br.com.concrete.model.PhoneModel;
import br.com.concrete.model.UsuarioModel;
import br.com.concrete.repository.UsuarioRepository;
import br.com.concrete.service.PhoneService;
import br.com.concrete.service.UsuarioService;
import br.com.concrete.util.ConvertPasswordOrTokenHash;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	PhoneService phoneService;

	@Autowired
	UsuarioRepository usuarioRepository;
	
	final static Logger logger = Logger.getLogger(UsuarioServiceImpl.class);
	final static Long TIME_OUT_SESSAO_USUARIO = 30L;

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Response buscaPorCodigoUsuario(Integer codigoUsuario) {
		
		UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
		
		try {
			
			Usuario usuario = usuarioRepository.buscaUsuarioPorCodigoUsuario(codigoUsuario);
			
			if(usuario == null) {
				usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.USUARIO_NAO_EXISTE.getCode());
				usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.USUARIO_NAO_EXISTE.getMessage());
				
				return Response.status(Status.NOT_ACCEPTABLE).entity(usuarioSaidaDTO).build();
			}
			
			UsuarioModel usuarioModel = converteParaUsuarioModel(usuario);
			
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.SUCESSO.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.SUCESSO.getMessage());
			usuarioSaidaDTO.setUsuarioModel(usuarioModel);
			
		} catch (Exception e) {
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.ERRO_GERAL.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.ERRO_GERAL.getMessage() + ": " + e.getMessage());
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(usuarioSaidaDTO).build();
		}
		
		return Response.ok(usuarioSaidaDTO).build();
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Response incluirUsuario(ManutencaoUsuarioEntradaDto entrada) {
		UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
		
		try {
			
			Usuario usuarioEmailExistente = usuarioRepository.buscaUsuarioPorEmail(entrada.getEmail());
			
			if(usuarioEmailExistente != null) {
				usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.USUARIO_EMAIL_EXISTE.getCode());
				usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.USUARIO_EMAIL_EXISTE.getMessage());
				
				return Response.status(Status.NOT_ACCEPTABLE).entity(usuarioSaidaDTO).build();
			}
			
			Usuario usuario = new Usuario();
			usuario.setCreated(new Date());
			usuario.setEmail(entrada.getEmail());
			usuario.setLastLogin(new Date());
			usuario.setName(entrada.getName());
			usuario.setPassword(ConvertPasswordOrTokenHash.convertPasswordOrTokenHash(entrada.getPassword()));
			
			String token = UUID.randomUUID().toString();
			System.out.println("TOKEN ORIGINAL: " + token);
			usuario.setToken(ConvertPasswordOrTokenHash.convertPasswordOrTokenHash(token));
			
			usuarioRepository.create(usuario);
			
			List<Phone> listaPhoneIncluir = new ArrayList<Phone>();
			
			for (PhoneModel phoneModel : entrada.getPhones()) {
				
				Phone phone = new Phone();
				phone.setDdd(phoneModel.getDdd());
				phone.setNumber(phoneModel.getNumber());
				phone.setUsuarioIduser(usuario);
				
				listaPhoneIncluir.add(phone);
			}
			
			phoneService.createPhones(listaPhoneIncluir);
			
			UsuarioModel usuarioModel = converteParaUsuarioModel(usuario);
			
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.SUCESSO.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.SUCESSO.getMessage());
			usuarioSaidaDTO.setUsuarioModel(usuarioModel);
			
		} catch (Exception e) {
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.ERRO_GERAL.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.ERRO_GERAL.getMessage() + ": " + e.getMessage());
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(usuarioSaidaDTO).build();
		}
		
		return Response.ok(usuarioSaidaDTO).build();
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Response alterarUsuario(ManutencaoUsuarioEntradaDto entrada) {
		UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
		
		try {
			
			Usuario usuario = usuarioRepository.buscaUsuarioPorCodigoUsuario(entrada.getIdUser());
			
			usuario.setModified(new Date());
			usuario.setName(entrada.getName());
			usuario.setPassword(ConvertPasswordOrTokenHash.convertPasswordOrTokenHash(entrada.getPassword()));
			
			usuarioRepository.update(usuario);
			
			List<Phone> listaPhoneExcluir = phoneService.buscaListaPhone(usuario);
			
			if (listaPhoneExcluir != null) {
				phoneService.deletePhones(listaPhoneExcluir);
			}
			
			List<Phone> listaPhoneIncluir = new ArrayList<Phone>();
			
			for (PhoneModel phoneModel : entrada.getPhones()) {
				
				Phone phone = new Phone();
				phone.setDdd(phoneModel.getDdd());
				phone.setNumber(phoneModel.getNumber());
				phone.setUsuarioIduser(usuario);
				
				listaPhoneIncluir.add(phone);
			}
			
			phoneService.createPhones(listaPhoneIncluir);
			
			Usuario usuarioJaIncluido = usuarioRepository.buscaUsuarioPorCodigoUsuario(entrada.getIdUser());
			
			UsuarioModel usuarioModel = converteParaUsuarioModel(usuarioJaIncluido);
			
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.SUCESSO.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.SUCESSO.getMessage());
			usuarioSaidaDTO.setUsuarioModel(usuarioModel);
			
		} catch (Exception e) {
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.ERRO_GERAL.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.ERRO_GERAL.getMessage() + ": " + e.getMessage());
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(usuarioSaidaDTO).build();
		}
		
		return Response.ok(usuarioSaidaDTO).build();
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Response excluirUsuario(ManutencaoUsuarioEntradaDto entrada) {
		UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
		
		try {
			
			Usuario usuario = usuarioRepository.buscaUsuarioPorCodigoUsuario(entrada.getIdUser());
			List<Phone> listaPhoneExcluir = phoneService.buscaListaPhone(usuario);
			
			if (listaPhoneExcluir != null) {
				phoneService.deletePhones(listaPhoneExcluir);
			}
			
			usuarioRepository.delete(usuario);
			
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.SUCESSO.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.SUCESSO.getMessage());
			
		} catch (Exception e) {
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.ERRO_GERAL.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.ERRO_GERAL.getMessage() + ": " + e.getMessage());
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(usuarioSaidaDTO).build();
		}
		
		return Response.ok(usuarioSaidaDTO).build();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Response login(String email, String password) {
		UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
		
		try {
			
			Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(email);
			
			if(usuarioPorEmail == null) {
				usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.USUARIO_OU_SENHA_INVALIDO.getCode());
				usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.USUARIO_OU_SENHA_INVALIDO.getMessage());
				
				return Response.status(Status.NOT_ACCEPTABLE).entity(usuarioSaidaDTO).build();
			}
			
			if (ConvertPasswordOrTokenHash.isEqualPasswordOrTokenHash(usuarioPorEmail.getPassword(), password) == false) {
				usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.USUARIO_OU_SENHA_INVALIDO.getCode());
				usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.USUARIO_OU_SENHA_INVALIDO.getMessage());
				
				return Response.status(Status.UNAUTHORIZED).entity(usuarioSaidaDTO).build();	
			}
			
			UsuarioModel usuarioModel = converteParaUsuarioModel(usuarioPorEmail);
			usuarioSaidaDTO.setUsuarioModel(usuarioModel);
			
			usuarioPorEmail.setLastLogin(new Date());
			
			usuarioRepository.update(usuarioPorEmail);
			
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.SUCESSO.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.SUCESSO.getMessage());
			
		} catch (Exception e) {
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.ERRO_GERAL.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.ERRO_GERAL.getMessage() + ": " + e.getMessage());
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(usuarioSaidaDTO).build();
		}
		
		return Response.ok(usuarioSaidaDTO).build();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Response perfil(Integer codigoUsuario, String token) {
		UsuarioSaidaDTO usuarioSaidaDTO = new UsuarioSaidaDTO();
		
		try {
			
			if(token == null || token.isEmpty()) {
				usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.USUARIO_TOKEN_INEXISTENTE.getCode());
				usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.USUARIO_TOKEN_INEXISTENTE.getMessage());
				
				return Response.status(Status.UNAUTHORIZED).entity(usuarioSaidaDTO).build();
			}
			
			Usuario usuario = usuarioRepository.buscaUsuarioPorCodigoUsuario(codigoUsuario);
			usuario.setPhoneList(phoneService.buscaListaPhone(usuario));
			
			if (ConvertPasswordOrTokenHash.isEqualPasswordOrTokenHash(usuario.getToken(), token) == false) {
				usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.USUARIO_TOKEN_INEXISTENTE.getCode());
				usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.USUARIO_TOKEN_INEXISTENTE.getMessage());
				
				return Response.status(Status.UNAUTHORIZED).entity(usuarioSaidaDTO).build();	
			}
	        
			if (diferencaDeMinutosEntreLogin(usuario.getLastLogin()) > TIME_OUT_SESSAO_USUARIO) {
				usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.USUARIO_TOKEN_INEXISTENTE.getCode());
				usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.USUARIO_TOKEN_INEXISTENTE.getMessage());
				
				return Response.status(Status.REQUEST_TIMEOUT).entity(usuarioSaidaDTO).build();
			}
			
			UsuarioModel usuarioModel = converteParaUsuarioModel(usuario);
			usuarioSaidaDTO.setUsuarioModel(usuarioModel);
			
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.SUCESSO.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.SUCESSO.getMessage());
			
		} catch (Exception e) {
			usuarioSaidaDTO.setCode(BusinessRulesReturnCodes.ERRO_GERAL.getCode());
			usuarioSaidaDTO.setMessage(BusinessRulesReturnCodes.ERRO_GERAL.getMessage() + ": " + e.getMessage());
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(usuarioSaidaDTO).build();
		}
		
		return Response.ok(usuarioSaidaDTO).build();
	}
	
	private UsuarioModel converteParaUsuarioModel (Usuario usuario) {
		
		UsuarioModel usuarioModel = new UsuarioModel();
		usuarioModel.setCreated(usuario.getCreated());
		usuarioModel.setEmail(usuario.getEmail());
		usuarioModel.setId(usuario.getIduser());
		usuarioModel.setLast_login(usuario.getLastLogin());
		usuarioModel.setModified(usuario.getModified());
		usuarioModel.setName(usuario.getName());
		usuarioModel.setPassword(usuario.getPassword());
		usuarioModel.setToken(usuario.getToken());
		
		List<PhoneModel> listaPhoneModel = new ArrayList<PhoneModel>();
		
		if (usuario.getPhoneList() != null) {
			for (Phone phone : usuario.getPhoneList()) {
				
				PhoneModel phoneModel = new PhoneModel();
				phoneModel.setDdd(phone.getDdd());
				phoneModel.setNumber(phone.getNumber());
				
				listaPhoneModel.add(phoneModel);
			}
		}
		
		usuarioModel.setListaPhoneModel(listaPhoneModel);
		
		return usuarioModel;
	}
	
	private Long diferencaDeMinutosEntreLogin (Date lastLogin) {
        Calendar horarioInicialLogin = Calendar.getInstance();
        Calendar horarioFinalLogin = Calendar.getInstance();

        horarioInicialLogin.setTime(lastLogin);
        horarioFinalLogin.setTime(new Date());

        long minutosDeDiferenca = ((horarioFinalLogin.getTimeInMillis() - horarioInicialLogin.getTimeInMillis())) / 60000;
        
        return minutosDeDiferenca;
	}
}
