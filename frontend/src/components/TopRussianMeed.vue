<template>
  <section>
    <div class="xz">
    <img src="../assets/beerlogo.png" alt="Кружка пива">
    <div class="description">
      <h2><b>Рекомендуем вам</b></h2>
      <p>Подборка на основе ваших предпочтений
      </p>
    </div>
  </div>
    <div class="beer-grid">
      <BeerCard v-for="beer in russianBeers" :key="beer.beerId" :beer="beer" />
    </div>
  </section>
</template>

<script>
import BeerCard from './BeerCard.vue';

export default {
  name: 'TopRussianMeads',
  components: {
    BeerCard
  },
  data() {
    return {
      beers: [
        { beerId: 2, beerName: "Corona Extra", imagePath: "http://localhost:9000/pipivo/beer_images/beer_1.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=lW4rzRQue17OqXNFYiQB%2F20250309%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250309T160030Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=b0ac1cd57ba5b661be8fd60e5cbbbc23b3bd5bdc33b7ceddcaf7eab7c58bcf5e", price: 340.55, averageRating: 2.83, country: "Germany", reviewCount: 10 },
        { beerId: 4, beerName: "Corona Extra", imagePath: "http://localhost:9000/pipivo/beer_images/beer_1.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=lW4rzRQue17OqXNFYiQB%2F20250309%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250309T160030Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=b0ac1cd57ba5b661be8fd60e5cbbbc23b3bd5bdc33b7ceddcaf7eab7c58bcf5e", price: 156.56, averageRating: 2.58, country: "Germany", reviewCount: 6 },
        { beerId: 9, beerName: "Paulaner", imagePath: "http://localhost:9000/pipivo/beer_images/beer_3.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=lW4rzRQue17OqXNFYiQB%2F20250309%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250309T160030Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=229183878069709f3684f0408b8e8e5a317921da1d9852abb12a4111111e393b", price: 307.66, averageRating: 3.62, country: "Germany", reviewCount: 5 },
        { beerId: 15, beerName: "Paulaner", imagePath: "http://localhost:9000/pipivo/beer_images/beer_3.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=lW4rzRQue17OqXNFYiQB%2F20250309%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250309T160030Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=229183878069709f3684f0408b8e8e5a317921da1d9852abb12a4111111e393b", price: 125.63, averageRating: 2.14, country: "Germany", reviewCount: 5 }
      ]
    };
  },
  computed: {
    russianBeers() {
      return this.beers.filter(beer => beer.country != null);
    }
  }
}
</script>

<style scoped>
section {
  margin-bottom: 40px;
}
main{
  display: flex;
  justify-content: center;
  align-items: center;
}
.xz {
  display: flex;
  align-items:  center;
  justify-content: left;
 
  padding-left: 0;
  margin-bottom: 20px;
}
h2 {

font-size: 20px;

}
h2 img{
  width: 50px;
  height: 50px;
  margin-right: 10px;
}
.beer-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.description{
  display: flex;
  flex-direction: column;
  justify-content: center; 
  align-items: start;
  
}
</style>