# Travail Pratique #2: Password Manager

## Auteurs
- ABCD1234, NURY, Yanis
- VACR21030400, VACHALDE, Rémi

## Compatibilité
- Langage : Java - version 20
- Outil de gestion des dépendances : Maven

## Utilisation

### Pré-requis
1. **Java Development Kit (JDK)**  
   - Version requise : 17 ou supérieure  
   - Téléchargement : [OpenJDK](https://jdk.java.net/) ou [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)

2. **Apache Maven**  
   - Vérifiez que Maven est installé :  
     ```bash
     mvn -v
     ```  
   - Si non, installez-le en suivant les instructions officielles : [Installation de Maven](https://maven.apache.org/install.html)

3. **SQLite JDBC**  
   - La bibliothèque `sqlite-jdbc` est automatiquement gérée par Maven grâce au fichier `pom.xml`. Aucune action supplémentaire n'est nécessaire.

4. **Fichier `.env`**  
   - Créez un fichier `.env` à la racine du projet en suivant le modèle fourni dans `.env.example`.
   - Exemple :
     ```plaintext
     DB_PASSWORD=votre_mot_de_passe
     ```

### Installation
1. **Cloner le projet :**  
   ```bash
   git clone https://github.com/votre-repo/password-manager.git
   cd password-manager
   ```

2. **Construire le projet avec Maven :**
   ```bash
   mvn clean install
   ```

3. **Exécuter le projet :**
   ```bash
   java -jar target/password-manager.jar
   ```

### Fonctionnalités principales
- **Inscription d'un utilisateur :**
    - Commande :
      ```bash
      passmanager -r <username>
      ```
    - Vous serez invité à entrer un mot de passe maître pour l'utilisateur.

- **Ajouter un mot de passe :**
    - Commande :
      ```bash
      passmanager -u <username> -a <label> <password>
      ```
    - Vous serez invité à entrer le mot de passe maître pour confirmer.

- **Afficher un mot de passe :**
    - Commande :
      ```bash
      passmanager -u <username> -s <label>
      ```

### Structure du projet
- **`src/main/java`**
    - Contient le code source du projet, structuré en différentes classes telles que `main.java.com.example.passwordmanager.PasswordManager`, `main.java.com.example.passwordmanager.DatabaseManager`, `main.java.com.example.passwordmanager.CryptoUtils`, etc.
- **`src/main/resources`**
    - Contient les fichiers de configuration, comme le modèle `.env.example`.

### Exemple d'utilisation
```bash
# Inscription d'un utilisateur
$ passmanager -r alice
Enter master password: ****
User alice registered successfully!

# Ajout d'un mot de passe pour alice
$ passmanager -u alice -a email password123
Enter alice master password: ****
Password email successfully saved!

# Affichage du mot de passe enregistré
$ passmanager -u alice -s email
Enter alice master password: ****
Password email is: password123
```

## Dépendances
- **sqlite-jdbc** (version 3.43.2.0)  
  Gestion de la base de données SQLite.
    - Ajoutée via Maven :
      ```xml
      <dependency>
          <groupId>org.xerial</groupId>
          <artifactId>sqlite-jdbc</artifactId>
          <version>3.43.2.0</version>
      </dependency>
      ```

- **dotenv-java**  
  Chargement des variables d'environnement à partir du fichier `.env`.
    - Ajoutée via Maven :
      ```xml
      <dependency>
          <groupId>io.github.cdimascio</groupId>
          <artifactId>dotenv-java</artifactId>
          <version>2.2.0</version>
      </dependency>
      ```

---

## Notes supplémentaires
- **Journalisation :**  
  Si vous voyez un message concernant SLF4J ("No SLF4J providers were found"), il s'agit d'une configuration manquante pour la journalisation. Cela n'affecte pas le fonctionnement du projet.

- **Sécurité :**  
  Veillez à ne pas compromettre votre fichier `.env` lors de la distribution de votre projet.

## Contact
Pour toute question ou contribution, n'hésitez pas à créer une *issue* ou à nous contacter directement.
