# 5A-SMA TP1
###### Dylan MARSANGY - Laura PHILIBERT

## Introduction
Dans le cadre des TPs sur les Systèmes Multi-Agents (SMA) à Polytech Lyon, nous avons eu l'occasion de développer un taquin dont les pièces sont des automates indépendants les uns des autres.
Leur but est de compléter le taquin, c'est-à-dire que chaque agent soit sur sa case 'but'.
Ce dépôt GitHub contient les sources de ce projet ainsi qu'un rapport étant ce README.md.

Dans ce rapport nous allons présenter en deux temps :
- La [prise en main](#prise-en-main-de-lapplication) de l'application avec une explication de la génération du taquin et du comportement des pièces (aspect fonctionnel).
- Le [développement](#dveloppement-du-projet) du projet qui se concentre sur l'aspect technique (cahier des charges, implémentation des pièces).

Nous conclurons sur les perspectives d'évolution du projet.

## Prise en main de l'application

### Démarrer le jeu

Le projet a été développé sous Java 8. Cette version est donc nécessaire pour démarrer l'application.

#### En lançant le jar (conseillé)
Avec le mail de rendu du projet a été transmis un fichier 'TP1-SMA-MARSANGY-PHILIBERT.jar'. Exécutez-le dans un terminal grâce à la commande suivante :
```shell script
java -jar TP1-SMA-MARSANGY-PHILIBERT.jar
```

Notez que différentes options existent pour exécuter le fichier.
Ces options figurent dans le tableau ci-dessous.

| OPTION | DESCRIPTION                                     | TYPE    | REQUIS | DÉFAUT |
|--------|-------------------------------------------------|---------|--------|--------|
| p      | Période d'exécution des automates (en secondes) | Integer | Non    | 0      |
| t      | Type de puzzle à instancier                     | String  | Non    | (none) |
| n      | Nombre de pièces placées sur le plateau         | Integer | Non    | 17     |

- La valeur de l'option `p` est la période d'exécution des automates (pièces), déterminant donc le temps qui s'écoule entre deux exécutions d'un automate (pièce). Par défaut, cette période est à 0 (loop). Si elle est définie comme négative par l'utilisateur, elle sera ramenée automatiquement à 0.
- La valeur de l'option `t` détermine le puzzle qui est instancié. Par défaut, le puzzle généré est aléatoire (grille de taille 5x5 avec les pièces aléatoirement placées dessus).
- La valeur de l'option `n` détermine le nombre de pièces (automates) générées sur la grille. Par défaut, elle est à 17.

Ainsi, par défaut, `java -jar TP1-SMA-MARSANGY-PHILIBERT.jar` revient à lancer `java -jar TP1-SMA-MARSANGY-PHILIBERT.jar --p=0 --n=17`.

Il est à noter que lorsque l'option `t` est renseignée, l'option `n` est ignorée (puisqu'un puzzle déterminé sera renvoyé grâce à la première option).
Le tableau des valeurs possibles de l'option `t` est disponible ci-dessous.

| VALEUR       | DESCRIPTION                                                            |
|--------------|------------------------------------------------------------------------|
| path-finding | Exemple de path-finding (automate suivant le meilleur chemin)          |
| push-request | Exemple de push request (automate demandant à un voisin de se pousser) |

Si une quelconque erreur figure dans les valeurs tapées par l'utilisateur dans le terminal, l'application s'arrêtera automatiquement et affichera un message d'erreur lui indiquant ses erreurs de saisie éventuelles.

*Exemple :*
```shell script
java -jar TP1-SMA-MARSANGY-PHILIBERT.jar --p=1 --t=path-finding
```

Cette commande générera une grille 3x3 avec deux pièces dont une est déjà posée sur sa destination finale (but).
L'autre doit encore s'y rendre. Cette grille déterminée permet de constater que l'automate suit le meilleur chemin pour se rendre à son but (chemin de Manhattan).
Cela signifie qu'elle se rendra à sa case par le plus court chemin qui évite la collision éventuelle avec d'autres agents (pour éviter d'envoyer des messages alors qu'il peut simplement contourner l'obstacle en prenant un autre chemin tout aussi court).

*Cette manière de procéder, avec la création de puzzles spécifiques/déterminés, nous a permis de réaliser des tests élémentaires sur le bon fonctionnement de nos fonctionnalités (déplacement optimal, messages envoyés à un autre automate, ...).*

#### En exécutant la fonction main (déprécié)
Exécutez la fonction `main` de la classe `view.View`. Modifiez les différents paramètres qui sont présents en attributs de la classe à votre convenance.

*Exemple :*
```java
public class View extends javafx.application.Application {
    // ...
    private static final int PUZZLE_SIZE = 5;
    private static final int NUMBER_PIECES = 4;
    // ...
}
```

*Le premier attribut permet de créer une grille de dimension 5 * 5.
Le second permet d'instancier 4 pièces sur ladite grille.*

### Écrans

Lorsque l'application est démarrée, une nouvelle fenêtre faisant apparaître le plateau de jeu va s'ouvrir, comme ci-dessous :

![Exemple de puzzle](/doc/puzzle-example.PNG)

*Fig. 1 : Exemple de puzzle*

Explications du plateau :
- Une case blanche correspond à une case vide.
- Une case colorée correspond à une pièce. Une pièce est également indexée par un identifiant sous forme d'entier.
- Les entiers inscrits dans le coin supérieur droit des cases signifie que la case est la case 'but' de l'agent dont l'identifiant correspond.

*Exemple par rapport à la figure : la case (2,2) contient la pièce n°2. Cette case est la case 'but' de la pièce n°4.*

La grille occupe la place de toute la fenêtre.
De plus, la taille des cases évolue dynamiquement avec les dimensions de la grille.

De plus, dans le terminal ayant servi à l'exécution de l'application apparaissent également des logs.
Ils correspondent à l'historique de la partie, récapitulant les actions qui ont été effectuées par les pièces.
Un exemple de logs est joint ci-dessous :

![Exemple de logs](/doc/logs-example.PNG)

*Fig. 2 : Exemple de logs console*

Les logs sont expliqués dans la partie ci-dessous.

### Les logs console
Différents logs sont affichés en console dépendamment des actions réalisées dans le jeu.
- `P3@(3, 5)->(LEFT)(3, 4)` signifie que la pièce 3 s'est déplacé de la première case (3,5) vers la seconde (3,4) (mouvement vers la gauche - LEFT).
- `P4@(UP)(1, 3)` signifie que la pièce 4 a tenté de se déplacer vers le haut (UP) mais est restée immobile sur sa case (1,3).
- `-- P2:MOVING->WAITING` signifie que la pièce 2 est passé de l'état `MOVING` à l'état `WAITING` (automate).
- `|| P1` signifie que la pièce 1 a reçu un message.
- `P1<>(1) From P8 to P1 at 07/01/2020 09:13:48: (PUSH) P8@(1, 2)->(1, 1)` signifie que la pièce 1 lit le message n°1 et le traite (push request).
- `P1!<>!(1) From P8 to P1 at 07/01/2020 09:13:48: (PUSH) P8@(1, 2)->(1, 1)` signifie que la pièce 1 lit le message n°1 et l'ignore.
- `P8...(1, 1)` signifie que la pièce 8 reste immobile sur la case (1, 1) (soit parce qu'elle attend qu'une pièce se déplace, soit parce qu'elle est arrivée à sa case destination).

### Génération de la grille
Par défaut une grille 5x5 est créée avec un certain nombre de pièces au démarrage du programme (17).
Les pièces (indexées par un numéro) sont positionnées aléatoirement sur la grille.
De plus, leur case 'but' est également choisie aléatoirement.

### Fonctionnement des pièces
Une pièce (agent) va spontanément se déplacer vers sa case 'but' avec une probabilité 9/10 ou se déplacer sur une case voisine aléatoire avec une probabilité 1/10.
Ce choix a été fait pour permettre de débloquer des situations en évitant des boucles.

Si une pièce est susceptible de rentrer en collision avec une autre pièce (obstacle), alors elle arrête son mouvement et envoie un message à l'obstacle.
Ce dernier va alors se déplacer vers une case libre voisine. S'il n'y en a pas, elle choisit une case aléatoirement autour d'elle.
Il est à noter que l'obstacle va préférer une case qui l'éloigne forcément de la première pièce (utile si elle a à se déplacer plusieurs fois d'affilées).

Quand une pièce reçoit un message, elle a une probabilité de 7/10 de le traiter et une probabilité de 3/10 de l'ignorer.
Ce choix a été fait pour éviter le flood de messages dans la boîte aux lettres et ne pas trop perturber le comportement des pièces.

Les pièces ont été conçues comme des automates.
Chaque pièce alterne donc entre plusieurs états en cours de la partie et effectue différentes actions selon cet état.
Plus d'informations sur le fonctionnement des pièces et leurs états sont disponibles dans la partie [Cahier des charges](#cahier-des-charges).

## Développement du projet

### Cahier des charges

Lors de la conception du projet, différentes charges ont été définies et le but du développement a été de les implémenter, de préférence de manière optimisée.
Le tableau ci-dessous reprend ces charges.

|        CHARGE       |                                  DESCRIPTION                                  |     IMPLÉMENTÉ     |
|---------------------|-------------------------------------------------------------------------------|:------------------:|
|     Indépendance    | Agent autonome agissant dans l'environnement (grille) de lui-même.            | :heavy_check_mark: |
|     Déplacement     | Agent se déplaçant vers son but de manière optimisée.                         | :heavy_check_mark: |
|   Synchronisation   | Variables globales mises à jour, lues et accédées sans conflit                 | :heavy_check_mark: |
| Objectif individuel | Comportement d'un agent satisfaisant son objectif (se rendre à sa case 'but') | :heavy_check_mark: |
|  Objectif collectif | Comportement des agents satisfaisant un objectif commun (terminer le taquin)  | :heavy_check_mark: |
|  Boîte aux lettres  | Zone de dépôt des messages commune à tous les agents                           | :heavy_check_mark: |
|     Interactions    | Agents échangeant des messages et interagissant entre eux                     | :heavy_check_mark: |
|    Ordonnancement   | Agents répondant et satisfaisant les requêtes des autres messages.            |       :dizzy:      |

#### Indépendance
Concernant l'indépendance, les pièces sont des agents qui ont été implémentés comme des services JavaFX, se comportant comme des threads en background par rapport à la vue (la fenêtre de vue) qui est un thread en foreground.
Plus précisément, ce sont des services périodiques qui sont appelés sur une certaine période. Toutes les X secondes, elles effectuent donc une certaine action selon leur état (déplacement, élaboration d'un message, se pousser, etc) .
Effectivement, les pièces ont été conçues comme des automates et ont donc un état qui évolue au cours de la partie. Nous y ferons référence dans toute la suite de ce rapport.

Lors du démarrage de l'application, ces services périodiques sont instanciés et lancés automatiquement.
Ils s'arrêtent uniquement lorsque la partie est finie (tous les agents sont sur leur case 'but').
L'indépendance des agents est donc validée.

Notons au passage que chaque agent détient toutes les informations disponibles sur l'environnement (grille de jeu), c'est-à-dire sa position actuelle, sa position 'but' et la position de l'ensemble des agents sur la grille.

#### Déplacement
Pour le déplacement des agents, deux critères ont été importants à considérer : l'exploitation et l'exploration.

A chaque instant, l'agent va calculer le prochain mouvement à effectuer (si nécessaire, c'est-à-dire s'il n'est pas déjà sur sa case 'but'). Il est alors dans l'état `MOVING`.
Il va préférer un déplacement qui le rapproche de sa case 'but' (en se basant sur la distance de Manhattan) et qui l'évite de rentrer en collision avec un autre agent (case libre).
Ainsi, en termes d'exploitation, il a le choix entre deux mouvements (sur les quatre possibles) à chaque fois.
S'il n'y a pas de case libre autour de lui qui le rapproche de son but, alors il tirera au sort un des deux mouvements et communiquera avec le voisin qui le bloque pour qu'il se pousse.
Ce comportement permet **l'exploitation** de la meilleure trajectoire vers le but dans la plupart des cas.

Toutefois, il arrive que ce comportement engendre des boucles (notamment lorsque, pour certaines raisons, l'agent qui fait obstacle n'arrive pas à se pousser).
Pour le régler, nous avons alors intégré un aspect **d'exploration** : l'agent va dans 90% des cas effectuer le mouvement optimal évoqué ci-dessus et un mouvement aléatoire dans 10% des cas.
Ce comportement permet de sortir des boucles dans la plupart des cas au bout d'un certain temps.
L'agent est alors susceptible de prendre par la suite un chemin tout à fait différent de celui qu'il comptait prendre initialement et ainsi d'éviter de répéter les mêmes actions qui n'aboutissaient pas.

**Axe d'amélioration**
> Le calcul du meilleur chemin implémenté actuellement n'est pas le plus performant car basique.
> Effectivement, il arrive que le chemin le plus court ne soit pas forcément le meilleur.
> Par exemple, il peut y avoir sur ce dernier des obstacles qui seront la source de nombreuses interactions alors qu'il peut exister un détour vide d'obstacles, représentant un chemin plus long à parcourir mais qui fait à la fin gagner en temps d'exécution puisqu'il y aurait moins d'échanges entre les agents et de traitements qui s'en suivent.
> 
> Nous pourrions à la place de l'algorithme implémenté actuellement imaginer un algorithme A* où le coût serait le nombre de messages à envoyer (à minimiser).

#### Synchronisation
La grille de jeu est synchronisée tout le long de la partie. Cela signifie qu'un seul agent peut accéder au plateau à la fois.
Une fois qu'il a terminé son action, il passe le relais à un autre agent.
Ce comportement est assuré par le mot-clé `synchronized` en Java, placé devant les méthodes susceptibles d'impacter l'état de l'environnement par divers threads en parallèle.
Ainsi, quand un agent accède à la grille de jeu, il est sûr et certain d'accéder au véritable actuel du jeu, évitant les conflits d'accès en lecture et écriture des variables globales.

#### Objectif individuel & collectif
Un agent, à son échelle (localement), a pour objectif d'atteindre sa case 'but'.
Nous avons décrit dans la partie ci-dessus le comportement de déplacement des agents qui lui garantit la satisfaction de son **objectif individuel** puisqu'il va chercher à tout instant à se rapprocher de sa case 'but'.

D'ailleurs, notons qu'une fois qu'il atteint son objectif individuel, il s'immobilise (tout en attendant qu'un autre agent lui envoie éventuellement un message (requête) pour agir de nouveau sur la grille).
Il passe alors de l'état `MOVING` à l'état `SLEEPING`.
Ainsi, il ne fera jamais entrave aux autres et reste un agent actif tout le long de la partie tant que le puzzle n'est pas correctement reconstitué. Ce comportement permet de satisfaire **l'objectif collectif**.

#### Boîte aux lettres
La boîte aux lettre est une fonctionnalité permettant aux différents agents de communiquer entre eux.
Concrètement, il s'agit d'une HashMap avec pour clé un agent et pour valeur la liste des mails que celui-ci a reçus.
De même que le plateau de jeu, elle est synchronisée, permettant à chaque agent de récupérer sa liste de mails à jour en temps et en heure.

Un message posté dans la boîte aux lettres est composé d'un header renseignant sur la date d'envoi du message, l'émetteur et le destinaire ainsi que d'un corps contenant la requête 'PUSH'.
Cette requête renseigne sur l'agent à l'origine de la requête, sa position au moment de l'envoi du message et la case qu'il souhaite atteindre.

#### Interactions
Lorsqu'un agent reçoit un nouveau message, il passe dans l'état `READING` et le traite.
Il va alors examiner la position de l'agent à l'origine de la requête (appelons-le client pour le bien de l'explication) et effectuer un mouvement qui lui permet de s'en éloigner, tout en favorisant les cases libres pour éviter les collisions.
Il se pousse donc.
Toutefois, si de cette manière il atteint la case 'but' de l'agent client, alors il va effectuer un second mouvement pour la libérer. Il se repousse donc une seconde fois.

Après ce(s) mouvement(s), il passera alors dans l'état `WAITING`.
Lorsque l'agent client va effectuer un mouvement (et emprunter le chemin qui vient de se libérer), il va alors repasser dans un état `MOVING` pour à son tour se déplacer vers sa propre case 'but' comme il le faisait avant la réception de la requête.

Si l'agent serveur (celui traitant la requête) n'a pas de case libre autour de lui quand il veut se pousser, alors il va choisir aléatoirement un de ses voisins (excepté le client) pour lui envoyer une requête pour qu'il se pousse d'abord.
Il deviendra alors à son tour client d'un autre agent. Il passera alors dans l'état `WAITING` et attendra que le voisin qui le bloque effectue un mouvement pour repasser dans l'état `MOVING` et effectuer à son tour un mouvement.

Lorsqu'un agent a reçu plusieurs messages, quand c'est à son tour d'agir, il va seulement traiter le dernier message reçu et supprimer les autres.
Cela permet d'éviter à l'agent de traiter d'anciens messages qui ne sont plus forcément d'actualité.

De plus, un agent ayant reçu un message a 30% de chance de l'ignorer et de repasser dans l'état `MOVING`. Ce comportement probabiliste permet d'éviter des boucles dans certains cas ou des traitements inutiles.
Effectivement, il se peut par exemple qu'un premier agent en plein déplacement bloque temporairement un second agent qui lui envoie donc une requête.
Toutefois, dans la pratique, la traiter n'est pas intéressant puisque le premier agent 'comptait' se déplacer encore et allait libérer la place pour le second agent.

Cette part de hasard offre donc la possibilité d'éviter un traitement non désiré.
Elle se prête également bien à la part de hasard dans le déplacement d'un agent lorsqu'il est dans l'état `MOVING`.
Effectivement, plutôt que de se pousser dans une direction totalement aléatoire comme il l'aurait fait dans l'état `SLEEPING`, il a l'opportunité dans l'état `MOVING` de se déplacer vers sa propre case 'but' en plus de libérer son emplacement actuel de manière plus optimisée.

**Axe d'amélioration**
> Tout comme l'axe d'amélioration évoqué pour le déplacement, la fonction de 'poussement' pourrait être optimisée.
> Effectivement, l'agent est poussé dans une direction aléatoire, ce qui peut être gênant pour l'agent client car l'agent poussé pourrait être poussé sur une case incluse dans le chemin que le client voulait emprunter.
> 
> Ainsi, à la place de ce  comportement, nous pourrions imaginer qu'il prendrait en compte la totalité des informations sur l'environnement pour calculer un trajet optimal pour se pousser, de sorte à ce qu'il ne soit jamais poussé sur le chemin que l'agent client serait susceptible de prendre pour se rendre à son but.

Enfin, il arrive que des comportements non désirés surviennent lors de l'interaction de deux agents.

#### Ordonnancement
Effectivement, l'ordonnancement dans ce projet peut être perfectionné. Même si les agents sont synchronisés entre eux, l'ordre dans lequel les agents agissent n'est pas prédéfini ou priorisé.
Ainsi, il est possible qu'un agent qui s'est fait pousser à un instant t se redéplace sur sa case d'origine à l'instant t+1.
L'agent client doit alors renvoyer une requête.

Cette absence d'ordonnancement peut donc être problématique car à l'origine de boucles dont il est parfois difficile de sortir.

**Axe d'amélioration**
> Nous pourrions imaginer l'implémentation d'un jeton pour gérer l'ordonnancement des agents.
> Ce jeton serait transmis entre les agents et seul celui qui le détient pourrait agir dans l'environnement.
> Une fois qu'il aurait agi, il le transmettrait à un autre agent.
> Le jeton serait donné en priorité à un agent qui vient de recevoir une requête.
> De plus, une fois qu'il se serait poussé, il le transmettrait à l'agent client (émetteur de la requête) pour qu'il puisse se déplacer sur la case qui vient de se libérer sans soucis.
> Cette implémentation pourrait résoudre, a priori, le problème d'ordonnancement évoqué précédemment.

### Structure du code

#### Explication des packages

Le package `mailbox` permet la gestion des boites aux lettres et définit la structure des messages qu'il est possible d'envoyer.
Ainsi, il définit notamment une interface `Mailable`. Toute instance d'une classe implémentant cette interface est capable d'envoyer des objets `Message` à d'autres instances de type `Mailable`.
Il définit également les objets `Message` à envoyer, comportant différents headers (ID, date, destinataire, etc) et un corps.

---

Le package `puzzle` définit le jeu en lui-même, à savoir le plateau de jeu et les agents qui s'y déplacent de manière autonomes (classe `AgentPiece`).
Ces agents sont des `Threads` et implémentent différents patterns Observer/Observable qui les rendent capable d'interagir efficacement avec leur environnement (plateau et autres agents).
De plus, ils sont également conçus comme des automates et implémentent donc le design pattern State.

##### Design pattern Observer/Observable
> Le design pattern Observer/Observable consiste en un objet observé par un observeur.
> Lorsque l'objet observé subit des changements qui intéressent l'observeur, alors il le notifie pour l'avertir de ces changements. L'observeur exécutera alors des opérations en réponse à ces changements.

##### Design pattern State
> Le design pattern State consiste à externaliser les traitements d'un automate en dehors de sa propre classe.
> Effectivement, un automate transite entre plusieurs états et effectue généralement un traitement différent pour chacun de ses états.
> Sans design pattern, tous ces traitements seraient regroupés au sein de la classe de l'automate, le nettoyant sous un flot de code dont il ne se sert qu'occassionnellement.

> Avec ce design pattern, un traitement est externalisé dans une classe de type `State`.
> Une instance de cette classe est attachée à l'automate.
> Ainsi, ce n'est plus l'automate qui exécute directement le traitement mais son objet `State`.
> Il y a donc autant de classes de type `State` que l'automate a de traitements à exécuter durant son cycle de vie.
> De plus, un objet `State` est capable de modifier l'état de l'automate auquel il est attaché. Cela permet d'assurer la transition entre les différents états de l'automate.

Brève description du contenu des sous-package :
- `puzzle/action` : outils pour faciliter le déplacement des pièces sur le plateau et le mettre à jour.
- `puzzle/agent` : définition des agents et outils pour faciliter la communication entre eux.
    - `puzzle/agent/state` : définition des états des agents (implémentation du design pattern State).
- `puzzle/pathfinding` : outils pour calculer un chemin entre deux cases du plateau.
- `puzzle/specials` : puzzles prédéfinis (dans un but de tests notamment). 

---

Le package `utility` sert surtout à offrir aux développeurs des outils utiles au debugging (logging).

---

Le package `view` définit l'UI. Lié au modèle (un objet de type `Puzzle` du package `puzzle`), il est automatiquement mis à jour lorsque ce dernier subit des changements.
L'interface a été réalisée avec la librairie JavaFX. Il est à noter que JavaFX rend plus difficile l'utilisation du multi-threading.
Elle définit toutefois des classes `Service` permettant à un objet d'agir comme un thread en background.
Ainsi, les agents ne sont plus des `Thread` mais des `Service` JavaFX (mais leur fonctionnement reste bien évidemment similaire).

## Conclusion
Ce projet a été très intéressant à concevoir et développer, notamment parce qu'il s'agit d'un exercice d'ouverture vers la recherche impliquant différents aspects de cette dernière.

Effectivement, nous avons vu au cours de ce rapport la complexité de développer un taquin, un jeu qui semble pourtant simple au premier abord.
Il mêle des notions complexes de l'informatique, comme des algorithmes de path-finding pour le calcul de chemins optimaux ou la création de jetons pour l'ordonnancement de processus.

Ce projet nous a donc permis de constater des enjeux de la recherche au travers de l'élaboration d'un jeu simple avec des agents autonomes.


## Bibliographie

Article sur la synchronisation des `Threads` en Java : [Différences entre les mots-clés `volatile` et `synchronized`](https://dzone.com/articles/difference-between-volatile-and-synchronized-keywo)
> So, where volatile only synchronizes the value of one variable between the thread memory and the "main" memory, synchronized synchronizes the value of all variables between the thread memory and the "main" memory and locks and releases a monitor to control the ownership between multiple threads.

