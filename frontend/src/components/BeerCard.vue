<template>
  <div class="beer-card">
    <div class="q1">
    <div class="img"><img :src="beer.imagePath" :alt="beer.beerName"></div>
    <div class="rating">{{ beer.averageRating.toFixed(1) }} <span class="stars">{{ getStars(beer.averageRating) }}</span></div>
    <div class="ratings-count">{{ beer.reviewCount }} отзывов</div>
    <div class="price">{{ beer.price.toFixed(2) }} ₽</div>
  </div>
    <div class="beer-details">{{ beer.beerName }}, {{ beer.country }}</div>
  </div>
</template>

<script>
export default {
  name: 'BeerCard',
  props: {
    beer: Object
  },
  methods: {
    getStars(rating) {
      const fullStars = Math.floor(rating);
      const halfStar = rating % 1 >= 0.5 ? 1 : 0;
      const emptyStars = 5 - fullStars - halfStar;
      return '★'.repeat(fullStars) + '½'.repeat(halfStar) + '☆'.repeat(emptyStars);
    }
  }
}
</script>

<style scoped>
.beer-card {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 10px;
  text-align: center;
}
.beer-card img {
  width: 100%;
  height: auto;
  border-radius: 10px;
}
.rating {
  font-size: 18px;
  font-weight: bold;
  margin: 10px 0;
}
.stars {
  color: orange;
}
.ratings-count {
  font-size: 14px;
  color: #666;
}
.q1{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.price {
  background-color: green;
  color: white;
  padding: 5px 10px;
  border-radius: 5px;
  display: inline-block;
  margin: 10px 0;
}
.beer-details {
  font-size: 16px;
  font-weight: bold;
}
.img{
  width: 40%;
  height: auto;
  
}
</style>