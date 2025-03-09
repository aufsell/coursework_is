<template>
    <div class="beer-card-mini" @click="goToBeerDetails">
      <img class="beer-image" :src="beer.imagePath" :alt="beer.beerName">
      <div class="beer-info">
        <span class="beer-name">{{ beer.beerName }}</span>
        <span class="beer-country">({{ beer.country }})</span>
        <span class="beer-rating">{{ beer.averageRating.toFixed(1) }} ★</span>
        <span class="beer-price">{{ beer.price.toFixed(2) }} ₽</span>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'BeerCardMini',
    props: {
      beer: Object
    },
    methods: {
      goToBeerDetails() {
        this.$router.push({ name: 'BeerDetail', params: { beerId: this.beer.beerId } });
      }
    }
  }
  </script>
  
  <style scoped>
  .beer-card-mini {
    display: flex;
    align-items: center;
    padding: 8px;
    border-bottom: 1px solid #ddd;
    cursor: pointer;
  }
  
  .beer-card-mini:hover {
    background-color: #f9f9f9;
  }
  
  .beer-image {
    width: 20px;
    height: 25px;
    border-radius: 5px;
    margin-right: 10px;
  }
  
  .beer-info {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 14px;
  }
  
  .beer-name {
    font-weight: bold;
  }
  
  .beer-country {
    color: #777;
  }
  
  .beer-rating {
    color: orange;
  }
  
  .beer-price {
    background-color: green;
    color: white;
    padding: 2px 5px;
    border-radius: 3px;
  }
  </style>
  