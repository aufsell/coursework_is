<script setup>
import { ref } from "vue";
import BeerList from "./BeerList.vue";

const beers = ref([
    // Update this data with real data
  {
    id: 2,
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
  <BeerList title="Top Russians meed" :beers="beers" :fetchMore="fetchMore" />
</template>
