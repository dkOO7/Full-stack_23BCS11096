// Select elements
const toggleBtn = document.querySelector('.theme-toggle');
const body = document.body;

// Apply saved theme if exists
if (localStorage.getItem('theme') === 'dark') {
  body.classList.add('dark-theme');
  toggleBtn.textContent = "☀️ Light Mode";
}

// Toggle theme on button click
toggleBtn.addEventListener('click', () => {
  body.classList.toggle('dark-theme');

  if (body.classList.contains('dark-theme')) {
    toggleBtn.textContent = "☀️ Light Mode";
    localStorage.setItem('theme', 'dark');
  } else {
    toggleBtn.textContent = "🌙 Dark Mode";
    localStorage.setItem('theme', 'light');
  }
});

// Auto-detect system theme if user hasn’t chosen yet
if (!localStorage.getItem('theme')) {
  if (window.matchMedia("(prefers-color-scheme: dark)").matches) {
    body.classList.add('dark-theme');
    toggleBtn.textContent = "☀️ Light Mode";
  }
}
// Toggle Dark Mode Functionality
const themeToggleButton = document.querySelector('.theme-toggle');
themeToggleButton.addEventListener('click', () => {
  document.body.classList.toggle('dark-mode');
});

// You can add additional event listeners and functions as needed
