<script setup>
import { ref } from "vue";
import BeerList from "./BeerList.vue";

const beers = ref([
    // TODO: Replace with real data
  {
    id: 1,
    name: "Wrong honey",
    brewery: "Wolfsbrewery",
    country: "Russia",
    rating: 4.5,
    reviews: 5312,
    price: 78,
    image: ".././assets/beer.png",
  },
]);

const fetchMore = () => {
  beers.value.push(...Array(4).fill({ ...beers.value[0], id: beers.value.length + 1 }));
};
</script>

<template>
  <BeerList title="Popular beers" :beers="beers" :fetchMore="fetchMore" />
</template>
