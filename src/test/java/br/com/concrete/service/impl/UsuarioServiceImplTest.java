package br.com.concrete.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.concrete.bean.Phone;
import br.com.concrete.bean.Usuario;
import br.com.concrete.dto.entrada.ManutencaoUsuarioEntradaDto;
import br.com.concrete.model.PhoneModel;
import br.com.concrete.repository.impl.PhoneRepositoryImpl;
import br.com.concrete.repository.impl.UsuarioRepositoryImpl;

public class UsuarioServiceImplTest {

	@InjectMocks
	UsuarioServiceImpl usuarioServiceImpl;
	
	@Mock
	PhoneServiceImpl phoneServiceImpl;
	
	@Mock
	PhoneRepositoryImpl phoneRepositoryImpl;

	@Mock
	UsuarioRepositoryImpl usuarioRepositoryImpl;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	//<<<--- TESTE DO MÉTODO BUSCAR POR CODIGO USUARIO --->>>
	
	@Test
	public void testBuscaPorCodigoUsuarioErroNaoExiste () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorCodigoUsuario(Mockito.anyInt())).thenReturn(usuarioNull());
		Response response = usuarioServiceImpl.buscaPorCodigoUsuario(Mockito.anyInt());
		Assert.assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testBuscaPorCodigoUsuarioSucesso () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorCodigoUsuario(Mockito.anyInt())).thenReturn(usuarioPreenchido());
		Response response = usuarioServiceImpl.buscaPorCodigoUsuario(Mockito.anyInt());
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	//<<<--- TESTE DO MÉTODO BUSCAR POR CODIGO USUARIO --->>>
	
	//<<<--- TESTE DO MÉTODO INCLUIR --->>>
	
	@Test
	public void testIncluirUsuarioErroUsuarioEmailExiste () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioPreenchido());
		Response response = usuarioServiceImpl.incluirUsuario(manutencaoUsuarioEntradaDtoPreenchido());
		Assert.assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testIncluirUsuarioErroInternalServerError () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioNull());
		Response response = usuarioServiceImpl.incluirUsuario(manutencaoUsuarioEntradaDtoNull());
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testIncluirUsuarioSucesso () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioNull());
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorCodigoUsuario(Mockito.anyInt())).thenReturn(usuarioPreenchido());
		Response response = usuarioServiceImpl.incluirUsuario(manutencaoUsuarioEntradaDtoPreenchido());
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	//<<<--- TESTE DO MÉTODO INCLUIR --->>>
	
	//<<<--- TESTE DO MÉTODO ALTERAR --->>>
	
	@Test
	public void testAlterarUsuarioSucesso () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioPreenchido());
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorCodigoUsuario(Mockito.anyInt())).thenReturn(usuarioPreenchido());
		Response response = usuarioServiceImpl.alterarUsuario(manutencaoUsuarioEntradaDtoPreenchido());
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	//<<<--- TESTE DO MÉTODO ALTERAR --->>>
	
	//<<<--- TESTE DO MÉTODO EXCLUIR --->>>
	
	@Test
	public void testExcluirUsuarioSucesso () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioPreenchido());
		Response response = usuarioServiceImpl.excluirUsuario(manutencaoUsuarioEntradaDtoPreenchido());
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testExcluirUsuarioErroInternalServerError () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioNull());
		Response response = usuarioServiceImpl.excluirUsuario(manutencaoUsuarioEntradaDtoNull());
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	//<<<--- TESTE DO MÉTODO EXCLUIR --->>>
	
	//<<<--- TESTE DO MÉTODO LOGIN --->>>
	
	@Test
	public void testLoginErroUsuarioNaoEncontrado () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioNull());
		Response response = usuarioServiceImpl.login("teste@junit.com", "123456");
		Assert.assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testLoginErroUsuarioOuSenhaInvalido () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioPreenchidoPasswordHash());
		Response response = usuarioServiceImpl.login("teste@junit.com", "12345");
		Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testLoginSucesso () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioPreenchidoPasswordHash());
		Response response = usuarioServiceImpl.login("teste@junit.com", "123456");
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testLoginErroInternalServerError () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorEmail(Mockito.anyString())).thenReturn(usuarioVazio());
		Response response = usuarioServiceImpl.login("teste@junit.com", "123456");
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	//<<<--- TESTE DO MÉTODO LOGIN --->>>
	
	//<<<--- TESTE DO MÉTODO PERFIL --->>>

	@Test
	public void testPerfilErroUsuarioTokenInexistente () {
		Response response = usuarioServiceImpl.perfil(1, null);
		Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testPerfilErroUsuarioTokenDiferente () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorCodigoUsuario(Mockito.anyInt())).thenReturn(usuarioPreenchido());
		Mockito.when(phoneServiceImpl.buscaListaPhone(Mockito.any(Usuario.class))).thenReturn(listaPhonePreenchida());
		Response response = usuarioServiceImpl.perfil(1, "A2");
		Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testPerfilErroUsuarioSessaoInvalida () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorCodigoUsuario(Mockito.anyInt())).thenReturn(usuarioPreenchidoLastLoginSessaoInvalida());
		Mockito.when(phoneServiceImpl.buscaListaPhone(Mockito.any(Usuario.class))).thenReturn(listaPhonePreenchida());
		Response response = usuarioServiceImpl.perfil(1, "A1");
		Assert.assertEquals(Response.Status.REQUEST_TIMEOUT.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testPerfilErroUsuarioSucesso () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorCodigoUsuario(Mockito.anyInt())).thenReturn(usuarioPreenchido());
		Mockito.when(phoneServiceImpl.buscaListaPhone(Mockito.any(Usuario.class))).thenReturn(listaPhonePreenchida());
		Response response = usuarioServiceImpl.perfil(1, "A1");
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void testPerfilErroUsuarioErroInternalServerError () {
		Mockito.when(usuarioRepositoryImpl.buscaUsuarioPorCodigoUsuario(Mockito.anyInt())).thenReturn(usuarioNull());
		Response response = usuarioServiceImpl.perfil(1, "A1");
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	//<<<--- TESTE DO MÉTODO PERFIL --->>>
	
	private Usuario usuarioNull () {
		Usuario usuario = null;
		return usuario;
	}
	
	private Usuario usuarioVazio () {
		Usuario usuario = new Usuario();
		return usuario;
	}
	
	private Usuario usuarioPreenchido () {
		Usuario usuario = new Usuario();
		usuario.setCreated(new Date());
		usuario.setEmail("teste@junit.com");
		usuario.setIduser(1);
		usuario.setLastLogin(new Date());
		usuario.setModified(new Date());
		usuario.setName("JUnit");
		usuario.setPassword("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
		
		List<Phone> listaPhone = new ArrayList<Phone>();
		
		Phone phone = new Phone();
		phone.setDdd(11);
		phone.setIdphone(1);
		phone.setNumber(123456);
		phone.setUsuarioIduser(usuario);
		
		listaPhone.add(phone);
		usuario.setPhoneList(listaPhone);
		
		usuario.setToken("16a36e86f6fed5d465ff332511a0ce1a863b55d364b25a7cdaa25db19abf9648");
		
		return usuario;
	}
	
	private Usuario usuarioPreenchidoLastLoginSessaoInvalida () {
		Usuario usuario = new Usuario();
		usuario.setCreated(new Date());
		usuario.setEmail("teste@junit.com");
		usuario.setIduser(1);
		
		Calendar dataAnterior = Calendar.getInstance();
		dataAnterior.set(2016, 7, 20, 12, 00);
		
		usuario.setLastLogin(dataAnterior.getTime());
		usuario.setModified(new Date());
		usuario.setName("JUnit");
		usuario.setPassword("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
		
		List<Phone> listaPhone = new ArrayList<Phone>();
		
		Phone phone = new Phone();
		phone.setDdd(11);
		phone.setIdphone(1);
		phone.setNumber(123456);
		phone.setUsuarioIduser(usuario);
		
		listaPhone.add(phone);
		usuario.setPhoneList(listaPhone);
		
		usuario.setToken("16a36e86f6fed5d465ff332511a0ce1a863b55d364b25a7cdaa25db19abf9648");
		
		return usuario;
	}
	
	private Usuario usuarioPreenchidoPasswordHash () {
		Usuario usuario = new Usuario();
		usuario.setCreated(new Date());
		usuario.setEmail("teste@junit.com");
		usuario.setIduser(1);
		usuario.setLastLogin(new Date());
		usuario.setModified(new Date());
		usuario.setName("JUnit");
		usuario.setPassword("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
		
		List<Phone> listaPhone = new ArrayList<Phone>();
		
		Phone phone = new Phone();
		phone.setDdd(11);
		phone.setIdphone(1);
		phone.setNumber(123456);
		phone.setUsuarioIduser(usuario);
		
		listaPhone.add(phone);
		usuario.setPhoneList(listaPhone);
		
		usuario.setToken("16a36e86f6fed5d465ff332511a0ce1a863b55d364b25a7cdaa25db19abf9648");
		
		return usuario;
	}
	
	private List<Phone> listaPhonePreenchida() {
		List<Phone> listaPhone = new ArrayList<Phone>();
		
		Phone phone = new Phone();
		phone.setDdd(11);
		phone.setIdphone(1);
		phone.setNumber(123456);
		phone.setUsuarioIduser(usuarioPreenchido());
		
		listaPhone.add(phone);
		
		return listaPhone;
	}
	
	private ManutencaoUsuarioEntradaDto manutencaoUsuarioEntradaDtoNull () {
		ManutencaoUsuarioEntradaDto manutencaoUsuarioEntradaDto = null;
		return manutencaoUsuarioEntradaDto;
	}
	
	private ManutencaoUsuarioEntradaDto manutencaoUsuarioEntradaDtoPreenchido () {
		ManutencaoUsuarioEntradaDto manutencaoUsuarioEntradaDto = new ManutencaoUsuarioEntradaDto();
		manutencaoUsuarioEntradaDto.setIdUser(1);
		manutencaoUsuarioEntradaDto.setEmail("teste@junit.com");
		manutencaoUsuarioEntradaDto.setName("JUnit");
		manutencaoUsuarioEntradaDto.setPassword("123456");
		
		List<PhoneModel> listaPhoneModel = new ArrayList<PhoneModel>();;
		
		PhoneModel phoneModel = new PhoneModel();
		phoneModel.setDdd(11);
		phoneModel.setNumber(123456);
		
		listaPhoneModel.add(phoneModel);
		manutencaoUsuarioEntradaDto.setPhones(listaPhoneModel);
		
		return manutencaoUsuarioEntradaDto;
	}
}
