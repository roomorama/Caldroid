Testes de Software - ESOF 2015/2016
========

# Conteúdos
1. [Introdução](#introdução)
2. [Grau de testabilidade do software do programa](#degree)
3. [Estatísticas de Teste](#statistics)
4. [Bug report](#bug_report)
5. [Bibliografia](#bli)


## *Introdução* <a name="introdução"></a>

* Controlar a qualidade dos sistemas de software é um grande desafio devido à alta
complexidade dos produtos e às inúmeras dificuldades relacionadas ao processo de
desenvolvimento, pois envolve questões humanas, técnicas, burocráticas, de negócio e políticas.

* Como consequência, testar um software permite aumentar a credibilidade do
mesmo e permite a avaliação da qualidade das características externas (ex: performance).

## *Grau de testabilidade do software do programa*  <a name="degree"></a>

A análise do grau de testabilidade é algo importante, e visto que o projeto em análise é portador de uma grande complexidade os
testes devem abordar todos os componentes envolventes no software.

Sendo os teste um grande método para garantir a qualidade do projeto é necessário percebermos a sua utilidade.

O grau de testabilidade está dividido nas seguintes secções:
#
1. [Controlabilidade](#control)
2. [Observabilidade](#obs)
3. [Capacidade de Isolamento](#cap)
4. [Separação de conceitos](#sep)
5. [Perceptibilidade](#percp)
6. [Heterogenidade](#heter)


## Controlabilidade <a name="control"></a>

  * *Fase em que é possível controlar o estado do componente que irá ser testado (CUT) em conformidade com o teste.*

  Dado que o [Caldroid](https://github.com/roomorama/Caldroid) não realiza testes não podemos falar concretamente para os módulos
  existentes. Desta forma podemos apenas afirmar que quanto maior a profundidade do componente, em relação à posição no código,
  maior deverá ser a sua controlabilidade.

## Observabilidade <a name="obs"></a>

  * *Fase em que é possível analisar os resultados intermediários e finais dos testes.*

## Capacidade de Isolamento <a name="cap"></a>

  * *Fase em que é possível testar o CUT de forma isolada.*

## Separação de conceitos <a name="sep"></a>

  * *Fase em que o componente a ser testado tem uma responsabilidade única a qual se encontra bem definda.*

  O projecto [Caldroid](https://github.com/roomorama/Caldroid) apresenta uma divisão em conceitos bem definida. Os responsáveis
  optaram pela divisão em 2 packages e toda a estrutura de código é realizada da forma mais simples evitando repetição de
  código e também a criação de código que não tenha qualquer utilidade. Desta forma, a equipa tenta ao máximo a reutilização de
  código para evitar a introdução de bugs no software.

## Perceptibilidade <a name="percp"></a>

  * *Fase em que o componente a ser testado se encontra bem documentado e auto-explicativo.*

  Em projetos de grande dimensão e com um grande número de contribuidores tal com o
  [Caldroid](https://github.com/roomorama/Caldroid), torna-se necessário que todos os intervenientes sigam uma determinada conduta
  de forma a tornar o projeto mais consistente e exato.

## Heterogenidade <a name="heter"></a>

  * *Fase em que requer o uso de diversos métodos de ensaio e ferramentas em paralelo.*

  A utilização de ferramentas em paralelo e de métodos de ensaio requer uma maior necessidade de comunicação entre os
  intervenientes uma vez que está sujeita à acção de vários contribuidores (no caso do Caldroid são 20 o número total de
  contribuidores).

  Como o [Caldroid](https://github.com/roomorama/Caldroid) é um projeto open-source, é estritamente necessário que
  depois da aceitação de um *pull request* o sistema permaneça operacional de forma a ser efectuada.


## *Estatísticas de Teste*  <a name="statistics"></a>

Esta fase de teste é realizada com o intuito de avaliar a qualidade do software, bem como a
eficiência e fiabilidade do mesmo.

No [Caldroid](https://github.com/roomorama/Caldroid), as alterações só são aceites quando satisfazem os requisitos especificados e contribuem para o aumento da qualidade ou fiabilidade do sistema. Não é feita uma análise rigorosa e detalhada do código aceite.


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

## *Bug Report*  <a name="bug_report"></a>

  *Neste secção iremos abordar casos de teste aplicados ao projeto bem como toda a análise efectuada pelo grupo  aos seus
  resultados.*



## *Bibliografia*  <a name="bli"></a>

  * **[SOFTWARE VERIFICATION AND VALIDATION](http://moodle.up.pt/pluginfile.php/74998/mod_resource/content/2/ESOF-VV%20-%20Part%20I.pdf)**
  * **[SOFTWARE TESTING](https://moodle.up.pt/pluginfile.php/74999/mod_resource/content/2/ESOF-VV%20-%20Part%20II.pdf)**
