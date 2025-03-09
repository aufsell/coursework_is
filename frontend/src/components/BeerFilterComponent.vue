<template>
  <div class="zx"><Header class="xz"/></div>
  <div class="main-container">
    <!-- Фильтры -->
    <div class="filters-container">
      <form @submit.prevent="applyFilters">
        <div>
          <label for="priceMin">Price Min:</label>
          <input type="number" v-model="filters.priceMin" id="priceMin" placeholder="Min price" />
        </div>
        <div>
          <label for="priceMax">Price Max:</label>
          <input type="number" v-model="filters.priceMax" id="priceMax" placeholder="Max price" />
        </div>
        <div>
          <label for="ratingMin">Rating Min:</label>
          <input type="number" v-model="filters.ratingMin" id="ratingMin" placeholder="Min rating" />
        </div>
        <div>
          <label for="ratingMax">Rating Max:</label>
          <input type="number" v-model="filters.ratingMax" id="ratingMax" placeholder="Max rating" />
        </div>
        <div>
          <label for="srmMin">SRM Min:</label>
          <input type="number" v-model="filters.srmMin" id="srmMin" placeholder="Min SRM" />
        </div>
        <div>
          <label for="srmMax">SRM Max:</label>
          <input type="number" v-model="filters.srmMax" id="srmMax" placeholder="Max SRM" />
        </div>
        <div>
          <label for="ibuMin">IBU Min:</label>
          <input type="number" v-model="filters.ibuMin" id="ibuMin" placeholder="Min IBU" />
        </div>
        <div>
          <label for="ibuMax">IBU Max:</label>
          <input type="number" v-model="filters.ibuMax" id="ibuMax" placeholder="Max IBU" />
        </div>
        <div>
          <label for="abvMin">ABV Min:</label>
          <input type="number" v-model="filters.abvMin" id="abvMin" placeholder="Min ABV" />
        </div>
        <div>
          <label for="abvMax">ABV Max:</label>
          <input type="number" v-model="filters.abvMax" id="abvMax" placeholder="Max ABV" />
        </div>
        <div>
          <label for="country">Country:</label>
          <select v-model="filters.country" id="country">
            <option value="">Select Country</option>
            <option v-for="country in countries" :key="country" :value="country">{{ country }}</option>
          </select>
        </div>
        <div>
          <label for="fermentationType">Fermentation Type:</label>
          <select v-model="filters.fermentationType" id="fermentationType">
            <option value="">Select Fermentation Type</option>
            <option v-for="type in fermentationTypes" :key="type" :value="type">{{ type }}</option>
          </select>
        </div>
        <button type="submit">Apply Filters</button>
      </form>
    </div>

    <!-- Карточки пива -->
    <div class="beer-cards-container" v-if="beers.length">
      <BeerCard v-for="beer in beers" :key="beer.beerId" :beer="beer" />
    </div>
    <div v-else class="f">
      <p>Ничего не найдено</p>
    </div>
    
  </div>
  <div class="pagination">
  <button @click="prevPage" :disabled="page === 0">Previous</button>
  <span>Page {{ page + 1 }} of {{ totalPages }}</span>
  <button @click="nextPage" :disabled="page >= totalPages - 1">Next</button>
</div>
</template>

<script>
import Header from './Header.vue';
import BeerCard from './BeerCard.vue'; // Убедитесь, что компонент BeerCard импортирован
import axios from 'axios';

export default {
  components: { Header, BeerCard },
  data() {
  return {
    filters: {
      priceMin: null,
      priceMax: null,
      ratingMin: null,
      ratingMax: null,
      srmMin: null,
      srmMax: null,
      ibuMin: null,
      ibuMax: null,
      abvMin: null,
      abvMax: null,
      country: null,
      fermentationType: null
    },
    countries: ['USA', 'Germany', 'Italy'],
    fermentationTypes: ['Lager', 'Ale', 'Stout', 'Porter'],
    beers: [],
    page: 0, 
    size: 9, 
    totalPages: 1 
  };
},
  async created() {
    try {
      const response = await axios.get('http://localhost:7777/api/v1/beer/search', {
      params: {
        ...this.filters,
        page: this.page,
        size: this.size
      },
      withCredentials: true
    });
      this.beers = response.data.content;
    this.totalPages = response.data.totalPages;
    } catch (error) {
      console.error(error);
    }
  },
  methods: {
    async applyFilters() {
  try {
    const response = await axios.get('http://localhost:7777/api/v1/beer/search', {
      params: {
        ...this.filters,
        page: this.page,
        size: this.size
      },
      withCredentials: true
    });
    this.beers = response.data.content;
    this.totalPages = response.data.totalPages;
  } catch (error) {
    console.error(error);
  }
  },
  nextPage() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.applyFilters();
    }
  },
  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.applyFilters();
    }
  }
  }
};
</script>

<style scoped>
.main-container {
  display: flex;
  justify-content: space-between;
  width: 70vw; /* Добавьте это */
}

.filters-container {
  width: 25%;
  padding: 20px;
  ;
  border-radius: 10px;
}

.beer-cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  width: 70%;
}

form div {
  margin-bottom: 10px;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
}

button:hover {
  background-color: #0056b3;
}
.f{
  display: grid;
  padding-right: 20vw;
  padding-top: 20vh;
}
select, input[type="number"] {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
  margin-bottom: 50px;
}

.pagination button {
  padding: 10px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination span {
  font-size: 16px;
  font-weight: bold;
}

.zx{
  display: flex;
  width: 70%;
}


</style>
