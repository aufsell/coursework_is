<template>
  <section class="favourites-beers">
    <div class="xz">
      <img src="../assets/beerlogo.png" alt="Кружка пива" />
      <div class="description">
        <h2><b>Пиво, рекомендуемое вам</b></h2>
      </div>
    </div>
    <div class="beer-grid">
      <BeerCard
        v-for="beer in favouriteBeers"
        :key="beer.beerId"
        :beer="beer"
      />
    </div>
  </section>
</template>

<script>
import axios from "axios";
import BeerCard from "./BeerCard.vue";
import { toRaw } from "vue";

export default {
  name: "RecommendationBeers",
  components: {
    BeerCard,
  },

  data() {
    return {
      beers: [],
    };
  },
  computed: {
    currentUserId() {
      return localStorage.getItem("userId");
    },
    favouriteBeers() {
      return this.beers.slice(0, 8);
    },
  },
  watch: {
    profileUserId(newId, oldId) {
      if (newId !== oldId) {
        this.loadFavouriteBeers();
      }
    },
  },
  created() {
    this.loadFavouriteBeers();
  },
  methods: {
    async loadFavouriteBeers() {
      try {
        const response = await axios.get(
          `http://localhost:7777/api/v1/tasteProfile`,
          { withCredentials: true }
        );
        this.beers = toRaw(response.data).map((beer) => ({
          beerId: beer.beerId,
          beerName: beer.name,
          imagePath: `${beer.imagePath}`,
          price: beer.price,
          averageRating:
            beer.averageRating != null && !isNaN(beer.averageRating)
              ? beer.averageRating
              : 3.0,
          country: beer.country,
          reviewCount: beer.srm,
        }));
        console.log("Избранное пиво загружено:", this.beers);
      } catch (error) {
        console.error("Ошибка при загрузке избранного пива:", error);
      }
    },
  },
};
</script>

<style scoped>
.favourites-beers {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5vw;
  width: 100%;
  margin-bottom: 50px;
}

.xz {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
}

h2 {
  font-size: 20px;
}

h2 img {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}

.beer-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.description {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: start;
  align-content: start;
}
</style>
