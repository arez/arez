document.documentElement.classList.add('js');
const toggle = document.querySelector('.nav-toggle');
if (toggle) {
  toggle.addEventListener('click', () => {
    const open = document.querySelector('.sidebar').classList.toggle('is-open');
    toggle.setAttribute('aria-expanded', String(open));
  });
}
