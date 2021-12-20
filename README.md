# redes-ep2-typerace
Repositório para o EP2 de Redes de Computadores, EACH-USP - 2021/2

# Integrantes
* Ahmad Kamel Abdouni - 11795825
* Gianlucca Siqueira Maiellaro - 11795929
*  Luiza Borghi de Mello - 11796037
*  Raphael Nobuaki Iwamoto - 11882986

## Pré-requisitos
* JDK 11 ou maior (testado com a JDK11 OpenJDK)
* Gradle (incluso no repositório, não é necessário instalá-lo)

### Rodando
Para rodar o servidor
```sh
./gradlew server:run 
```
Para rodar o servidor com o terminal mais limpo
```sh
./gradlew server:run --console=plain
```
Para rodar um cliente
```sh
./gradlew client:run
```
Para rodar um cliente com o terminal mais limpo (recomendado)
```sh
./gradlew client:run --console=plain
```

## Regras
* Ao iniciar um client é solicitado um id para o usuário. Caso o id escolhido já esteja sendo utilizado, o usuário é registrado com o id desejado + um número, incrementado sequencialmente.
* Uma mensagem vazia não é enviada para o servidor.
* O usuário só precisa digitar as palavras corretamente, letras maiúsculas e minúsculas são desconsideradas.
* O jogo roda localmente na porta 8080.
* Cada partida possui 50 palavras, vence o jogo aquele que escrever corretamente 20 palavras primeiro ou aquele que acertou o maior número de palavras caso ninguém acerte 20. 
* Caso um jogador seja desconectado durante uma partida, a aplicação considera que ele errou todas as palavras restantes.

### Comandos
* Start: comando utilizado para iniciar uma partida, pode ser invocado por qualquer jogador.
* Quit: comando utilizado para um cliente se desconectar, pode ser utilizado a qualquer momento.

## Informações do jogo
* As palavras foram extraídas do [link](http://200.17.137.109:8081/novobsi/Members/cicerog/disciplinas/introducao-a-programacao/arquivos-2016-1/algoritmos/Lista-de-Palavras.txt/view).
* Para embaralhar e reduzir o número de palavras utilizou-se o [site](https://onlinerandomtools.com/shuffle-words).
* Foram mantidas 5000 palavras em um arquivo de texto na pasta resources do pacote server e utilizadas 50 para cada partida.
