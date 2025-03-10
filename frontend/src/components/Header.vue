<template>
    <header>
      <div class="logo" @click="goToMain">PIPIVO</div>
      <SearchBar/>
    
      <nav>
        <ul>
          <li class="active" @click="goToMain">Пиво</li>
          <li @click="goToFilter">Фильтры</li>
        </ul>
      </nav>
      <div class="nav-icons">
        <img src="../assets/active.png" alt="Уведомления">
        <img src="../assets/user.png" alt="Профиль" @click="goToProfile">
      </div>
    </header>
  </template>
  
  <script>

  import SearchBar from './SearchBar.vue';
  export default {
    name: 'AppHeader',
    components
    : {
      SearchBar
    }
  ,
  
  methods: {
    goToFilter() {
      this.$router.push('/filters');
    },
    goToProfile() {
      this.$router.push("/profile");
    },
    goToMain() {
      this.$router.push('/mainpage');
  }
}
  }
  </script>
  


<style scoped>
header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  padding: 10px 0px;

  margin-bottom: 40px;
}
.logo {
  font-size: 30px;
  font-weight: bold;
  color: orange;
}
.search-bar {
  padding: 5px 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
}
.nav-icons img {
  width: 24px;
  height: 24px;
  margin-left: 10px;
}
nav ul {
  list-style: none;
  display: flex;
  gap: 20px;
  margin: 0;
  padding: 0;
}
nav ul li {
  font-weight: bold;
  cursor: pointer;
}
nav ul li.active {
  color: orange;
}
</style>
