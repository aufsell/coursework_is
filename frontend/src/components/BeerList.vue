<script setup>
import { ref, onMounted } from "vue";
import BeerCard from "./BeerCard.vue";
import PopularBeers from "./PopularBeer.vue";
import TopRussianMeed from "./TopRussianMeed.vue";
const props = defineProps({
  title: String,
  beers: Array,
  fetchMore: Function,
});

const container = ref(null);

const onScroll = () => {
  const el = container.value;
  if (el && el.scrollWidth - el.scrollLeft <= el.clientWidth + 50) {
    props.fetchMore();
  }
};

onMounted(() => {
  container.value.addEventListener("scroll", onScroll);
});
</script>

<template>
  <div>
    <h2 class="text-2xl font-semibold flex items-center">
      <img src="../assets/beerlogo.png" alt="beer" class="w-6 h-6 mr-2" />
      {{ title }}
    </h2>
    <p class="text-gray-500">Most rated beers of all time</p>
    <div
      ref="container"
      class="flex space-x-4 overflow-x-auto no-scrollbar mt-4 pb-2"
    >
      <BeerCard v-for="beer in beers" :key="beer.id" :beer="beer" />
      <div class="container mx-auto px-4">
    <PopularBeers />
    <TopRussianMeed />
  </div>
    </div>
  </div>
</template>

<style scoped>
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>


<!-- <script setup>
import { ref, onMounted } from 'vue';
import BeerCard from './BeerCard.vue';

const popularBeers = ref([]);
const topRussianMeed = ref([]);

const fetchBeers = async () => {
  // Здесь замени на реальные API-запросы
  popularBeers.value = Array(4).fill({
    id: 1,
    name: 'Wrong honey',
    brewery: 'Wolfsbrewery',
    country: 'Russia',
    rating: 4.5,
    reviews: 5312,
    price: 78,
    image: '/src/assets/beer.png',
  });

  topRussianMeed.value = Array(4).fill({
    id: 2,
    name: 'Wrong honey',
    brewery: 'Wolfsbrewery',
    country: 'Russia',
    rating: 4.5,
    reviews: 5312,
    price: 78,
    image: '/src/assets/beer.png',
  });
};

onMounted(fetchBeers);
</script>

<template>
  <div class="container mx-auto px-4">
    <h2 class="text-2xl font-semibold flex items-center">
      <img src=".././assets/beerlogo.png" alt="beer" class="w-6 h-6 mr-2" />
      Popular beers
    </h2>
    <p class="text-gray-500">Most rated beers of all time</p>
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mt-4">
      <BeerCard v-for="beer in popularBeers" :key="beer.id" :beer="beer" />
    </div>

    <h2 class="text-2xl font-semibold flex items-center mt-8">
      <img src=".././assets/beerlogo.png" alt="beer" class="w-6 h-6 mr-2" />
      Top Russians meed
    </h2>
    <p class="text-gray-500">Most rated Russian meed of all time</p>
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mt-4">
      <BeerCard v-for="beer in topRussianMeed" :key="beer.id" :beer="beer" />
    </div>
  </div>
</template> -->
