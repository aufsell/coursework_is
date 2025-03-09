<template>
  <div class="search-container">
    <input 
      v-model="query" 
      type="text" 
      placeholder="Поиск любого пива" 
      class="search-bar" 
      @input="fetchResults"
      @focus="showDropdown = true"
      @blur="hideDropdown"
    />
    <div v-if="showDropdown && results.length" class="search-results">
      <BeerCardMini 
        v-for="beer in results" 
        :key="beer.id" 
        :beer="mapBeerData(beer)" 
        @click="goToBeerDetails(beer.id)" 
      class = "zx"/>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

import BeerCardMini from './BeerCardMini.vue';

export default {
  components: {  BeerCardMini},
  data() {
    return {
      query: '',
      results: [],
      showDropdown: false
    };
  },
  methods: {
    async fetchResults() {
      if (this.query.length < 1) {
        this.results = [];
        return;
      }
      try {
        const response = await axios.get(`http://localhost:7777/api/v1/beer/beers/search?`, {
          params: { name: this.query }, withCredentials: true
        });
        this.results = response.data;
      } catch (error) {
        console.error('Ошибка при получении данных:', error);
      }
    },
    hideDropdown() {
      setTimeout(() => { this.showDropdown = false; }, 200);
    },
    goToBeerDetails(beerId) {
      this.$router.push({ name: 'BeerDetail', params: { beerId } });
    },
    mapBeerData(beer) {
      return {
        beerId: beer.id,
        beerName: beer.name,
        averageRating: beer.abv / 2, // Здесь можно заменить на реальные данные рейтинга
        reviewCount: beer.ibu, // Аналогично, заменить на реальные отзывы
        price: beer.price,
        country: beer.country,
        imagePath: beer.image_path
      };
    }
  }
};
</script>

<style scoped>
.search-container {
  position: relative;
  width: 40%;
}
.search-bar {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ccc;
  border-radius: 10px;
}
.search-results {
  position: absolute;
  width: 100%;
  background: white;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  max-height: 300px;
  overflow-y: auto;
  z-index: 10;
}

</style>
