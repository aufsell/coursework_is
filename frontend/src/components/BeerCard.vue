<script setup>
defineProps({
  beer: Object
});
</script>



<template>
  <div class="border rounded-lg p-4 shadow-md">
    <img src=".././assets/beer.png" alt="beer" class="w-full h-48 object-contain" />
    <div class="text-lg font-semibold mt-2">{{ beer.rating }} ★</div>
    <div class="text-gray-500 text-sm">{{ beer.reviews }} ratings</div>
    <div class="text-green-600 font-bold mt-1">{{ beer.price }} ₽</div>
    <div class="mt-2 text-sm text-gray-700">
      <span class="font-semibold">{{ beer.brewery }}</span><br />
      {{ beer.name }}<br />
      {{ beer.country }}
    </div>
  </div>
</template>
