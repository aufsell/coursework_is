<template>
  <div class="header">
    <Header />
  </div>
  <div class="main">
    <div v-if="beer" class="beer-container">
      <img :src="beer.image_path" alt="Изображение пива" class="beer-image" />
      <div class="beer-details">
        <h2>{{ beer.name }}</h2>
        <p><strong>abv:</strong> {{ beer.abv }}</p>
        <p><strong>Страна:</strong> {{ beer.country }}</p>
        <p><strong>Тип ферментации:</strong> {{ fermentationType }}</p>
        <p><strong>IBU:</strong> {{ beer.ibu }}</p>
        <p><strong>Цена:</strong> {{ beer.price }}</p>
        <p><strong>OG:</strong> {{ beer.og }}%</p>
        <p><strong>SRM:</strong> {{ beer.srm }}</p>
        <p><strong>Объем:</strong> {{ beer.volume }}</p>

        <button @click="toggleFavourite" :class="{ favourite: isFavourite }">
          {{ isFavourite ? "Убрать из избранного" : "Добавить в избранное" }}
        </button>
      </div>
    </div>
  </div>

  <div class="reviews-container">
    <h3>Отзывы</h3>
    <div v-if="reviews.length > 0">
      <div v-for="review in reviews" :key="review.reviewId" class="review-card">
        <div class="review-header">
          <div class="avatar-container">
            <img
              :src="
                review.profile.avatarPath
                  ? review.profile.avatarPath
                  : require('@/assets/user.png')
              "
              :alt="
                review.profile.avatarPath
                  ? 'Аватар пользователя'
                  : 'Аватар по умолчанию'
              "
              class="avatar-image"
            />
          </div>

          <div class="review-info">
            <div class="review-date">{{ formatDate(review.created_at) }}</div>
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
    </div>
    <p v-else>Пока нет отзывов</p>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import axios from "axios";
import Header from "./Header.vue";

export default {
  components: {
    Header,
  },
  props: {
    beerId: String,
  },
  methods: {
    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    getStars(rating) {
      const fullStars = Math.floor(rating);
      const halfStar = rating % 1 >= 0.5 ? 1 : 0;
      const emptyStars = 5 - fullStars - halfStar;
      return (
        "★".repeat(fullStars) + "½".repeat(halfStar) + "☆".repeat(emptyStars)
      );
    },
    goToUserProfile() {
      this.$router.push({ path: `/profile/${this.review.profile.profileId}` });
    },
  },
  setup(props) {
    const beer = ref(null);
    const fermentationType = ref("");
    const reviews = ref([]);
    const isFavourite = ref(false);

    const fetchBeerData = async () => {
      try {
        const beerResponse = await axios.get(
          `http://localhost:7777/api/v1/beer/beers/search?id=${props.beerId}`,
          {
            withCredentials: true,
          }
        );

        const fermentationResponse = await axios.get(
          `http://localhost:7777/api/v1/beer/fermentationType/${beerResponse.data[0].fermentation_type}`,
          {
            withCredentials: true,
          }
        );

        beer.value = beerResponse.data[0];
        fermentationType.value = fermentationResponse.data?.name || "";
      } catch (error) {
        console.error("Ошибка при загрузке данных:", error);
      }
    };

    const fetchReviews = async () => {
      try {
        const response = await axios.get(
          `http://localhost:7777/api/v1/reviews/beer/${props.beerId}`,
          {
            withCredentials: true,
          }
        );

        reviews.value = response.data.content;
      } catch (error) {
        console.error("Ошибка при загрузке отзывов:", error);
      }
    };

    const checkFavouriteStatus = async () => {
      try {
        const response = await axios.get(
          `http://localhost:7777/api/v1/favourite/${props.beerId}/isFavourite`,
          {
            withCredentials: true,
          }
        );
        isFavourite.value = response.data; // Предполагается, что API возвращает true/false
      } catch (error) {
        console.error("Ошибка при проверке статуса избранного:", error);
      }
    };

    const toggleFavourite = async () => {
      try {
        if (isFavourite.value) {
          await axios.post(
            `http://localhost:7777/api/v1/favourite/remove/${props.beerId}`,
            {},
            {
              withCredentials: true,
            }
          );
        } else {
          await axios.post(
            `http://localhost:7777/api/v1/favourite/add/${props.beerId}`,
            {},
            {
              withCredentials: true,
            }
          );
        }
        isFavourite.value = !isFavourite.value;
      } catch (error) {
        console.error("Ошибка при переключении избранного:", error);
      }
    };

    onMounted(() => {
      fetchBeerData();
      fetchReviews();
      checkFavouriteStatus();
    });

    return { beer, fermentationType, reviews, isFavourite, toggleFavourite };
  },
};
</script>

<style scoped>
.header {
  width: 43vw;
  margin-left: 28.5%;
}
.main {
  width: 100vw;
  background-color: #ffc473;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.beer-container {
  display: flex;
  align-items: center;
  padding-left: 30vw;
}

.beer-image {
  width: 150px;
  border-radius: 8px;
  margin-right: 20px;
}

.beer-details {
  padding-left: 15vw;
}

h2 {
  font-size: 24px;
  font-weight: bold;
  margin-top: 10px;
}

button {
  margin-top: 20px;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #007bff;
  color: white;
  font-size: 16px;
}

button.favourite {
  background-color: #28a745;
}

.reviews-container {
  width: 40vw;
  background-color: #ffffff;
  margin-left: 30vw;
}

.review-card {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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

.avatar-container {
}

.avatar-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
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
</style>
