# TesteViaVarejo
Implementação de API de teste para o projeto Via Varejo

A API fornece o método *POST* para o recurso ***/api/order***

No arquivo **application.properties** temos duas possibilidades de configuração:

- Porta de execução do WebService (**server.port**), por padrão estará configurado o valor **8080**.

- Caminho base para a API (**server.servlet.contextPath**), por padrão estará configurado o caminho **/via_varejo**.


Os logs podem ser obtidos via console (se executando em modo debug) e no arquivo de log (dentro da pasta logs com o nome **api\_via\_varejo.log**) que 
ficam no diretório base do projeto (ou no diretório base do servidor de aplicação utulizado, por exemplo o Tomcat).

URL completa para execução da chamada:

    http://<HOST>:<PORTA>/via_varejo/api/order

Por exemplo:

    http://127.0.0.1:8080/via_varejo/api/order

**JSON de envio no POST:**

```json
{
"condicaoPagamento": {
		"qtdeParcelas": "integer // Quantidade de parcelas",
		"valorEntrada": "float // Valor de entrada"
		
	},
	"produto": {
		"codigo": "integer // Código do produto",
		"valor": "float // Valor do produto",
		"nome": "string // Nome do produto"
	}
}
```

- Por exemplo:

```json
{
"condicaoPagamento": {
		"qtdeParcelas": 6,
		"valorEntrada": 0
		
	},
	"produto": {
		"codigo": 12,
		"valor": 122,
		"nome": "Jogo de talher"
	}
}
```
**Json de retorno da API para o status HTTP 200 OK com parcelas menor do que 6:**
```json
[
    {
        "numeroParcela": "integer // Número da parcela",
        "valor": "float // Valor da parcela"
    },
]
```

 - Por exemplo:
```json
[
    {
        "numeroParcela": 1,
        "valor": 24.4
    },
    {
        "numeroParcela": 2,
        "valor": 24.4
    },
    {
        "numeroParcela": 3,
        "valor": 24.4
    },
    {
        "numeroParcela": 4,
        "valor": 24.4
    },
    {
        "numeroParcela": 5,
        "valor": 24.4
    }
]
```

**Json de retorno da API para o status HTTP 200 OK com parcelas maior ou igual a 6:**
```json
[
    {
        "numeroParcela": "integer // Número da parcela",
        "valor": "float // Valor da parcela",
        "taxaJurosAoMes": "float // Taxa de juros mensal considerada"
    },
]
```

 - Por exemplo:
```json
[
    {
        "numeroParcela": 1,
        "valor": 21.736334,
        "taxaJurosAoMes": 1.15
    },
    {
        "numeroParcela": 2,
        "valor": 21.736334,
        "taxaJurosAoMes": 1.15
    },
    {
        "numeroParcela": 3,
        "valor": 21.736334,
        "taxaJurosAoMes": 1.15
    },
    {
        "numeroParcela": 4,
        "valor": 21.736334,
        "taxaJurosAoMes": 1.15
    },
    {
        "numeroParcela": 5,
        "valor": 21.736334,
        "taxaJurosAoMes": 1.15
    },
    {
        "numeroParcela": 6,
        "valor": 21.736334,
        "taxaJurosAoMes": 1.15
    }
]
```

**Json de retorno da API para o status HTTP de erro (maior ou igual a 400 ) com parcelas maior ou igual a 6:**
```json
{
    "timestamp": "integer // Data e hora do erro no formato UNIX",
    "status": "integer // Status HTTP",
    "error": "string // Tipo do erro HTTP",
    "message": "string // Detalhe da mensagem do erro",
    "path": "string // Caminho do recurso que gerou o erro"
}
```

 - Por exemplo:
```json
{
    "timestamp": 1570936284674,
    "status": 400,
    "error": "Bad Request",
    "message": "Se for informado, o valor de entrada não pode ser maior do que o valor do produto",
    "path": "/via_varejo/api/order"
}
```

URL completa para documentação Swagger

    http://<HOST>:<PORTA>/via_varejo/swagger-ui.html

Por exemplo:

    http://127.0.0.1:8080/via_varejo/swagger-ui.html


