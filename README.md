# Private Repository: API Recipe Management

This repository contains API recipes, some of which are **publicly shared** while others are for **internal use only**. We use a **dual-branch and dual-repo approach** to keep internal and public recipes separate while maintaining a **single source of truth**.

## 📌 Workflow Overview

1. **Adding a new public recipe**
   - Added to `main` (private repo).
   - Merged into `public-main` (private repo).
   - Synced with `main` in the **public repo**.

2. **Adding a private-only recipe**
   - Added to `main` (private repo).
   - Stays in `main` (private repo) and does **not** appear in `public-main` or the public repo due to `.gitignore` rules.

We use `.gitattributes` to **protect critical files** (like `README.md` and `.gitignore`) from being overridden when merging `main` into `public-main`.

---

## 🏗 **Adding a New Public Recipe**

### **1️⃣ Add the Recipe to `main` (Private Repo)**
```sh
git checkout main
touch src/main/java/com/monarkmarkets/NewRecipe.java
git add src/main/java/com/monarkmarkets/NewRecipe.java
git commit -m "Add new public recipe"
git push origin main
```

### **2️⃣ Merge `main` into `public-main` (Private Repo)**
```sh
git checkout public-main
git merge --no-ff main
git push origin public-main
```

### **3️⃣ Push `public-main` to the Public Repo**
```sh
git push public public-main:main
```

🚀 **Result:** The new recipe is now **available in the public repo**.

---

## 🔒 **Adding a Private-Only Recipe**

### **1️⃣ Add the Recipe to `main` (Private Repo)**
```sh
git checkout main
touch src/main/java/com/monarkmarkets/internal/NewPrivateRecipe.java
git add src/main/java/com/monarkmarkets/internal/NewPrivateRecipe.java
git commit -m "Add new private recipe"
git push origin main
```

### **2️⃣ Ensure It Is Ignored in `public-main`**
Since `.gitignore` contains:
```
**/internal/**
```
this file will **never be included in `public-main` or the public repo**.

### **3️⃣ Merge `main` into `public-main` (Private Repo)**
```sh
git checkout public-main
git merge --no-ff main
git push origin public-main
```

Since the internal files are ignored, **they will not be merged into `public-main` or pushed to the public repo**.

---

## ⚠ **How `.gitattributes` Prevents Overwrites**
We use `.gitattributes` to **protect** `README.md` and `.gitignore`:

```plaintext
README.md merge=ours
.gitignore merge=ours
```
### **Effect When Merging `main` into `public-main`**
- The **public README.md** in `public-main` will remain unchanged, even if the `main` README is different.
- `.gitignore` rules in `public-main` stay intact, ensuring that ignored files remain excluded.

---

## **🔄 Keeping Everything in Sync**

### **After Adding or Updating Recipes**
1. **Ensure `main` (private repo) is up to date:**
   ```sh
   git checkout main
   git pull origin main
   ```

2. **Merge `main` into `public-main` (excluding private recipes):**
   ```sh
   git checkout public-main
   git merge --no-ff main
   git push origin public-main
   ```

3. **Push the clean `public-main` branch to the public repo:**
   ```sh
   git push public public-main:main
   ```

---
