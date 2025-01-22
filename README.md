# Documentation - ChatManagementController

## Description
Le contrôleur `ChatManagementController` gère les interactions liées aux prompts, scénarios et personnages dans une application de gestion de génération d'histoires et de scénarios. Il fournit des points d'entrée pour créer, lire, mettre à jour et supprimer des modèles de prompt, des scénarios et des personnages.

## Endpoints

### 1. Génération de scénario
**URL:** `/api/v1/chat/{promptId}/scenariogenerate`  
**Méthode:** `POST`  
**Description:**  
Génère un scénario basé sur un prompt existant.  
**Paramètres:**
- `promptId` (Path) : ID du modèle de prompt.
- `promptmodel_Id` (Body) : ID du modèle de prompt (redondant, à clarifier).
- `requestDTO` (Body) : Contient les détails de la requête.  
  **Retour:**
- Réponse texte représentant le scénario généré.

---

### 2. Création de modèle de prompt
**URL:** `/api/v1/chat/promptcreate`  
**Méthode:** `POST`  
**Description:**  
Crée un nouveau modèle de prompt et retourne la liste mise à jour des modèles existants.  
**Paramètres:**
- `promptModelDTO` (Body) : DTO contenant les informations sur le prompt (titre, description, image).  
  **Retour:**
- Liste de `PromptModelDTO`.

---

### 3. Mise à jour de modèle de prompt
**URL:** `/api/v1/chat/update/{id}`  
**Méthode:** `PUT`  
**Description:**  
Met à jour les informations d'un modèle de prompt existant.  
**Paramètres:**
- `id` (Path) : ID du modèle de prompt.
- `promptModelDTO` (Body) : DTO contenant les nouvelles informations du prompt.  
  **Retour:**
- Message confirmant la mise à jour.

---

### 4. Récupération d'un modèle de prompt par ID
**URL:** `/api/v1/chat/model/{id}`  
**Méthode:** `GET`  
**Description:**  
Récupère les détails d'un modèle de prompt par son ID.  
**Paramètres:**
- `id` (Path) : ID du modèle de prompt.  
  **Retour:**
- Détails du prompt sous forme de `PromptModelDTO`.

---

### 5. Liste des modèles de prompt
**URL:** `/api/v1/chat/promptmodels`  
**Méthode:** `GET`  
**Description:**  
Retourne une liste de tous les modèles de prompt disponibles.  
**Retour:**
- Liste de `PromptModelDTO`.

---

### 6. Historique des scénarios d'un modèle de prompt
**URL:** `/api/v1/chat/historique/{promptModelId}`  
**Méthode:** `GET`  
**Description:**  
Récupère la liste des scénarios associés à un modèle de prompt.  
**Paramètres:**
- `promptModelId` (Path) : ID du modèle de prompt.  
  **Retour:**
- Liste de `ScenarioDTO`.

---

### 7. Liste des scénarios avec titre et trame
**URL:** `/api/v1/chat/scenarioList`  
**Méthode:** `GET`  
**Description:**  
Retourne une liste des scénarios avec leurs titres et trames d'histoire.  
**Retour:**
- Liste de `ScenarioByIDAndTitre`.

---

### 8. Suppression d'un scénario
**URL:** `/api/v1/chat/delete/scenario`  
**Méthode:** `DELETE`  
**Description:**  
Supprime un scénario spécifique.  
**Paramètres:**
- `scenarioID` (Query) : ID du scénario à supprimer.  
  **Retour:**
- Message confirmant la suppression.

---

### 9. Liste des personnages d'un scénario
**URL:** `/api/v1/chat/personnages`  
**Méthode:** `GET`  
**Description:**  
Retourne une liste des personnages associés à un scénario spécifique.  
**Paramètres:**
- `scenarioID` (Query) : ID du scénario.  
  **Retour:**
- Liste de `PersonnageDTO`.

---

### 10. Suppression d'un personnage
**URL:** `/api/v1/chat/delete_personnage`  
**Méthode:** `DELETE`  
**Description:**  
Supprime un personnage spécifique.  
**Paramètres:**
- `personnageID` (Query) : ID du personnage à supprimer.  
  **Retour:**
- Message confirmant la suppression.

---

### 11. Suppression d'un modèle de prompt
**URL:** `/api/v1/chat/delete`  
**Méthode:** `DELETE`  
**Description:**  
Supprime un modèle de prompt.  
**Paramètres:**
- `id` (Body) : ID du modèle de prompt à supprimer.  
  **Retour:**
- Message confirmant la suppression.

---

### 12. Détails d'un scénario
**URL:** `/api/v1/chat/scenarioDetail/{scenarioID}`  
**Méthode:** `GET`  
**Description:**  
Retourne les détails d'un scénario spécifique et la liste des personnages qui lui sont associés.  
**Paramètres:**
- `scenarioID` (Path) : ID du scénario.  
  **Retour:**
- Une paire contenant `ScenarioDTO` et une liste de `PersonnageDTO`.

---

## Méthodes utilitaires

### `getPersonnageDTOS`
**Description:**  
Récupère la liste des `PersonnageDTO` associés à un scénario donné.

