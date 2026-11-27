# Participant Superfight pour l'arène

Cette branche reprend le corrigé `step7_figth` et ajoute une application
participante : elle crée un joueur de démonstration en base H2, l'inscrit,
répond au ping et joue une stratégie simple à chaque manche. Deux bots de cet
exemple adoptent des styles complémentaires (attaque ou défense par moments)
pour limiter les matchs nuls ; le style peut changer à chaque combat, sans
garantie d'un vainqueur. Le profil de
démonstration n'utilise ni PostgreSQL ni les données habituelles du projet.
Les données H2 disparaissent à l'arrêt.

## Essai sur un seul poste

Il faut Java 25 et Maven. Avec le JDK 23, ajoutez `-Djava.version=23` aux
commandes Maven ci-dessous. Le formateur lance d'abord l'arène avec l'option
loopback, depuis le dépôt `formation_talosi` :

```bash
cd tools/superfight-arena
export ARENA_ALLOW_LOOPBACK=true
export ARENA_ADMIN_TOKEN="$(openssl rand -hex 32)"
export ARENA_JOIN_CODE="$(openssl rand -hex 16)"
printf 'Code de salle : %s\n' "$ARENA_JOIN_CODE"
printf 'Token formateur : %s\n' "$ARENA_ADMIN_TOKEN"
mvn spring-boot:run
```

Le token formateur reste privé ; transmettez le code de salle par un canal
privé. Si l'arène tourne déjà sans `ARENA_ALLOW_LOOPBACK=true`, il faut la
redémarrer : **toutes les inscriptions et tous les combats sont perdus**.

Dans un second terminal, à la racine de cette branche :

```bash
export APIKEY="$(openssl rand -hex 32)"
export ARENA_JOIN_CODE='code-de-salle-de-l-arene'
mvn -Dspring-boot.run.profiles=arena-demo spring-boot:run
```

Le bot `ArenaBot` démarre sur `127.0.0.1:9090` et s'inscrit auprès de
`http://127.0.0.1:8090`. Dans l'interface de l'arène, vérifiez que son ping est
vert. Pour un combat, ajoutez un deuxième participant ou lancez un deuxième bot
dans un troisième terminal :

```bash
export APIKEY="$(openssl rand -hex 32)"
export ARENA_JOIN_CODE='code-de-salle-de-l-arene'
export SERVER_PORT=9091
export ARENA_CALLBACK_URL=http://127.0.0.1:9091
export ARENA_PLAYER_ALIAS=ArenaBot2
mvn -Dspring-boot.run.profiles=arena-demo spring-boot:run
```

Si aucun code de salle n'est activé, omettez `ARENA_JOIN_CODE`. Ne publiez pas
les tokens ou la clé API ; l'exemple génère une clé privée distincte pour les
autres écritures de chaque application.

## Autres adresses

| Variable | Défaut du profil | Rôle |
|---|---|---|
| `ARENA_URL` | `http://127.0.0.1:8090` | URL de l'arène |
| `ARENA_CALLBACK_URL` | `http://127.0.0.1:9090` | URL à laquelle l'arène appelle ce bot |
| `ARENA_PLAYER_ALIAS` | `ArenaBot` | alias unique du joueur créé |
| `ARENA_PLAYER_POWER` | `60` | puissance du joueur, de 1 à 100 |
| `SERVER_PORT` | `9090` | port local du bot |
| `SERVER_ADDRESS` | `127.0.0.1` | interface locale d'écoute |
| `APIKEY` | obligatoire | clé privée des autres routes d'écriture |
| `ARENA_JOIN_CODE` | vide | code transmis par le formateur si activé |

En LAN, adaptez l'interface et `ARENA_CALLBACK_URL` à l'IPv4 du poste et aux
plages autorisées par l'arène. L'HTTP LAN transporte les secrets en clair ;
utilisez un réseau de formation fiable. Un tunnel Quick Tunnel peut également
fournir une URL HTTPS de callback `*.trycloudflare.com`. Un tunnel expose
toutes les routes de l'application : utilisez uniquement des données fictives,
respectez les règles de votre organisation et gardez `APIKEY` secret.

Le code reprend la solution pédagogique dans `com.talosi.superfight.arena`.
Sans profil `arena-demo`, l'inscription est désactivée par défaut. Pour
réutiliser une base existante, activer `ARENA_ENABLED=true` et configurer
`ARENA_URL`, `ARENA_CALLBACK_URL`, `ARENA_PLAYER_ID` ou `ARENA_PLAYER_ALIAS`,
et si nécessaire `ARENA_JOIN_CODE` ; aucun joueur n'est créé automatiquement.

Après un redémarrage du bot, le formateur doit supprimer son ancienne
inscription avant la nouvelle tentative. Après un redémarrage de l'arène,
redémarrer les bots : les inscriptions et leurs secrets ont été effacés.
Si le ping échoue, vérifier l'adresse, le port, le réseau et le code de salle.
Les secrets ne doivent être ni journalisés ni commités.
