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

## 🔗 Setting Up the Public Remote

This repo uses two remotes:

- `origin`: the internal/private repository
- `public`: the public-facing repository

To add the public remote:

```sh
git remote add public git@github.com:Monark-Markets/primary-offerings-recipes.git
```

You can verify with:

```sh
git remote -v
```

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

To ensure files like `README.md` and `.gitignore` in `public-main` are not overwritten by changes in `main`, we use:

```gitattributes
README.md merge=ours
.gitignore merge=ours
```

This configuration must be placed in the `.gitattributes` file **in the `public-main` branch**.

### **Effect When Merging `main` into `public-main`**
- The **public README.md** remains unchanged.
- The `.gitignore` in `public-main` stays intact.
- Any other sensitive or public-facing overrides can be handled similarly.

---

## ⛔ **Excluding Specific Files from Public Repo**

Some files (e.g., CI/CD workflows) exist in `main` but should **never be merged** into `public-main`. To prevent this:

1. Remove the file from `public-main`:
   ```sh
   git checkout public-main
   git rm --cached .github/workflows/recipes-daily-build.yml
   git commit -m "Remove internal workflow from public-main"
   ```

2. Add it to `.gitignore` in `public-main`:
   ```
   .github/workflows/recipes-daily-build.yml
   ```

3. Optionally protect it further in `.gitattributes`:
   ```
   .github/workflows/recipes-daily-build.yml merge=ours
   ```

---

## 🔄 **Keeping Everything in Sync**

### **After Adding or Updating Recipes**
1. **Ensure `main` is up to date:**
   ```sh
   git checkout main
   git pull origin main
   ```

2. **Merge `main` into `public-main`:**
   ```sh
   git checkout public-main
   git merge --no-ff main
   git push origin public-main
   ```

3. **Push to the public repo:**
   ```sh
   git push public public-main:main
   ```

---

## 🧨 Handling Diverged History in Public Repo

Sometimes the public repo's `main` branch gets out of sync with `public-main`. If you get a non-fast-forward error when pushing:

```sh
git push public public-main:main
# → error: failed to push some refs to ...
```

To resolve it (⚠️ only if you're sure `public-main` is the correct version):

```sh
git push public public-main:main --force
```

This will **overwrite** the public repo's `main` branch with the contents of `public-main`.

---

💡 **Questions or improvements?** Reach out to the internal development team.