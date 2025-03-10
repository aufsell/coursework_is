<template>
  <div v-if="beer">
    <img :src="beer.image_path" alt="Изображение пива">
    <h2>{{ beer.name }}</h2>
    <p><strong>abv:</strong> {{ beer.abv }}</p>
    <p><strong>Страна:</strong> {{ beer.country }}</p>
    <p><strong>Тип ферментации:</strong> {{ fermentationType }}</p>
    <p><strong>IBU:</strong> {{ beer.ibu }}</p>
    <p><strong>Цена:</strong> {{ beer.price }}</p>
    <p><strong>OG:</strong> {{ beer.og }}%</p>
    <p><strong>SRM:</strong> {{ beer.srm }}</p>
    <p><strong>Объем:</strong> {{ beer.volume }}</p>

    <button @click="toggleFavourite" :class="{'favourite': isFavourite}">
      {{ isFavourite ? 'В избранном' : 'Добавить в избранное' }}
    </button>

    <h3>Отзывы</h3>
    <div v-if="reviews.length > 0">
      <div v-for="review in reviews" :key="review.reviewId" class="review-card">
        <div class="review-header">
          <img v-if="review.profile.avatarPath" :src="review.profile.avatarPath" alt="Аватар пользователя" class="avatar">
          <span>{{ review.profile.firstName }} {{ review.profile.lastName }}</span>
        </div>
        <p><strong>Рейтинг:</strong> {{ review.rating }}</p>
        <p>{{ review.comment }}</p>
        <p><small>Дата: {{ new Date(review.created_at).toLocaleDateString() }}</small></p>
      </div>
    </div>
    <p v-else>Пока нет отзывов</p>
  </div>
  <p v-else>Загрузка...</p>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  props: {
    beerId: String
  },
  setup(props) {
    const beer = ref(null);
    const fermentationType = ref('');
    const reviews = ref([]);
    const isFavourite = ref(false);

    const fetchBeerData = async () => {
      try {
        const beerResponse = await axios.get(`http://localhost:7777/api/v1/beer/beers/search?id=${props.beerId}`, {
          withCredentials: true,
        });

        const fermentationResponse = await axios.get(`http://localhost:7777/api/v1/beer/fermentationType/${beerResponse.data[0].fermentation_type}`, {
          withCredentials: true,
        });

        beer.value = beerResponse.data[0];
        fermentationType.value = fermentationResponse.data?.name || '';

      } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
      }
    };

    const fetchReviews = async () => {
      try {
        const response = await axios.get(`http://localhost:7777/api/v1/reviews/beer/${props.beerId}`, {
          withCredentials: true,
        });

        reviews.value = response.data.content;
      } catch (error) {
        console.error('Ошибка при загрузке отзывов:', error);
      }
    };


    const toggleFavourite = async () => {
      try {
        await axios.post(`http://localhost:7777/api/v1/favourite/add/${props.beerId}`, {}, {
          withCredentials: true,
        });
        isFavourite.value = !isFavourite.value;
      } catch (error) {
        console.error('Ошибка при добавлении в избранное:', error);
      }
    };

    onMounted(() => {
      fetchBeerData();
      fetchReviews();
      
    });

    return { beer, fermentationType, reviews, isFavourite, toggleFavourite };
  },
};
</script>

<style scoped>
.review-card {
  border: 1px solid #ddd;
  padding: 10px;
  margin-top: 10px;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: bold;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

button {
  margin-top: 10px;
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
</style>
