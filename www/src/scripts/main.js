import '../styles/main.css';
import version from './version';

// renders the version from version.js
(() => { window.document.getElementById("f2-version").textContent = version; })();