# Backend Spring Boot

Backend com rotas REST para as entidades `Cliente` e `Produto`.

## Rotas disponíveis

### Cliente
- `GET /cliente` - lista todos
- `POST /cliente` - cria um cliente
- `PATCH /cliente/{codigo}` - atualiza um cliente
- `DELETE /cliente/{codigo}` - exclui um cliente

### Produto
- `GET /produto` - lista todos
- `POST /produto` - cria um produto
- `PATCH /produto/{codigo}` - atualiza um produto
- `DELETE /produto/{codigo}` - exclui um produto

## Estrutura implementada
- `model/cliente/Cliente.java`
- `model/produto/Produto.java`
- `repository/ClienteRepository.java`
- `repository/ProdutoRepository.java`
- `service/ClienteService.java`
- `service/ProdutoService.java`
- `controller/ClienteController.java`
- `controller/ProdutoController.java`

## Observações
- O banco de dados já deve estar configurado em `application.properties`.
- As rotas possuem CORS liberado para facilitar o uso com Angular.

