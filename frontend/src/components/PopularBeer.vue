<template>
  <section>
    
    <div class="xz">
      <img src="../assets/beerlogo.png" alt="Кружка пива">
      <div class="description">
        <h2><b>Популярное пиво</b></h2>
        <p>Самое рейтинговое пиво всех времён</p>
      </div>
    </div>
    <div v-if="loading">
      <p>Загрузка...</p>
    </div>
    <div v-else class="beer-grid">
      <BeerCard v-for="beer in beers" :key="beer.beerId" :beer="beer" />
    </div>
  </section>
</template>

<script>
import BeerCard from './BeerCard.vue';

export default {
  name: 'TopBeers',
  components: {
    BeerCard
  },
  data() {
    return {
      loading: true,
      beers: []
    };
  },
  async created() {
    try {
      const response = await fetch('http://localhost:7777/api/v1/beer/top', {
        method: 'GET',
        credentials: 'include'
      });
      if (!response.ok) {
        throw new Error(`Ошибка HTTP: ${response.status}`);
      }
      this.beers = await response.json();
    } catch (error) {
      console.error('Ошибка загрузки популярных сортов пива:', error);
    } finally {
      this.loading = false;
    }
  }
};
</script>

<style scoped>
section {
  margin-bottom: 40px;
}
h2 {
  font-size: 20px;
}

.beer-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.xz {
  display: flex;
  align-items: center;
  justify-content: left;
  margin-bottom: 20px;
  padding-left: 0;
}

h2 img {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}

.description {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: start;
}

main {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
