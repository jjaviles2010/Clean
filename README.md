# Clean
Desenvolvido por um time da turma 18Mob da Fiap. O app tem o objetivo
de ajudar os nossos usuários a encontrar um serviço de limpeza de qualidade.
Este é um aplicativo Android desenvolvido em Kotlin.

### Características do projeto
* Linguagem de programação: Kotlin.
* Arquitetura MVVM.
* Usa algumas das bibliotecas do Android Architecture Components, ex.: ViewModel, LiveData, Room.
* O Clean utiliza a lib [mylib](https://jitpack.io/#lecosas/mylib), criada pelo próprio time para 
mostrar Toasts customizados.
* O Clean usa o [Koin](https://insert-koin.io/) como framework de injeção de dependência.
* Usamos algumas libs externas pra facilitar o desenvolvimento: [stetho](http://facebook.github.io/stetho/), [retrofit](https://square.github.io/retrofit/).
* O app é integrado com o Firebase, e usamos alguns dos seus recursos, ex.: Realtime Database, Analytics, Crashlytics, Remote Config, Cloud Messaging.

### Funcionalidades
* Cadastro de usuários, que podem ser clientes ou diaristas.
* Login do usuário.
* Lista com as diaristas cadastradas no sistema.
* Ver detalhes da diarista selecionando ela na lista.
* Agendar um serviço a partir da tela de detalhes da diarista.
* Ver lista com serviços agendados.
* Ver no mapa quais diaristas se encontram perto do local atual do cliente.
* Tela sobre com informações do time e do app.

### Execução
* Clone este repositório.
* Abra o projeto usando o Android Studio e aguarde o build do gradle.
* Starte um device no emulador do Android Studio ou conecte um devicee físico no PC.
* Clique no botão Run do Android Studio.
