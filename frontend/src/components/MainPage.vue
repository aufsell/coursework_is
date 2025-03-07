<template>
      <main>
        <Header />
        <TopBeers />
        <TopRussianMeads />
      </main>
  </template>
  
  <script>
  import Header from './Header.vue';
  import TopBeers from './PopularBeer.vue';
  import TopRussianMeads from './TopRussianMeed.vue';
  
  export default {
    components: {
      Header,
      TopBeers,
      TopRussianMeads
    }
  }
  </script>
  
  <style>
  body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #fff;
  }
  .container {
    width: 100%;
  }
  main{
  display: flex;
    flex-direction: column;
  justify-content: center;
  align-items: center;
}

  </style>