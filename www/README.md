# Minimal Starter for 11ty 

## Installation

Install the needed artifacts and dependencies:

```
npm install
```

### Additional steps on Windows

In order to run 11ty locally on Windows systems, just install git to get the git bash.
Then activate this as default shell for 11ty:

```
npm config set script-shell "C:\\Program Files\\git\\bin\\bash.exe"
```

If running 11ty locally fails it might be because the current OpenSSL version is unsupported.
In that case, run the following command before starting running 11ty locally:
```
set NODE_OPTIONS=--openssl-legacy-provider 
```

## Run locally

Watches for changes and serves locally on http://localhost:8080

```
npm run serve
```

## Create a production build

```
npm run build
```
