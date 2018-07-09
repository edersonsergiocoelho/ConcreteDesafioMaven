package br.com.concrete.enums;

public enum BusinessRulesReturnCodes {
	
	INCLUIR 									(1,    "Registro Incluído Com Sucesso"),
	ALTERAR 									(2,    "Registro Alterado Com Sucesso"),
	EXCLUIIR 									(3,    "Registro Excluído Com Sucesso"),
	SUCESSO 									(4,    "Sucesso"),
	ERRO_GERAL 									(5,    "Erro Geral"),
	USUARIO_NAO_EXISTE							(6,    "Usuário Não Existe"),
	USUARIO_EMAIL_EXISTE						(7,    "Email Já Existente"),
	USUARIO_OU_SENHA_INVALIDO					(8,    "Usuário E/Ou Senha Inválidos"),
	USUARIO_TOKEN_INEXISTENTE					(9,    "Não Autorizado"),
	USUARIO_SESSAO_INVALIDA 					(10,   "Sessão Inválida");
	
	private Integer code;
	private String message;
	
	private BusinessRulesReturnCodes(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
