
# spring-boot-base
Base para projetos de serviço REST com SpringBoot.

Vem pré configurado com Spring Web, Spring Data Jpa, Spring Security, Spring Actuator,


## Instalação
Adicionar ao Maven do projeto:

```xml
	<dependency>
		<groupId>info.agilite</groupId>
		<artifactId>spring-boot-base</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
```
## Spring Security
É necessário implementar a interface **SecurityUserService**, essa interface é utilizada para obter dados do usuário seja através de banco de dados seja através de qualquer outro serviço de login

O Spring Security vem configurado da seguinte forma:
 - Acesso livre a todas as URLS **/public/****  
 - Acesso restrito apenas a usuários com a <i>Role</i> **SIS_ADMIN** para todos os Endpoints do Actuator e para todas as URLS **/sisadmin/****
 - Acesso restrito para usuários logados com qualquer <i>Role</i> para todas as outras URLS
 
 
#### Cache de usuários a partir do Token
 > Atenção a implementação padrão do cache de usuários logados a partir do token JWT está implementado em memória, caso o seu projeto utilize mais de um servidor em produção você deve implementar a interface **SecurityUserCacheByToken** com outra abordagem de cache como o REDIS por exemplo
 
Para configurar o cache em memória são aceitos 2 parâmetros:
 - **info.agilite.base.memory-user-cache-max-size**: Quantidade máxima de usuários no Cache (Default 100)
 - **info.agilite.base.memory-user-cache-expire-minutes**: Tempo em minutos do timeout do usuário no cache (Default 30)
 

#### Login/Logout Controller
O usuário terá um instância única, cada vez que o usuário fizer o login através do username e da senha o sistema irá gerar um novo token JWT. O Token gerado deve ser enviado nas próximas requisições através do Token **Authorization**

 Nesse projeto já é fornecido um controller para o Login que pode ser acessado através da URL **/public/login**, conforme abaixo:
 ```java
    @PostMapping
	public String login(
			@RequestParam("username") final String username, 
			@RequestParam("senha") final String senha){
		return service.login(username, senha).map(SecurityUser::getToken).orElseThrow(()-> new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválido"));
	}
 ```

e um controller para logout que pode ser acessado através da URL **/logout**, veja o código abaixo
```java
    @PostMapping
	public void logout(@AuthenticationPrincipal SecurityUser user){
		service.logout(user);
	}
```