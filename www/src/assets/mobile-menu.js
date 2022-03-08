const menu = document.getElementById("mobile-menu");
const menuIcons = document.getElementsByClassName("mobile-icon")

function toggleMenu() {
    if (menu) {
        menu.classList.toggle("-translate-y-[100vh]")
    }
    for(let i = 0; i < menuIcons.length; i++) {
        menuIcons[i].classList.toggle("hidden")
    }
}

