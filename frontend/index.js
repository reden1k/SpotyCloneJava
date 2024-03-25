const copyButton = document.getElementById('copyButton');

copyButton.addEventListener('click', () => {
  const url = window.location.href;
  navigator.clipboard.writeText(url);
});

const myButton = document.getElementById('myButton');

myButton.addEventListener('click', () => {
  myButton.classList.toggle('clicked');
  setTimeout(() => {
        myButton.classList.toggle('unclicked');
        setTimeout(() => {
          myButton.classList.toggle('unclicked');
          myButton.classList.toggle('clicked');
        }, 750);
      }, 3000);
      
});