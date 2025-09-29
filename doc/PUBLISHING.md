# Publishing

fritz2 artifacts are published to [Maven Central](https://search.maven.org/search?q=g:dev.fritz2).

In order to authenticate, the GitHub actions workflows of this project use a bunch of secrets that are stored
as GitHub repository secrets. Below you can find a guide on how to obtain these secrets.

## Overview of Required Secrets

| Secret Name              | Description                                                           |
|--------------------------|-----------------------------------------------------------------------|
| `OSSRH_USERNAME`         | Your Sonatype token username.                                         |
| `OSSRH_PASSWORD`         | Your Sonatype token password / personal access token.                 |
| `GPG_SIGNING_KEY`        | Your GPG private key for signing artifacts.                           |
| `GPG_SIGNING_PASSPHRASE` | The passphrase for your GPG private key.                              |
| `FRITZ2_WEB_TOKEN`       | Token for deploying the release build of the website to GitHub Pages. |

> [!NOTE]
> Note: The `snapshot`-version of the website is hosted via GitHub pages in _this_ repository, so no additional secrets
> are needed for that.  
> The `release`-version of the website is hosted via GitHub pages in the
> [fritz2-web](https://github.com/jwstegemann/fritz2-web) repository, so an additional secret (`FRITZ2_WEB_TOKEN`) is
> needed for that. It is a personal access token with write access to the `fritz2-web` repository.

## Obtaining Credentials

### 1. `OSSRH_USERNAME`, `OSSRH_PASSWORD`

Log in to your Sonatype account and generate a personal access token.
You will receive a username and password/token to use for publishing.

### 2. `GPG_SIGNING_KEY`, `GPG_SIGNING_PASSPHRASE`

Maven Central requires the submitted artifacts to be signed with GPG.
Below are instructions on how to create a GPG key pair and export the necessary information:

1. **Generate a GPG Key Pair:**
   ```sh
   gpg --full-generate-key
   ```
    - Type: RSA and RSA (or ECC), at least 4096 bits recommended.
    - Choose a passphrase you’ll remember (`GPG_SIGNING_PASSPHRASE`).

2. **Export your GPG Private Key (for signing):**
   ```sh
   gpg --armor --export-secret-keys YOUR_KEY_ID
   ```
    - Replace `YOUR_KEY_ID` with your GPG key’s ID (`gpg --list-secret-keys`).
    - Copy the entire output (including `-----BEGIN PGP PRIVATE KEY BLOCK-----`).
    - This will be the value for `GPG_SIGNING_KEY`.

3. **Export your GPG Public Key (upload to key server):**
   ```sh
   gpg --armor --export YOUR_KEY_ID
   ```
    - Upload this public key to a key server (e.g., [keyserver.ubuntu.com](https://keyserver.ubuntu.com/)) so Maven
      Central can verify your signature.
