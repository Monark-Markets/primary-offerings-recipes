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

### **3️⃣ Push to the Public Repo via Pull Request**

Since the public repo's `main` branch is protected, you need to push to a temporary branch and create a PR:

```sh
# Push public-main to a new branch on the public repo
git push public public-main:sync/update-recipes

# Create a PR in the public repo (requires gh CLI)
gh pr create --repo Monark-Markets/primary-offerings-recipes \
  --head sync/update-recipes --base main \
  --title "Sync recipes from internal repo" \
  --body "Sync latest recipe changes from the internal repository."
```

Then merge the PR on GitHub (or via CLI):

```sh
gh pr merge --repo Monark-Markets/primary-offerings-recipes --merge
```

After merging, clean up the remote branch:

```sh
git push public --delete sync/update-recipes
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

Some files (e.g., CI/CD workflows, Postman collections) exist in `main` but should **never be merged** into `public-main`. To prevent this:

1. Remove the file/directory from `public-main`:
   ```sh
   git checkout public-main
   git rm --cached -r <path>
   git commit -m "Remove <path> from public-main"
   ```

2. Add it to `.gitignore` in `public-main` (prevents it from being re-tracked):
   ```
   <path>
   ```

3. Add it to `.gitattributes` in `public-main` (prevents merge conflicts):
   ```
   <path> merge=ours
   ```

### Currently excluded from `public-main`

| Path | Reason |
|------|--------|
| `**/internal/**` | Internal-only recipes |
| `.github/workflows/recipes-daily-build.yml` | Internal CI/CD |
| `postman/` | Internal Postman collections |
| `README.md` | Public repo has its own README |
| `.gitignore` | Public repo has its own .gitignore |

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

3. **Push to the public repo via PR:**
   ```sh
   git push public public-main:sync/update-recipes
   gh pr create --repo Monark-Markets/primary-offerings-recipes \
     --head sync/update-recipes --base main \
     --title "Sync recipes from internal repo" \
     --body "Sync latest recipe changes from the internal repository."
   # After merging the PR:
   git push public --delete sync/update-recipes
   ```

---

## 🧨 Handling Diverged History in Public Repo

Since the public repo's `main` branch is protected, you cannot force-push to it. If `public-main` and the public repo's `main` diverge (e.g., due to Dependabot PRs merged directly on the public repo), you need to reconcile them:

1. **Fetch the latest from the public repo:**
   ```sh
   git fetch public
   ```

2. **Merge the public repo's main into public-main:**
   ```sh
   git checkout public-main
   git merge public/main
   # Resolve any conflicts, then:
   git push origin public-main
   ```

3. **Then proceed with the normal sync workflow** (merge `main` into `public-main`, push via PR).

---

💡 **Questions or improvements?** Reach out to the internal development team.