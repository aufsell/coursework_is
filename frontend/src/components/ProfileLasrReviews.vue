<template>
  <section class="latest-reviews">
    <div class="xz">
      <div class="description">
        <h2><b>Последние отзывы</b></h2>
      </div>
    </div>
    <div class="review-grid">
      <ReviewCard
        v-for="review in latestReviews"
        :key="review.reviewId"
        :review="review"
      />
    </div>
    <br />
    <br />
  </section>
</template>

<script>
import axios from "axios";
import ReviewCard from "./ReviewCardWithPhoto.vue";
import { toRaw } from "vue";

export default {
  name: "LastReviews",
  components: {
    ReviewCard,
  },
  props: {
    profileUserId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      reviews: [],
    };
  },
  computed: {
    latestReviews() {
      return this.reviews.slice(0, 4);
    },
  },
  watch: {
    profileUserId(newId, oldId) {
      if (newId !== oldId) {
        this.loadReviews();
      }
    },
  },
  created() {
    this.loadReviews();
  },
  methods: {
    async loadReviews() {
      if (!this.profileUserId) return;

      try {
        const response = await axios.get(
          `http://localhost:7777/api/v1/reviews/user/${this.profileUserId}?order=desc&sortBy=created_at`,
          { withCredentials: true }
        );
        this.reviews = toRaw(response.data.content).map((review) => ({
          reviewId: review.reviewId,
          rating: review.rating,
          comment: review.comment,
          createdAt: review.created_at,
          beer: {
            beerId: review.beerId,
            imagePath: review.beerImagePath,
          },
        }));
      } catch (error) {
        console.error("Ошибка при загрузке отзывов:", error);
      }
    },
  },
};
</script>

<style scoped>
.latest-reviews {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5vw;
  width: 100%;
}

.xz {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

h2 {
  font-size: 20px;
}

.review-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* По 2 отзыва в строке */
  gap: 20px;
  justify-content: center;
  width: 100%;
  max-width: 600px;
}

.description {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: start;
}
</style>
