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

    <!-- Форма для добавления отзыва -->
    <div class="review-form">
      <h4>Оставить отзыв</h4>
      <div class="rating-input">
        <label>Рейтинг:</label>
        <input
          type="number"
          v-model="newReview.rating"
          min="0.0"
          max="5.0"
          step="0.1"
          required
          @input="validateRating"
        />
      </div>
      <textarea
        v-model="newReview.comment"
        placeholder="Ваш комментарий..."
        required
      ></textarea>
      <button @click="submitReview">Отправить отзыв</button>
    </div>

    <!-- Существующие отзывы -->
    <div v-if="reviews.length > 0">
      <div v-for="review in reviews" :key="review.reviewId" class="review-card">
        <div class="review-header">
          <div
            class="avatar-container"
            @click="goToUserProfile(review.profile.profileId)"
          >
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
            <div>{{ review.profile.name }}</div>
            <div class="review-date">{{ formatDate(review.created_at) }}</div>
            <div class="review-rating">
              {{ review.rating.toFixed(1) }}
              <span class="stars">{{ getStars(review.rating) }}</span>
            </div>
            <div
              v-if="
                !editingReview || editingReview.reviewId !== review.reviewId
              "
              class="review-text"
              :title="review.comment"
            >
              {{ review.comment }}
            </div>
            <!-- Форма редактирования -->
            <div v-else class="edit-form">
              <div class="rating-input">
                <label>Рейтинг:</label>
                <input
                  type="number"
                  v-model="editingReview.rating"
                  min="0.0"
                  max="5.0"
                  step="0.1"
                  required
                  @input="validateRatingEdit"
                />
              </div>
              <textarea v-model="editingReview.comment" required></textarea>
              <button @click="saveEditedReview(review.reviewId)">
                Сохранить
              </button>
              <button @click="cancelEdit">Отмена</button>
            </div>
          </div>
          <button
            v-if="canEditReview(review)"
            @click="startEdit(review)"
            class="edit-button"
          >
            Редактировать
          </button>
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
    goToUserProfile(profileId) {
      this.$router.push({ path: `/profile/${profileId}` });
    },
    // Проверка, может ли пользователь редактировать отзыв
    canEditReview(review) {
      console.log(localStorage.getItem("userId"));
      console.log(review.profile.profileId);
      const userId = JSON.parse(localStorage.getItem("userId"));
      const role = JSON.parse(localStorage.getItem("role")); // Предполагается, что ID пользователя хранится в localStorage
      console.log(
        role === "admin" || (userId && review.profile.profileId === userId)
      );
      return (
        role === "admin" || (userId && review.profile.profileId === userId)
      );
    },
    // Начало редактирования
    startEdit(review) {
      this.editingReview = { ...review };
    },
    // Валидация рейтинга при редактировании
    validateRatingEdit() {
      let value = parseFloat(this.editingReview.rating);
      if (value < 0.1) {
        this.editingReview.rating = 0.1;
      } else if (value > 5.0) {
        this.editingReview.rating = 5.0;
      }
    },
    // Сохранение отредактированного отзыва
    async saveEditedReview(reviewId) {
      try {
        if (!this.editingReview.rating || !this.editingReview.comment) {
          alert("Пожалуйста, заполните рейтинг и комментарий");
          return;
        }

        let rating = parseFloat(this.editingReview.rating);
        if (rating < 0.1 || rating > 5.0) {
          alert("Рейтинг должен быть от 0.1 до 5.0");
          return;
        }

        await axios.patch(
          `http://localhost:7777/api/v1/reviews/${reviewId}`,
          {
            rating: this.editingReview.rating,
            comment: this.editingReview.comment,
            beerId: this.beerId,
          },
          { withCredentials: true }
        );

        this.editingReview = null;
        await this.fetchReviews();
      } catch (error) {
        console.error("Ошибка при сохранении отзыва:", error);
        alert("Не удалось сохранить изменения");
      }
    },
    // Отмена редактирования
    cancelEdit() {
      this.editingReview = null;
    },
  },
  setup(props) {
    const beer = ref(null);
    const fermentationType = ref("");
    const reviews = ref([]);
    const isFavourite = ref(false);
    const newReview = ref({
      rating: null,
      comment: "",
      beerId: props.beerId,
    });
    const editingReview = ref(null); // Для хранения редактируемого отзыва

    const fetchBeerData = async () => {
      try {
        const beerResponse = await axios.get(
          `http://localhost:7777/api/v1/beer/beers/search?id=${props.beerId}`,
          { withCredentials: true }
        );

        const fermentationResponse = await axios.get(
          `http://localhost:7777/api/v1/beer/fermentationType/${beerResponse.data[0].fermentation_type}`,
          { withCredentials: true }
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
          { withCredentials: true }
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
          { withCredentials: true }
        );
        isFavourite.value = response.data;
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
            { withCredentials: true }
          );
        } else {
          await axios.post(
            `http://localhost:7777/api/v1/favourite/add/${props.beerId}`,
            {},
            { withCredentials: true }
          );
        }
        isFavourite.value = !isFavourite.value;
      } catch (error) {
        console.error("Ошибка при переключении избранного:", error);
      }
    };

    const validateRating = () => {
      let value = parseFloat(newReview.value.rating);
      if (value < 0.1) {
        newReview.value.rating = 0.1;
      } else if (value > 5.0) {
        newReview.value.rating = 5.0;
      }
    };

    const submitReview = async () => {
      try {
        if (!newReview.value.rating || !newReview.value.comment) {
          alert("Пожалуйста, заполните рейтинг и комментарий");
          return;
        }

        let rating = parseFloat(newReview.value.rating);
        if (rating < 0.1 || rating > 5.0) {
          alert("Рейтинг должен быть от 0.1 до 5.0");
          return;
        }

        await axios.post(
          `http://localhost:7777/api/v1/reviews/${props.beerId}`,
          newReview.value,
          { withCredentials: true }
        );

        newReview.value.rating = null;
        newReview.value.comment = "";
        await fetchReviews();
      } catch (error) {
        console.error("Ошибка при отправке отзыва:", error);
        alert("Не удалось отправить отзыв");
      }
    };

    onMounted(() => {
      fetchBeerData();
      fetchReviews();
      checkFavouriteStatus();
    });

    return {
      beer,
      fermentationType,
      reviews,
      isFavourite,
      toggleFavourite,
      newReview,
      submitReview,
      validateRating,
      editingReview,
      fetchReviews,
    };
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
  margin-bottom: 30px;
}
.review-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

.review-card {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #ddd;
  display: flex;
  align-items: center;
  margin-top: 10px;
  margin-bottom: 10px;
}

.review-header {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  width: 100%;
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
  -webkit-box-orient: vertical;
  overflow: hidden;
  word-break: break-word;
  text-indent: 0px;
  text-align: left;
  transition: max-height 1.1s ease, padding-bottom 1.1s ease;
}

.review-form {
  margin: 20px 0;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 5px;
}

.review-form h4 {
  margin-bottom: 15px;
}

.rating-input {
  margin-bottom: 10px;
}

.rating-input input {
  width: 60px;
  padding: 5px;
  margin-left: 10px;
}

.review-form textarea {
  width: 100%;
  min-height: 80px;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
}

.review-form button {
  background-color: #28a745;
  padding: 8px 15px;
}
.edit-form {
  margin-top: 10px;
}

.edit-form .rating-input {
  margin-bottom: 10px;
}

.edit-form input {
  width: 60px;
  padding: 5px;
  margin-left: 10px;
}

.edit-form textarea {
  width: 100%;
  min-height: 80px;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
}

.edit-form button {
  margin-right: 10px;
  background-color: #007bff;
}

.edit-button {
  margin-top: 10px;
  background-color: #ffc107;
  font-size: 14px;
  align-items: right;
}
</style>
