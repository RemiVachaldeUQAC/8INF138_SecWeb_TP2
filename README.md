# Travail Pratique #2: Password Manager

## Auteurs
- **Code permanent** : VACR21030400, **Nom** : VACHALDE, **Prénom** : Rémi
- **Code permanent** : ______, **Nom** : NURY, **Prénom** : Yanis

## Compatibilité
- **Langage** : Java 20 ou supérieur
- **Environnement de développement** : IntelliJ IDEA

## Description

**Password Manager** est une application de gestion de mots de passe basée sur une interface de ligne de commande (CLI). Elle permet de créer des utilisateurs, d’ajouter des mots de passe, de consulter des mots de passe existants, ainsi que de modifier ou supprimer des labels et leurs mots de passe.

### Fonctionnalités principales
1. **Créer un utilisateur** : Enregistrez un utilisateur avec un mot de passe maître.
2. **Ajouter un mot de passe** : Associez un mot de passe à un label pour un utilisateur donné.
3. **Afficher un mot de passe** : Récupérez le mot de passe d'un label spécifique.
4. **Supprimer un label** : Supprimez un label et son mot de passe.
5. **Modifier un label** : Changez le nom d’un label existant.
6. **Modifier un mot de passe** : Mettez à jour le mot de passe associé à un label.

## Utilisation

### Prérequis

1. **Java Development Kit (JDK)**  
   Téléchargez et installez [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html).  
   Assurez-vous que Java est bien installé en exécutant :
   ```bash
   java -version
   ```
   La version doit être **20 ou supérieure**.

2. **IntelliJ IDEA**  
   Téléchargez et installez [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/).

3. **Configuration des bibliothèques**
    - Les bibliothèques nécessaires sont situées dans le dossier `libs` adjacent au projet.
    - Configurez-les dans IntelliJ IDEA :
        - Allez dans **File > Project Structure > Libraries**.
        - Ajoutez les fichiers `.jar` depuis le dossier `libs`.

---

### Installation

1. Clonez ou téléchargez le projet dans votre environnement local. Ou téléchargez le projet sous la forme d'un fichier `.zip` ou `.tar.gz` et décompressez-le.
   ```bash
   git clone https://github.com/RemiVachaldeUQAC/8INF138_SecWeb_TP2.git
   cd passmanager
   ```

2. Ouvrez le projet dans IntelliJ IDEA.

3. Configurez les bibliothèques en suivant les instructions ci-dessus.

4. Exécutez la classe `Main` pour lancer l'application.

---

### Commandes Disponibles

#### 1. Créer un utilisateur
```bash
passmanager -r <username>
> enter_user_password
```
Crée un nouvel utilisateur avec un mot de passe maître.

#### 2. Ajouter un mot de passe
```bash
passmanager -u <username> -a <label> <password>
> enter_user_password
```
Ajoute un mot de passe pour un label spécifique.

#### 3. Afficher un mot de passe
```bash
passmanager -u <username> -s <label>
> enter_user_password
```
Affiche le mot de passe associé à un label.

#### 4. Supprimer un label
```bash
passmanager -u <username> -d <label>
> enter_user_password
```
Supprime un label (et son mot de passe) pour un utilisateur donné.

#### 5. Modifier un label
```bash
passmanager -u <username> -ul <label>
> enter_user_password
> enter_new_label
```
Change le nom d'un label existant.

#### 6. Modifier un mot de passe
```bash
passmanager -u <username> -up <label>
> enter_user_password
> enter_new_password
```
Met à jour le mot de passe associé à un label.

---

## Sécurité

- Les mots de passe sont chiffrés avant d'être stockés dans la base de données SQLite.
- L'accès aux mots de passe est protégé par un mot de passe maître.
- Les données utilisateur sont également sécurisées avec un système de chiffrement AES.

---

## Fonctionnalités Bonus
Nous avons ajouté des fonctionnalités supplémentaires pour enrichir le projet :

### 1. **Gestion des mots de passe faibles**
- Lors de l'ajout d'un mot de passe, le programme vérifie si celui-ci est considéré comme faible (trop court, absence de chiffres, caractères spéciaux, etc.).
- Si un mot de passe faible est détecté, l'utilisateur reçoit un avertissement et doit choisir un mot de passe plus fort.

### 2. **Générateur de mots de passe forts**
- Un générateur intégré permet de créer des mots de passe forts aléatoires. Ces mots de passe respectent les règles de sécurité (longueur, complexité, etc.).
- Accessible via la commande lors de la création ou modification d’un mot de passe.

### 3. **Gestion des tentatives avec timeout**
- L'utilisateur a **3 essais** pour entrer le mot de passe maître. Après 3 échecs, un **timeout de 30 secondes** est imposé avant de réessayer.
- Cela renforce la sécurité contre les attaques par force brute.

### 4. **Suppression de mots de passe**
- Les labels (et leur mots de passe associés) peuvent être supprimés avec la commande :
  ```bash
  passmanager -u <username> -d <label>
  ```
- Cette fonctionnalité respecte les pratiques du CRUD.

### 5. **Modification des labels**
- Les noms des labels peuvent être modifiés avec la commande :
  ```bash
  passmanager -u <username> -ul <label>
  ```
- Cette modification respecte les pratiques du CRUD.

### 6. **Modification des mots de passe des labels**
- Les mots de passe associés à un label peuvent être modifiés avec la commande :
  ```bash
  passmanager -u <username> -up <label>
  ```
- Cette fonctionnalité respecte également les pratiques du CRUD.

---

## Licence

Ce projet est sous licence MIT. Consultez le fichier `LICENSE` pour plus d'informations.  