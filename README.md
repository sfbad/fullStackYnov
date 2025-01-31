#####              Documentation
Prérequis
Avant de lancer l'application, assurez-vous d'avoir les éléments suivants :

## JDK 17 : L'application nécessite Java 17. 
    Vous pouvez télécharger et installer la dernière version de OpenJDK 17 si ce n'est pas déjà fait.

## Serveur de base de données MySQL : 
    Vous devez avoir MySQL installé et activé sur votre machine. Si vous n'avez pas MySQL, vous pouvez l'installer depuis ici.

## L'IA  Ollama version 3.2 : 
    Cette application utilise l'IA Ollama pour certaines fonctionnalités. Vous devez installer la version 3.2 (ou une version compatible). 
    Vous pouvez l'activer en utilisant la commande suivante dans votre terminal :
     ollama run llama3.2
## Si vous avez l'application Ollama installée, vous pouvez aussi la lancer directement depuis l'interface graphique.

## Base de données MySQL : 
    L'application utilise une base de données MySQL locale appelée "bandir". Vous devez créer cette base de données dans votre serveur MySQL. 
    Pour cela, connectez-vous à MySQL et créez la base de données en exécutant la commande suivante :
##     CREATE DATABASE bandir;

######              Configuration
    Allez dans le dossier src/main/resources du projet et ouvrez le fichier 
      application.properties.
    Assurez-vous que le fichier contient les informations suivantes pour la connexion à la base de données :
           spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver 
           spring.datasource.url=jdbc:mysql://localhost:3306/bandir
           spring.datasource.username=votre_nom_utilisateur_mysql
           spring.datasource.password=votre_mot_de_passe_mysql
      Remplacez votre_nom_utilisateur_mysql et votre_mot_de_passe_mysql par vos informations d'identification MySQL.

#####      Lancer l'application
    Une fois que vous avez configuré la base de données et Ollama, vous pouvez démarrer l'application.
    L'API sera disponible sur le port par défaut 8080. 
    Pour vérifier que l'application fonctionne, ouvrez votre navigateur et accédez à l'URL suivante :
##                  http://localhost:8080
                    L'URL de base pour acceder aux ressource est http://localhost:8080/api/v1/chat/.....

#####       Remarque
    Si vous avez des problèmes avec MySQL ou Ollama, assurez-vous que les services sont bien lancés avant de démarrer l'application. 
    Vous pouvez vérifier l'état des services MySQL et Ollama en utilisant les commandes appropriées dans votre terminal.