<template>
  <div class="review-card">
    <div class="review-header">
      <div class="beer-container" @click="goToBeerDetails">
        <img
          :src="review.beer.imagePath"
          :alt="review.beer.beerName"
          class="beer-image"
        />
      </div>
      <div class="review-info">
        <div class="review-date">{{ formatDate(review.createdAt) }}</div>
        <div class="review-rating">
          {{ review.rating.toFixed(1) }}
          <span class="stars">{{ getStars(review.rating) }}</span>
        </div>
        <div class="review-text" :title="review.comment">
          {{ review.comment }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReviewCard",
  props: {
    review: Object,
  },
  methods: {
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString();
    },
    getStars(rating) {
      const fullStars = Math.floor(rating);
      const halfStar = rating % 1 >= 0.5 ? 1 : 0;
      const emptyStars = 5 - fullStars - halfStar;
      return (
        "★".repeat(fullStars) + "½".repeat(halfStar) + "☆".repeat(emptyStars)
      );
    },
    goToBeerDetails() {
      this.$router.push({
        name: "BeerDetail",
        params: { beerId: this.review.beer.beerId },
      });
    },
  },
};
</script>

<style scoped>
.review-card {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 15px;
  width: 20vw;
  margin: auto;
  border-bottom: 1px solid #ddd;
  display: flex;
  align-items: center;
}

.review-header {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  width: 100%;
}

.beer-container {
  width: 80px;
  height: 100px;
}

.beer-image {
  width: 100%;
  height: 100%;
  object-fit: contain; /* Масштабирование картинки без обрезки */
  border-radius: 5px;
}

.review-info {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  width: 100%;
}

.review-date {
  font-size: 14px;
  color: #666;
  text-align: left;
}

.review-rating {
  font-size: 16px;
  font-weight: bold;
  margin-top: 5px;
  text-align: left;
}

.stars {
  color: orange;
}

.review-text {
  margin-top: 10px;
  font-size: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  word-break: break-word;
  text-indent: 0px;
  text-align: left;
  max-height: 3em;
  transition: max-height 1.1s ease, padding-bottom 1.1s ease;
}

.review-text:hover {
  -webkit-line-clamp: unset;
  overflow: visible;
  white-space: normal;
  max-height: 100em;
}
</style>
