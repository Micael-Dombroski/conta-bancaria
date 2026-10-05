# Conta Bancária

Sistema de contas bancárias em Java, desenvolvido para praticar POO e arquitetura em camadas. Cadastra clientes, endereços e contas (corrente e poupança), com persistência em PostgreSQL e preenchimento automático de endereço pelo CEP usando a API **ViaCEP**.

> **Status:** em desenvolvimento. A camada de persistência (DAOs e Repositories) está pronta e testada. A camada de serviço (regras de negócio) e a interface JavaFX estão em construção.

## Funcionalidades

- Cadastro, consulta, edição e exclusão de **clientes**, **endereços** e **contas**
- Dois tipos de conta: **corrente** e **poupança**
- Preenchimento automático do endereço a partir do CEP (ViaCEP)
- CPF protegido no banco: armazenado **criptografado** e consultado por **hash**
- Senha da conta armazenada com **BCrypt** (nunca em texto puro)
- Testes de integração para cada DAO

## Tecnologias

| Item | Versão / uso |
|---|---|
| Java | 23 (projeto modular, `module-info.java`) |
| Build | Maven |
| Banco de dados | PostgreSQL (driver JDBC 42.7) |
| Interface | JavaFX 21 |
| Senhas | jBCrypt 0.4 |
| HTTP / JSON | Apache HttpClient (fluent-hc) e Gson |
| Testes | JUnit 5 |

## Integração com o ViaCEP

O [ViaCEP](https://viacep.com.br) é uma API pública e gratuita que devolve o endereço completo a partir de um CEP. O sistema a usa para que o usuário digite apenas o CEP e o número, e o resto (logradouro, bairro, cidade e UF) seja preenchido sozinho.

Requisição:

```
GET https://viacep.com.br/ws/{cep}/json/
```

Exemplo de resposta:

```json
{
  "cep": "22461-000",
  "logradouro": "Rua Jardim Botânico",
  "complemento": "até 518 - lado par",
  "bairro": "Jardim Botânico",
  "localidade": "Rio de Janeiro",
  "uf": "RJ"
}
```

Esses campos mapeiam diretamente para a classe `Endereco` (por isso ela expõe `getLocalidade()`). CEP inexistente retorna `{"erro": true}` e CEP malformado retorna HTTP 400, então ambos precisam ser tratados antes de salvar.

## Arquitetura

O projeto é dividido em camadas, com dependências apontando para o domínio:

```
conta
├── domain
│   ├── model        Cliente, Conta, ContaCorrente, ContaPoupanca, Endereco, TipoConta
│   ├── repository   Interfaces: ClienteRepository, ContaRepository, EnderecoRepository
│   └── service      Regras de negócio (em construção)
├── infra
│   ├── dao          ClienteDAO, ContaDAO, EnderecoDAO (SQL via JDBC)
│   ├── repository   Implementações *RepositoryJdbc (delegam aos DAOs)
│   ├── database     DataBaseConnection
│   └── security     HashSenha, HashCPF, CPFCrypto
├── api              Cliente HTTP do ViaCEP
└── ui               Telas JavaFX
```

Fluxo de uma chamada:

```
UI  →  Service  →  Repository (interface)  →  RepositoryJdbc  →  DAO  →  PostgreSQL
```

- **DAO:** fala SQL e tabelas.
- **Repository:** fala a linguagem do domínio ("salvar conta", "buscar cliente por CPF") e devolve `Optional` para buscas de um item e `List` (nunca `null`) para listas.
- **Service:** concentra as regras de negócio, incluindo a autenticação. O Repository não verifica senha.

## Modelo de dados

Três tabelas relacionadas:

```
enderecos  (id, cep, logradouro, complemento, numero, bairro, cidade, uf, data_modificacao)
    ▲
    │ endereco_id
clientes   (id, cpf_crypt, cpf_hash, nome, data_nascimento, endereco_id, data_modificacao)
    ▲
    │ cliente_id
contas     (id, numero, cliente_id, saldo, senha_hash, tipo, data_modificacao)
```

- Um endereço é identificado por **CEP + número** (número ausente é gravado como `N/A`).
- O `tipo` da conta é `1` (corrente) ou `2` (poupança).
- Existem as chaves estrangeiras `fk_endereco_cliente` e `fk_conta_cliente`, então a ordem de exclusão é: contas, cliente, endereço.

## Segurança

| Dado | Como é guardado |
|---|---|
| Senha da conta | Hash **BCrypt** (custo 12, salt aleatório). A verificação é feita em Java com `HashSenha.verificar`. |
| CPF | Duas colunas: `cpf_crypt` (criptografado, reversível) e `cpf_hash` (usado para buscar sem expor o CPF). |

## Como executar

### Pré-requisitos

- JDK 23
- Maven
- PostgreSQL em execução

### Passos

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   cd conta-bancaria
   ```
2. Crie o banco e as tabelas (`enderecos`, `clientes`, `contas`) conforme o modelo acima.
3. Configure a conexão em `DataBaseConnection` (URL, usuário e senha do PostgreSQL).
4. Configure a chave usada pelo `CPFCrypto`. **Não versione chaves nem senhas**; prefira variáveis de ambiente.
5. Compile e execute:
   ```bash
   mvn clean compile
   mvn javafx:run
   ```

## Testes

Os testes são de integração e usam o banco real, então configure um banco de **desenvolvimento**, nunca de produção.

```bash
mvn test
```

Cada classe de teste limpa os dados que cria (por CPF ou por número de endereço) no `@BeforeEach` e no `@AfterEach`, para não depender do estado deixado por execuções anteriores:

| Classe | O que cobre |
|---|---|
| `ClienteDAOTest` | Adicionar (inclusive duplicado), editar, consultar e excluir cliente |
| `EnderecoDAOTest` | Adicionar, editar, consultar por CEP/número e excluir endereço |
| `ContaDAOTest` | Adicionar, atualizar saldo, trocar senha, consultar por número/tipo e excluir conta |

## Roadmap

- [x] Modelo de domínio (`Cliente`, `Conta`, `Endereco`)
- [x] DAOs com testes de integração
- [x] Repositories (interfaces e implementações JDBC)
- [ ] Tratar `SQLException` de forma consistente no `EnderecoDAO`
- [ ] `ContaService`: autenticar, depositar, sacar, transferir e excluir com senha
- [ ] Validações no domínio (valor positivo, saldo suficiente)
- [ ] Transferência atômica (uma única transação para as duas contas)
- [ ] Testes do Service com Repository em memória
- [ ] Integração do ViaCEP na interface
- [ ] Telas JavaFX
- [ ] Trocar `double` por `BigDecimal` nos valores monetários

## Autor

Projeto de estudo desenvolvido por **Micael e Andre** com uso de Inteligência Artificial.