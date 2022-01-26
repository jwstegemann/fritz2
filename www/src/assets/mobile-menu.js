console.log("geladen")

function toggleMenu() {
    console.log("toggle")
    const menu = document.getElementById("mobile-menu");
    if (menu) {
        console.log("toggling")
        menu.classList.toggle("hidden")
    }
}

