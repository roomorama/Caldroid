Testes de Software - ESOF 2015/2016
========

# Conteúdos
1. [Introdução](#introdução)
2. [Test Phases](#phases)
3. [Test Statistics](#statistics)
4. [Bug report](#bug_report)


## Introdução <a name="introdução"></a>

* Controlar a qualidade dos sistemas de software é um grande desafio devido à alta
complexidade dos produtos e às inúmeras dificuldades relacionadas ao processo de
desenvolvimento, pois envolve questões humanas, técnicas, burocráticas, de negócio e políticas.

* Como consequência, testar um software permite aumentar a credibilidade do
mesmo e permite a avaliação da qualidade das características externas (ex: performance).

## Test Phases  <a name="phases"></a>


## Test Statistics  <a name="statistics"></a>

Esta fase de teste é realizada com o intuito de avaliar a qualidade do software, bem como a
eficiência e fiabilidade do mesmo.

No [Caldroid](https://github.com/roomorama/Caldroid), as alterações só são aceites se
trouxeram resposta aos requisitos especificados e contribuírem para o aumento da qualidade
ou fiabilidade do sistema, não sendo realizada uma análise rigorosa e detalhada do código
aceite.


* **Unit Testing**

    *O principal objectivo é detectar erros funcionais e estruturais no software.*

    * Para um *pull request* ser aceite pelo master, este é o caso de teste mais importante ou talvez até o único
    utilizado no projeto [Caldroid](https://github.com/roomorama/Caldroid). Desta forma, qualquer nova alteração é aceite desde que
    não traga qualquer tipo de  erros funcionais e estruturais ao software e responda aos requisitos especificados por parte do
    cliente/utilizador.

* **Integration testing**

    *Esta fase permite uma fácil localização dos erros e tem como principal objectivo detectar erros que occorram na unidade interface do software.*

    * Depois de uma intensa análise e comunicação com os detentores do projeto, verificamos que esta fase de teste não se encontra
    presente no [Caldroid](https://github.com/roomorama/Caldroid) .

* **System testing**

    *Testes realizados em todas as unidades do software para avaliar conformidade do sistema com os requisitos especificados.*

    * Esta fase de teste é efectuada no projeto [Caldroid](https://github.com/roomorama/Caldroid) com o intuito de verificar se a mudança não influenciou negativamente o funcionamento global do programa.

* **Acceptance testing**

    *Esta fase de teste é utilizada para determinar se o sistema satisfaz os critérios de aceitação impostos pelo cliente ou qualquer outra entidade que irá usufruir do software.*

    * No projeto [Caldroid](https://github.com/roomorama/Caldroid) o cliente pode editar o software de acordo com os seus requisitos o que leva a que não seja necessário a realização de *Acceptance testing*.


* **Regression testing**

    *Esta análise é efectuada com o intuito de verificar se as modificações realizadas não causaram efeitos que pusessem em causa os requisistos especificados pelo cliente.*

    * No projeto [Caldroid](https://github.com/roomorama/Caldroid), sempre que uma alteração é proposta alguém irá ser responsável por testá-la e avaliá-la, sendo que o principal objectivo é que esta mudança não provoque erros nas restantes funções implementadas.

## Bug Report  <a name="bug_report"></a>
