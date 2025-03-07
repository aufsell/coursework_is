<template>
  <header>
    <div class="logo">PIPIVO</div>
    <input type="text" placeholder="Поиск любого пива" class="search-bar" />

    <nav>
      <ul>
        <li class="active">Пиво</li>
        <li>Фильтры</li>
      </ul>
    </nav>
    <div class="nav-icons">
      <img src="../assets/active.png" alt="Уведомления" />
      <img src="../assets/user.png" alt="Профиль" />
    </div>
  </header>
</template>

<script>
export default {
  name: "AppHeader",
};
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
